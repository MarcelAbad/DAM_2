// EVALUABLE E16 · "Panel personal" (resuelto)
package es.aula.futbol

object E16Panel {
 // Cuenta jugadores por club, sin repetir IDs, ordenado por nombre de club.
 fun counts(rows: List<Player>): Map<String, Int> =
  rows.distinctBy { it.id }
   .groupingBy { it.club }
   .eachCount()
   .toSortedMap()
 fun demo(): String = counts(Seed.players).toString()
}
