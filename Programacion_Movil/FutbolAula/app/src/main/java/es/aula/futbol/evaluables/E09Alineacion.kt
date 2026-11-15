// EVALUABLE E09 · "Alineación 4-3-3" (resuelto)
package es.aula.futbol

object E09Alineacion {
 // true solo si hay 11 jugadores distintos con reparto 1 portero, 4 defensas, 3 centrocampistas, 3 delanteros.
 fun validate(rows: List<Player>): Boolean {
  if (rows.size != 11) return false
  // Once IDs distintos: nadie repetido.
  if (rows.map { it.id }.distinct().size != 11) return false
  // Contamos cuántos hay de cada posición.
  val count = rows.groupingBy { it.position }.eachCount()
  return count["Portero"] == 1 && count["Defensa"] == 4 &&
   count["Centrocampista"] == 3 && count["Delantero"] == 3
 }
 fun demo(): String = validate(Seed.players.take(11)).toString()
}
