// index.js

const express = require('express');
const app = express();
const port = 4001; // Puedes cambiar el puerto si es necesario

app.get('/', (req, res) => {
  res.send('¡Producto Service funcionando!');
});

app.listen(port, () => {
  console.log(`Servidor ejecutándose en http://localhost:${port}`);
});
