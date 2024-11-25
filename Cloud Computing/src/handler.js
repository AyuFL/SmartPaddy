/* eslint-disable linebreak-style */
const { nanoid } = require('nanoid');
const users = require('./data');
const padiDatas = require('./data');

const regisUserHandler = (request, h) => {
  const { name, email, password } = request.payload;

  const token = nanoid(16);

  const newUser = {
    token,
    name,
    email,
    password
  };

  if (!name || !email || !password){
    const response = h.response({
      status: 'fail',
      message: 'Mohon isi seluruh data'
    });
    response.code(400);
    return response;
  }

  const existingUser = users.find((user) => user.email === email);
  if (existingUser) {
    const response = h.response({
      status: 'fail',
      message: 'Email sudah terdaftar'
    });
    response.code(400);
    return response;
  }

  users.push(newUser);

  const response = h.response({
    status: 'success',
    message: 'User berhasil ditambahkan',
    user: {
      token,
      name,
      email,
      password
    }
  });
  response.code(201);
  return response;
};

const loginUserHandler = (request, h) => {
  const { email, password } = request.payload;

  if (!email || !password) {
    const response = h.response({
      status: 'fail',
      message: 'Mohon isi email dan password'
    });
    response.code(400);
    return response;
  }

  const user = users.find((user) => user.email === email);

  if (!user || user.password !== password) {
    const response = h.response({
      status: 'fail',
      message: 'Email atau password salah'
    });
    response.code(401);
    return response;
  }

  const response = h.response({
    status: 'success',
    token: user.token,
    message: 'Selamat datang di SmartPaddy'
  });
  response.code(200);
  return response;
};

const padiDatsaHandler = (request, h) => {
  const { userIds, imageUri } = request.payload;
  const { label, score, desc, cPenanggulangan, cMengobati } = request.params;

  const newPadiDatas = {
    userIds,
    imageUri
  };

  if (!userIds) {
    const response = h.response({
      status: 'fail',
      message: 'User tidak ditemukan'
    });
    response.code(401);
    return response;
  };

  if (!imageUri) {
    const response = h.response({
      status: 'fail',
      message: 'Gambar tidak ditemukan'
    });
    response.code(401);
    return response;
  }

  padiDatas.push(newPadiDatas);

  const response = h.response({
    status: 'success',
    message: 'Data berhasil diterima',
    padiDatas: {
      imageUri,
      label,
      score,
      desc,
      cPenanggulangan,
      cMengobati
    }
  });
  response.code(201);
  return response;
};

// const getPadiDatasHandler = (request, h) => {
//   const { imageUri, label, score, desc, cPenanggulangan, cMengobati } = request.params;

//   const padiData = padiDatas.filter((n) => n.imageUri === imageUri);

//   if (padiData !== undefined) {
//     return {
//       status: 'success',
//       message: 'Data ditemukan',
//       padiDatas: {
//         imageUri,
//         label,
//         score,
//         desc,
//         cPenanggulangan,
//         cMengobati
//       },
//     };
//   };

//   const response = h.response({
//     status: 'fail',
//     message: 'Data tidak ditemukan',
//   });
//   response.code(404);
//   return response;

// };

const getPostDetail = (request, h) => {
  const { id, label, score, desc, cPenanggulangan, cMengobati } = request.params;

  const padiData = padiDatas.filter((n) => n.id === id)[0];

  if (padiData !== undefined) {
    return {
      status: 'success',
      data: {
        padiData,
        label,
        score,
        desc,
        cPenanggulangan,
        cMengobati
      },
    };
  };

  const response = h.response({
    status: 'fail',
    message: 'Post tidak ditemukan',
  });
  response.code(404);
  return response;
};


module.exports = { regisUserHandler, loginUserHandler, padiDatsaHandler, getPostDetail };