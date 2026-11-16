// EJEMPLO RESUELTO X09 · "Tríos sin repetir (mini-alineación)"
// ------------------------------------------------------------
// Lee docs/E09.md. Guía para resolver el evaluable E09Alineacion.kt.
// La idea clave: una alineación válida exige jugadores DISTINTOS y el
// número exacto por posición. Aquí se practica con un trío; el once es igual.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X09Alineacion con las funciones de este ejemplo.
object X09Alineacion {
 // ¿Es un trío válido? Deben ser exactamente 3 y todos distintos.
 fun validTrio(rows: List<Player>): Boolean {
  // size==3 asegura el número exacto; distinct() quita repetidos y compara tamaños.
  return rows.size == 3 && rows.map { it.id }.distinct().size == 3
 }
 // demo(): prueba con los tres primeros del catálogo.
 fun demo(): String = validTrio(Seed.players.take(3)).toString()
 // Añade un jugador al trío respetando las reglas.
 fun addToTrio(rows: List<Player>,player: Player): List<Player> {
  // Si ya hay 3, el trío está completo: no cabe nadie más.
  require(rows.size<3) { "Trío completo" }
  // Un jugador que ya está dentro no se puede repetir.
  require(rows.none { it.id==player.id }) { "Repetido" }
  // Devuelve el trío con el jugador añadido.
  return rows+player
 }
 // Guarda el trío como lista de IDs: es el "borrador" que se puede retomar luego.
 fun saveDraft(rows: List<Player>): List<Int> = rows.map { it.id }
}
