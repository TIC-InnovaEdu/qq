const express = require('express');
const router = express.Router();

// Registrar un usuario
router.post('/register', (req, res) => {
  const { name, email, password } = req.body;
  // Lógica para registrar el usuario (validación, guardar en base de datos, etc.)
  res.json({ message: 'Usuario registrado con éxito' });
});

// Iniciar sesión
router.post('/login', (req, res) => {
  const { email, password } = req.body;
  // Lógica para autenticar al usuario
  res.json({ message: 'Inicio de sesión exitoso', token: 'fake-jwt-token' });
});

module.exports = router;
