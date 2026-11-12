// Errores visibles: no obliga al principiante a buscar la consola.
function mostrarError(texto){
 let aviso=document.getElementById('error-global');
 if(!aviso){aviso=document.createElement('p');aviso.id='error-global';aviso.setAttribute('role','alert');document.body.appendChild(aviso);}
 aviso.textContent='Aviso: '+texto+' Revisa permisos, navegador y conexion.';
}
window.addEventListener('error',e=>mostrarError(e.message));
window.addEventListener('unhandledrejection',e=>mostrarError(e.reason?.message||String(e.reason)));
