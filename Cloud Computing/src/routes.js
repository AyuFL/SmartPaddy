/* eslint-disable linebreak-style */
const { regisUserHandler, loginUserHandler, padiDatsaHandler, getPostDetail, getHistory } = require('./handler');

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
  },
  {
    method: 'GET',
    path: '/history/{id}',
    handler: getHistory,
  }
];

module.exports = routes;