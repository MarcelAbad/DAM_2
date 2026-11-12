// Funciones puras: reciben datos y devuelven resultados, sin tocar el navegador.
(function (g) {
  const estados = ['pending', 'paid', 'shipped', 'cancelled']; // Orden del ciclo.
  const normalizar = s => String(s).toLowerCase().trim().replace(/[.!?]+$/, ''); // Limpia comandos.
  const siguiente = (i, paso, n) => n > 0 ? ((i + paso) % n + n) % n : 0; // Indice circular seguro.
  const filtrar = (lista, frase) => { // Conserva siempre la lista original.
    const f = normalizar(frase);
    if (f === 'stock bajo') return lista.filter(p => p.stock < 5);
    if (f.startsWith('buscar ')) return lista.filter(p => p.name.toLowerCase().includes(f.slice(7)));
    return lista;
  };
  const movimiento = (foto, anterior, ancho) => { // Compara brillo, no identifica personas.
    let izq = 0, der = 0;
    if (!anterior) return { izq, der };
    for (let i = 0; i < foto.length; i += 4) {
      const diferencia = Math.abs(foto[i]+foto[i+1]+foto[i+2]-anterior[i]-anterior[i+1]-anterior[i+2]);
      if (diferencia > 90) { if ((i/4)%ancho < ancho/2) izq++; else der++; }
    }
    return { izq, der };
  };
  const pinza = m => !!m && Math.hypot(m[8].x-m[4].x, m[8].y-m[4].y)<0.05; // Distancia normalizada.
  const api = {estados, normalizar, siguiente, filtrar, movimiento, pinza};
  if (typeof module !== 'undefined') module.exports = api; // Pruebas Node.
  else g.Logica = api; // Navegador.
})(globalThis);
