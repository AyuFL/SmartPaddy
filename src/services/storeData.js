/* eslint-disable linebreak-style */
const { Firestore } = require('@google-cloud/firestore');

// Logic collection users pada firestore
async function storeUserData() {
  const db = new Firestore();

  const userCollection = db.collection('users');
  return userCollection.doc().set();
}

// Logic collection prediction data pada firestore
async function storePredictionData(id, data) {
  const db = new Firestore();

  const predictCollection = db.collection('predictions');
  return predictCollection.doc(id).set(data);
}

module.exports = { storeUserData, storePredictionData };