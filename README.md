# SmartPaddy API Documentation

## Overview
The SmartPaddy API provides endpoints to manage user authentication, perform image-based predictions, and retrieve historical user data. This guide outlines the available routes, their expected inputs, and outputs.

## How to Run

### Prerequisites
1. Ensure you have Python installed (version 3.8 or later).
2. Install `pip` (Python package manager).
3. Clone this repository to your local machine.
4. Set up a virtual environment for dependency management (recommended).

### Steps to Run

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. **Create a Virtual Environment**:
   ```bash
   python3 -m venv venv
   source venv/bin/activate  
   # On Windows: venv\Scripts\activate
   ```

3. **Install Dependencies**:
   ```bash
   pip install -r requirements.txt
   ```

4. **Set Environment Variables**:
   - Create a `.env` file in the root directory.
   - Add the necessary environment variables such as:
     ```env
     MODEL_URL=your_secret_key
     ```

5. **Run the Flask Application**:
   ```bash
   python run.py
   ```

6. **Access the API**:
   Open your browser or API client (e.g., Postman) and navigate to:
   ```
   http://127.0.0.1:5000
   ```


---

## Endpoints

### 1. **User Registration**
**Route:** `/register`  
**Method:** `POST`

**Description:**  
Registers a new user. The `regis_user_handler` function processes the request.

---

### 2. **User Login**
**Route:** `/login`  
**Method:** `POST`

**Description:**  
Authenticates an existing user. The `login_user_handler` function processes the request.

---

### 3. **Scan Paddy Image**
**Route:** `/scan`  
**Method:** `POST`

**Description:**  
Processes an image of paddy plants to make predictions.

#### Expected Input:
- **Image**: A `.png` or `.jpg` file.
- **UserIds**: A string used for authentication.

#### Expected Outputs:

1. **Case 1: Successful Prediction**  
**HTTP Status Code:** 200
```json
{
    "data": {
        "predict_id": "String",
        "result": {
            "c_menangani": "String",
            "gejala": "String",
            "penjelasan": "String",
            "predicted_class": "String",
            "predicted_prob": Float
        },
        "user_id": "String"
    },
    "status": "success"
}
```

2. **Case 2: Low Accuracy but Valid Prediction**  
**HTTP Status Code:** 200
```json
{
    "data": {
        "predict_id": "String",
        "result": {
            "c_menangani": "String",
            "gejala": "String",
            "message": "Under the model Accuracy",
            "penjelasan": "String",
            "predicted_class": "String",
            "predicted_prob": Float
        },
        "user_id": "String"
    },
    "status": "success"
}
```

3. **Case 3: Image Not Provided**  
**HTTP Status Code:** 400
```json
{
    "message": "Image file is required",
    "status": "fail"
}
```

4. **Case 4: User ID Not Provided**  
**HTTP Status Code:** 400
```json
{
    "message": "User ID is required",
    "status": "fail"
}
```

5. **Case 5: Invalid or Unrecognizable Image**  
**HTTP Status Code:** 415
```json
{
    "message": "Image is not recognizable. Please use a better Image!",
    "status": "fail"
}
```

---

### 4. **Get Post Details**
**Route:** `/post/<string:post_id>`  
**Method:** `GET`

**Description:**  
Retrieves details of a specific post by its `post_id`. The `get_post_detail` function processes the request.

---

### 5. **User History**
**Route:** `/history/<string:user_id>`  
**Method:** `GET`

**Description:**  
Retrieves the historical data associated with a user.

#### Expected Input:
- **user_id**: The ID of the user.

#### Expected Outputs:

1. **Case 1: Data Retrieved Successfully**  
**HTTP Status Code:** 200
```json
{
    "data": [
        {},
        {}
    ]
}
```

2. **Case 2: User Not Found or Invalid ID**  
**HTTP Status Code:** 404
```json
{
    "message": "History tidak ditemukan",
    "status": "fail"
}
```

---




