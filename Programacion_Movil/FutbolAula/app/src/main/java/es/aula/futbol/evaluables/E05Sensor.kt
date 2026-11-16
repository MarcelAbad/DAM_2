// EVALUABLE E05 · "Sorpresa respetando filtros" (resuelto)
package es.aula.futbol

object E05Sensor {
 // Elige un jugador SOLO de la lista recibida usando un índice circular.
 fun choose(candidates: List<Player>, index: Int): Player? {
  // Sin candidatos no hay sorpresa: null (no es un error).
  if (candidates.isEmpty()) return null
  // floorMod da siempre un resultado entre 0 y size-1, incluso con índices negativos.
  return candidates[Math.floorMod(index, candidates.size)]
 }
 fun demo(): String = choose(Seed.players.filter { it.club == "FC Barcelona" }, 0)?.name ?: "Sin candidatos"
}
