// EVALUABLE E03 · "Etiquetas y valoración personal" (resuelto)
package es.aula.futbol

object E03Etiquetas {
 // Devuelve una copia de la marca con etiquetas limpias y la valoración; conserva favorito y nota.
 fun mark(original: Mark, tags: List<String>, rating: Int): Mark {
  // La valoración personal solo admite de 0 a 5.
  require(rating in 0..5) { "La valoración debe estar entre 0 y 5" }
  // Recortamos espacios y quitamos las vacías.
  val clean = tags.map { it.trim() }.filter { it.isNotEmpty() }
  // Ninguna etiqueta puede pasar de 24 caracteres.
  require(clean.all { it.length <= 24 }) { "Etiqueta de más de 24 caracteres" }
  // Quitamos repetidas ignorando tildes y mayúsculas (nos quedamos con la primera).
  val unique = clean.distinctBy { TextKey.of(it) }
  // Máximo cinco etiquetas distintas.
  require(unique.size <= 5) { "Máximo cinco etiquetas" }
  // copy() cambia solo lo indicado: favorito y nota se mantienen.
  return original.copy(tags = unique, rating = rating)
 }
 fun demo(): String = mark(Mark(), listOf("Técnica", " técnica "), 4).toString()
}
