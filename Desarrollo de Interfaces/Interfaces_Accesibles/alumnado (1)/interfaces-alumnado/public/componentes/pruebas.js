// Mini sistema de pruebas unitarias (sin librerias).
export function test(nombre, fn, salida) {          // test: nombre, funcion con la prueba, lista donde escribir
  const li = document.createElement('li');          // creamos una linea de lista
  try {                                             // intentamos ejecutar la prueba
    fn();                                           // si no lanza error, la prueba pasa
    li.textContent = 'OK - ' + nombre;              // texto de exito
    li.className = 'ok';                            // clase CSS verde
  } catch (e) {                                     // si hubo error, la prueba falla
    li.textContent = 'FALLA - ' + nombre + ' -> ' + e.message; // texto con el motivo
    li.className = 'mal';                           // clase CSS roja
  }
  salida.appendChild(li);                           // anadimos la linea a la pagina
}
export function igual(real, esperado) {             // compara dos valores
  if (real !== esperado) {                          // si son distintos...
    throw new Error('esperaba ' + esperado + ' pero salio ' + real); // ...lanzamos error
  }
}
