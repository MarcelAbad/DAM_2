// Configuracion de Next.js (BACKEND: no tocar).
module.exports = {                       // exportamos la configuracion
  async rewrites() {                     // "rewrites" = redirecciones internas de direcciones
    return [                             // lista de reglas
      { source: '/', destination: '/index.html' }, // al abrir localhost:3000/ se carga public/index.html
    ];                                   // fin de la lista
  },                                     // fin de rewrites
};                                       // fin de la configuracion
