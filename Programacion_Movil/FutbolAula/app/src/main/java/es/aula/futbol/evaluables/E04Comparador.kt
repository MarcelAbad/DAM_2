// EVALUABLE E04 · "Comparar fichas sin inventar datos" (resuelto)
package es.aula.futbol

object E04Comparador {
 // Una línea por jugador: nombre | club | posición. Sin ganador ni habilidades inventadas.
 fun compare(rows: List<Player>): String {
  // Se comparan entre dos y cuatro jugadores.
  require(rows.size in 2..4) { "Elige de dos a cuatro jugadores" }
  // No se puede comparar a alguien consigo mismo.
  require(rows.map { it.id }.distinct().size == rows.size) { "Hay jugadores repetidos" }
  return rows.joinToString("\n") { "${it.name} | ${it.club} | ${it.position}" }
 }
 fun demo(): String = compare(Seed.players.take(2))
}
