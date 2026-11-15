// EJEMPLO RESUELTO X03 · "Favoritos, notas y etiquetas personales"
// ------------------------------------------------------------
// Lee docs/E03.md. Guía para resolver el evaluable E03Etiquetas.kt.
// La idea clave: Mark guarda los datos PERSONALES del alumno (favorito,
// nota, etiquetas, valoración). Nunca son estadísticas oficiales.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X03Etiquetas con las funciones de este ejemplo.
object X03Etiquetas {
 // Cambia el favorito al valor contrario: si era favorito, deja de serlo (y al revés).
 // copy() crea una Mark nueva cambiando solo el campo indicado; el resto se conserva.
 fun favorite(mark: Mark): Mark {
  // El signo ! invierte el booleano: true pasa a false y false a true.
  return mark.copy(favorite = !mark.favorite)
 }
 // demo(): parte de una Mark vacía y muestra cómo queda tras marcar favorito.
 fun demo(): String = favorite(Mark()).toString()
 // Guarda una nota personal, quitando espacios sobrantes y con límite de tamaño.
 fun withNote(mark: Mark, text: String): Mark {
  // Una nota de más de 4000 caracteres no cabe en la base de datos: se rechaza.
  require(text.length<=4000) { "Nota demasiado larga" }
  // trim() quita espacios del principio y del final del texto.
  return mark.copy(note=text.trim())
 }
 // Une las dos piezas: actualiza la nota y marca favorito en un solo paso.
 fun sample(): Mark {
  // Crea una Mark de partida con una nota anterior, para ver que no se pierde.
  val original=Mark(false,"Observación anterior")
  // Primero cambia la nota y después invierte el favorito, todo sobre la misma ficha.
  return favorite(withNote(original,"Nueva observación"))
 }
}
