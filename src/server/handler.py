import bcrypt
import os
import argon2
from argon2.exceptions import VerifyMismatchError
from nanoid import generate
from flask import request, jsonify, current_app
from io import BytesIO
from .data import users, padi_datas
from src.services.store_data import store_user_data, store_prediction_data, get_prediction_data
from src.services.inference_service import predict_image
from google.cloud import firestore
from werkzeug.utils import secure_filename
from datetime import datetime
from google.cloud import storage

argon_hash = argon2.PasswordHasher()

def regis_user_handler():
    data = request.get_json()
    name = data.get('name')
    email = data.get('email')
    password = data.get('password')

    if not all([name, email, password]):
        return jsonify({'status': 'fail', 'message': 'Mohon isi seluruh data'}), 400

    if any(user['email'] == email for user in users):
        return jsonify({'status': 'fail', 'message': 'Email sudah terdaftar'}), 400
    
    try:
        hashed_password = argon_hash.hash(password.encode('utf-8'))
        token = generate(size=16)
    
        new_user = {
            'token': token,
            'name': name,
            'email': email,
            'hashed_password': hashed_password
        }
    
        users.append(new_user)
        store_user_data(new_user)
        
        return jsonify({
            'status': 'success',
            'message': 'User berhasil ditambahkan',
            'user': {'token': token, 'name': name, 'email': email}
        }), 201
    
    except Exception as e:
         print(f"An error occurred during registration: {e}")
         return jsonify({"status": "error", "message": f"A server-side error occurred"}), 500

def login_user_handler():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    if not all([email, password]):
        return jsonify({'status': 'fail', 'message': 'Mohon isi email dan password'}), 400

    user = next((user for user in users if user['email'] == email), None)
    
    if not user or 'hashed_password' not in user:
        return jsonify({'status': 'fail', 'message': 'Email atau password salah'}), 401

    if not verify_password(user['hashed_password'], password):
        return jsonify({'status': 'fail', 'message': 'Email atau password salah'}), 401

    return jsonify({
        'status': 'success',
        'token': user['token'],
        'message': 'Selamat datang di SmartPaddy'
    }), 200

def verify_password(stored_hash, password):
    try:
        return argon_hash.verify(stored_hash, password.encode())
    except VerifyMismatchError:
        return False

def upload_img_to_bucket(bucket_name, image_file, image_filename, predict_id):
    try:
        client = storage.Client()
        bucket = client.bucket(bucket_name)
    
        file_extension = image_filename.split('.')[-1]
        unique_filename = f"{predict_id}.{file_extension}"
    
        blob = bucket.blob(unique_filename)
        blob.upload_from_file(image_file, content_type=image_file.content_type)
    
        blob.make_public()
    
        return blob.public_url
        
    except Exception as e:
        print(f"Error during upload to bucket: {e}")
        raise 

def padi_data_predict():
    model = current_app.config['MODEL']
    bucket_name = os.getenv("BUCKET_NAME")

    if not bucket_name:
        return jsonify({"status": "error", "message": "Bucket name is not configured"}), 500

    image_file = request.files.get('imageUri')
    user_id = request.form.get('userIds')
    predict_id = generate(size=16)

    # Error Handling 
    if not image_file:
        return jsonify({"status": "fail", "message": "Image file is required"}), 400
    if not user_id:
        return jsonify({"status": "fail", "message": "User ID is required"}), 400

    try:
        #Handling untuk prediksi data
        image_stream = BytesIO(image_file.read())
        image_stream.seek(0)
        result = predict_image(image_stream, model)

        created_at = datetime.utcnow()

        # Handling untuk gambar tidak jelas
        if result == False:
            return jsonify({"status": "fail", "message": "Image is not recognizable. Please use a better Image!"}), 415

        # Handling untuk upload gambar ke cloud storage
        image_file.stream.seek(0)
        image_filename= image_file.filename
        image_url = upload_img_to_bucket(bucket_name, image_file, image_filename, predict_id)

        padi_data = {
            "user_id": user_id,
            "created_at": created_at,
            "predict_id": predict_id,
            "image_url": image_url,
            "result": result
        }

        store_prediction_data(predict_id, padi_data)

        return jsonify({"status": "success", "data": padi_data}), 200

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

def get_post_detail(predict_id):
    padi_data_item = get_prediction_data(predict_id)

    if padi_data_item:
        return jsonify({
            'status': 'success',
            'data': {
                'result': padi_data_item['result']
            }
        }), 200
    else:
        return jsonify({'status': 'fail', 'message': 'Data tidak ditemukan'}), 404

def get_history(user_id):
    db = firestore.Client()
    predictions_ref = db.collection('predictions')
    query = predictions_ref.where('user_id', '==', user_id).stream()

    history = []
    for doc in query:
        history.append(doc.to_dict())

    if history:
        response_history = [{
            "predict_id": data['predict_id'],
            "image_url": data['image_url'],
            "created_at": data['created_at'],
            "result": data['result']
        } for data in history]

        return jsonify({'status': 'success', 'data': response_history}), 200

    return jsonify({'status': 'fail', 'message': 'History tidak ditemukan'}), 404
