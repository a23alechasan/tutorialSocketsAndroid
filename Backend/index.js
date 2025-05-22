const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const fs = require('fs');
const path = require('path');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const PORT = 29888;
const IMATGES_DIR = path.join(__dirname, 'Imatges');

app.use(express.static('public'));

app.use('/Imatges', express.static(IMATGES_DIR));

let imatges = [];
try {
  imatges = fs.readdirSync(IMATGES_DIR)
    .filter(f => /\.(jpe?g|png|gif)$/i.test(f));
  console.log(`Imatges carregades: ${imatges.join(', ')}`);
} catch (err) {
  console.error('No s’ha pogut llegir el directori d’imatges:', err);
}

io.on('connection', (socket) => {
  console.log('Nou client connectat');

  socket.on('missatge', (data) => {
    console.log('Rebut:', data);

    if (data === 'encendre') {
      io.emit('resposta', '1');
    } 
    else if (data === 'apagar') {
      io.emit('resposta', '0');
    } 
    else if (data === 'imatge') {
      if (imatges.length === 0) {
        io.emit('resposta', 'No hi ha imatges disponibles');
      } else {
        const idx = Math.floor(Math.random() * imatges.length);
        const nomFitxer = imatges[idx];
        const url = `/Imatges/${nomFitxer}`;
        io.emit('resposta', url);
        console.log(`Imatge enviada: ${nomFitxer}`);
      }
    }
    else {
      io.emit('resposta', 'Comanda no reconeguda');
    }
  });

  socket.on('disconnect', () => {
    console.log('Client desconnectat');
  });
});

server.listen(PORT, () => {
  console.log(`Servidor escoltant a http://localhost:${PORT}`);
});
