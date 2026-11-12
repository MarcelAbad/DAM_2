// Une todos los componentes en un unico fichero. Se ejecuta con: npm run empaquetar
const fs = require('fs');                                   // modulo para leer/escribir ficheros
const path = require('path');                               // modulo para rutas
const dir = path.join(__dirname, '..', 'public', 'componentes'); // carpeta de componentes
const files = fs.readdirSync(dir)                           // lista de ficheros de la carpeta
  .filter((f) => f.endsWith('.js') && f !== 'pruebas.js'); // solo .js y sin el de pruebas
const code = files                                          // por cada fichero...
  .map((f) => '/* ' + f + ' */\n' + fs.readFileSync(path.join(dir, f), 'utf8')) // ...su contenido con titulo
  .join('\n');                                              // ...y todo junto
const out = path.join(__dirname, '..', 'public', 'dist');   // carpeta de salida
fs.mkdirSync(out, { recursive: true });                     // la creamos si no existe
fs.writeFileSync(path.join(out, 'erp-componentes.js'), code); // escribimos el paquete
console.log('Paquete creado con', files.length, 'componente(s): public/dist/erp-componentes.js'); // aviso
