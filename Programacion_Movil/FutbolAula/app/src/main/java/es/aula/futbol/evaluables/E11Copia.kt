// EVALUABLE E11 · "Copia de seguridad" (resuelto)
package es.aula.futbol

object E11Copia {
 // Restaura favoritos y notas de la copia sin borrar lo que no aparece en ella.
 fun restore(current: Map<Int, Mark>, text: String, known: Set<Int>): Map<Int, Mark> {
  // Wire.decode valida TODO (JSON, versión, tipos, IDs repetidos) antes de devolver nada.
  // Si el texto no es JSON lanza excepción y no se toca nada.
  val incoming = Wire.decode(text, known)
  // Partimos de lo que ya había (se conserva lo ausente de la copia)...
  val result = LinkedHashMap<Int, Mark>(current)
  // ...y la copia sobrescribe favorito y nota, manteniendo etiquetas y valoración existentes.
  incoming.forEach { (id, mark) ->
   val old = current[id]
   result[id] = old?.copy(favorite = mark.favorite, note = mark.note) ?: mark
  }
  return result
 }
 fun demo(): String = restore(emptyMap(), Wire.encode(mapOf(1 to Mark(true, "Nota"))), setOf(1)).toString()
}
