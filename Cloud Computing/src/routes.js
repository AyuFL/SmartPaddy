/* eslint-disable linebreak-style */
const { regisUserHandler, loginUserHandler, padiDatsaHandler, getPostDetail } = require('./handler');

const routes = [
  {
    method: 'POST',
    path: '/register',
    handler: regisUserHandler,
  },
  {
    method: 'POST',
    path: '/login',
    handler: loginUserHandler,
  },
  {
    method: 'POST',
    path: '/scan',
    handler: padiDatsaHandler,
  },
  {
    method: 'GET',
    path: '/post/{id}',
    handler: getPostDetail,
  }
];

module.exports = routes;