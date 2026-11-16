// EVALUABLE E02 · "Buscar, filtrar y ordenar" (resuelto)
package es.aula.futbol

object E02Filtros {
 // Combina con AND: texto, club, posición y favoritos. Luego ordena por nombre.
 fun filter(rows: List<Player>, query: String, club: String, position: String, favorites: Set<Int>, onlyFavorites: Boolean, descending: Boolean): List<Player> {
  // Normalizamos el texto buscado (sin tildes, sin mayúsculas, sin espacios sobrantes).
  val key = TextKey.of(query)
  val found = rows.filter { p ->
   TextKey.of(p.name).contains(key) &&
   (club.isEmpty() || p.club == club) &&
   (position.isEmpty() || p.position == position) &&
   (!onlyFavorites || p.id in favorites)
  }
  // Orden ascendente por nombre normalizado; el descendente es el mismo orden invertido.
  val ascending = found.sortedBy { TextKey.of(it.name) }
  return if (descending) ascending.reversed() else ascending
 }
 fun demo(): String = filter(Seed.players, "", "FC Barcelona", "Delantero", emptySet(), false, false).joinToString { it.name }
}
