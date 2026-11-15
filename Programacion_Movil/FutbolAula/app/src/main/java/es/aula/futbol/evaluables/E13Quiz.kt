// EVALUABLE E13 · "Cuestionario" (resuelto)
package es.aula.futbol

object E13Quiz {
 // Crea `count` preguntas de club, una por jugador distinto, con dos opciones.
 fun build(rows: List<Player>, count: Int): List<Question> {
  require(count > 0) { "La cantidad debe ser positiva" }
  require(rows.map { it.id }.distinct().size == rows.size) { "Hay IDs repetidos" }
  require(rows.size >= count) { "No hay jugadores suficientes" }
  val clubs = rows.map { it.club }.distinct()
  require(clubs.size >= 2) { "Hacen falta al menos dos clubes" }
  return rows.take(count).mapIndexed { i, p ->
   // Club incorrecto: cualquier club distinto al del jugador (se reparte según la posición).
   val others = clubs.filter { it != p.club }
   val wrong = others[i % others.size]
   // Alternamos el orden para que la correcta no esté siempre en primer lugar.
   val options = if (i % 2 == 0) listOf(p.club, wrong) else listOf(wrong, p.club)
   Question(p.id, "¿Dónde juega ${p.name}?", options, p.club)
  }
 }
 fun demo(): String = build(Seed.players, 5).joinToString("\n") { it.text }
}
