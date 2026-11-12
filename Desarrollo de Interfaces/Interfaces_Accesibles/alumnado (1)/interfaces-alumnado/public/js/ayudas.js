// Infraestructura frontend proporcionada: no es necesario modificarla.
window.Ayudas = {
  voz(boton, accion, mensaje) { // Conecta un boton a una funcion que recibe la frase.
    const R = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!R) { boton.disabled = true; mensaje.textContent = 'Reconocimiento no disponible: prueba Chrome o Edge.'; return; }
    const oido = new R(); // Una frase por pulsacion.
    oido.lang = 'es-ES';
    oido.onresult = e => Promise.resolve(accion(e.results[0][0].transcript)).catch(e => mensaje.textContent = e.message);
    oido.onerror = e => mensaje.textContent = 'Voz: ' + e.error + '. Revisa permisos y conexion.';
    oido.onend = () => boton.disabled = false;
    boton.onclick = () => { try { boton.disabled = true; oido.start(); } catch(e) { boton.disabled = false; mensaje.textContent = e.message; } };
    window.addEventListener('pagehide', () => oido.abort());
  },
  async pedir(url, opciones) { // Comprueba errores HTTP antes de usar los datos.
    const r = await fetch(url, opciones);
    if (!r.ok) throw new Error('Error HTTP ' + r.status);
    return r.json();
  },
  escribir(url, metodo, datos) { // Envios JSON.
    return this.pedir(url, {method: metodo, headers: {'Content-Type':'application/json'}, body: JSON.stringify(datos)});
  },
  escapar(s) { // Los textos de usuarios no deben convertirse en etiquetas HTML.
    return String(s ?? '').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  }
};
