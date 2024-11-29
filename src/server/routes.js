/* eslint-disable linebreak-style */
const { regisUserHandler, loginUserHandler, padiDatsaHandler, getPostDetail, getHistory } = require('./handler');

const routes = [
  { // Method yang mengatur logic register akun
    method: 'POST',
    path: '/register',
    handler: regisUserHandler,
  },
  { // Method yang mengatur logic login akun
    method: 'POST',
    path: '/login',
    handler: loginUserHandler,
  },
  { // Method yang mengatur logic scan/tangkap gambar untuk melakukan predeksi
    method: 'POST',
    path: '/scan',
    handler: padiDatsaHandler,
    options: {
      payload: {
        allow: 'multipart/form-data',
        multipart: true,
        // maxBytes : 1000000
      }
    }
  },
  { // Method yang mengatur logic detail post
    method: 'GET',
    path: '/post/{id}',
    handler: getPostDetail,
  },
  { // Method yang mengatur logic history
    method: 'GET',
    path: '/history/{id}',
    handler: getHistory,
  }
];

module.exports = routes;