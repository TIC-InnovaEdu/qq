require('dotenv').config(); // Cargar variables de entorno desde el archivo .env

const express = require('express');
const mongoose = require('mongoose');

const app = express();

// Middleware para procesar JSON
app.use(express.json());

// Conectar a MongoDB
mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
  .then(() => console.log('Conexión exitosa a MongoDB'))
  .catch((error) => console.error('Error al conectar a MongoDB:', error));

// Rutas
const authRoutes = require('./routes/authRoutes');
app.use('/auth', authRoutes);

// Iniciar el servidor
const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
  console.log(`Auth Service corriendo en el puerto ${PORT}`);
});
