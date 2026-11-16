// EJEMPLO RESUELTO X02 · "Buscar y filtrar"
// ------------------------------------------------------------
// Lee docs/E02.md. Guía para resolver el evaluable E02Filtros.kt.
// La idea clave: comparar textos "normalizados" (sin acentos ni mayúsculas)
// para que "mbappe" encuentre a "Mbappé". Eso lo hace TextKey.of.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X02Filtros con las funciones de búsqueda de este ejemplo.
object X02Filtros {
 // Busca jugadores cuyo nombre contiene el texto pedido (ignora acentos y mayúsculas).
 fun search(rows: List<Player>, query: String): List<Player> {
  // filter recorre la lista y se queda solo con los que cumplen la condición.
  // TextKey.of normaliza AMBOS lados: el nombre del jugador y lo que escribió el usuario.
  return rows.filter { TextKey.of(it.name).contains(TextKey.of(query)) }
 }
 // demo(): prueba la búsqueda con "mbappe" y devuelve los nombres encontrados.
 fun demo(): String = search(Seed.players,"mbappe").joinToString { it.name }
 // Filtra por club exacto: solo quedan los jugadores de ese club.
 fun byClub(rows: List<Player>, club: String): List<Player> = rows.filter { it.club == club }
 // Devuelve un mensaje distinto si la búsqueda no encontró a nadie.
 fun emptyMessage(rows: List<Player>): String = if(rows.isEmpty()) "No hay coincidencias" else "${rows.size} coincidencias"
 // Busca y prepara un texto con el resultado completo para mostrar en pantalla.
 fun searchAndDescribe(rows: List<Player>, query: String): String {
  // Ejecuta la búsqueda y guarda el resultado.
  val found=search(rows,query)
  // Devuelve el resumen y debajo la lista de nombres, uno por línea.
  return emptyMessage(found)+"\n"+found.joinToString("\n") { it.name }
 }
}
