// EVALUABLE E01 · "Más equipos reales" (resuelto)
package es.aula.futbol

object E01Catalogo {
 // Posiciones que acepta la app (las mismas que usan las fichas del catálogo).
 private val positions = setOf("Portero", "Defensa", "Centrocampista", "Delantero")

 // Añade un lote nuevo (de UN solo club) al catálogo actual, validando todo antes.
 fun addTeam(current: List<Player>, incoming: List<Player>): List<Player> {
  // 1. Un lote vacío no tiene sentido.
  require(incoming.isNotEmpty()) { "El lote está vacío" }
  // 2. Todos los jugadores deben ser del mismo club.
  require(incoming.map { it.club }.distinct().size == 1) { "El lote mezcla varios clubes" }
  // 3. Cada ficha necesita nombre, club, posición permitida y fuente HTTPS.
  incoming.forEach {
   require(it.name.isNotBlank() && it.club.isNotBlank()) { "Ficha sin nombre o club" }
   require(it.position in positions) { "Posición no admitida: ${it.position}" }
   require(it.source.startsWith("https://")) { "La fuente debe ser HTTPS" }
  }
  // 4. IDs repetidos: ni dentro del lote ni contra el catálogo actual.
  val ids = incoming.map { it.id }
  require(ids.distinct().size == ids.size) { "IDs repetidos dentro del lote" }
  require(current.none { it.id in ids }) { "Algún ID ya existe en el catálogo" }
  // 5. Mismo jugador (club + nombre sin tildes/mayúsculas) con otro ID = duplicado.
  val existing = current.map { TextKey.of(it.club) + "|" + TextKey.of(it.name) }.toSet()
  val news = incoming.map { TextKey.of(it.club) + "|" + TextKey.of(it.name) }
  require(news.distinct().size == news.size) { "Jugador repetido dentro del lote" }
  require(news.none { it in existing }) { "El jugador ya existe en ese club" }
  // Todo correcto: catálogo actual + lote.
  return current + incoming
 }
 // demo() lo usa Academia: devuelve cuántos jugadores hay tras añadir el lote del Athletic.
 fun demo(): String = addTeam(Seed.players, Seed.extra).size.toString()
}
