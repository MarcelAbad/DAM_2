// EVALUABLE E10 · "Colecciones" (resuelto)
package es.aula.futbol

object E10Colecciones {
 // Devuelve la lista previa más el nombre nuevo, ya limpio.
 fun add(names: List<String>, name: String): List<String> {
  val clean = name.trim()
  // De 1 a 40 caracteres.
  require(clean.length in 1..40) { "El nombre debe tener de 1 a 40 caracteres" }
  // No puede ser equivalente a otro (ignorando tildes y mayúsculas).
  require(names.none { TextKey.of(it) == TextKey.of(clean) }) { "Ya existe una colección con ese nombre" }
  return names + clean
 }
 fun demo(): String = add(listOf("Promesas"), "Mi once").joinToString()
}
