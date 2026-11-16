// EJEMPLO RESUELTO X04 · "Comparar dos fichas"
// ------------------------------------------------------------
// Lee docs/E04.md. Guía para resolver el evaluable E04Comparador.kt.
// La idea clave: comparar SOLO datos que existen (club, posición).
// Nunca inventes puntuaciones ni un "ganador".
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X04Comparador con las funciones de este ejemplo.
object X04Comparador {
 // Responde si dos jugadores juegan en el mismo club.
 fun sameClub(a: Player, b: Player): Boolean {
  // Comparar textos con == es exacto: "Real Madrid" no es "Real Madrid CF".
  return a.club == b.club
 }
 // demo(): compara los dos primeros jugadores del catálogo.
 fun demo(): String = sameClub(Seed.players[0],Seed.players[1]).toString()
 // Construye la "línea de identidad" de un jugador para mostrarla en pantalla.
 fun identity(player: Player): String = "${player.name} | ${player.club} | ${player.position}"
 // Compara dos jugadores y devuelve un texto con la comparación completa.
 fun describePair(a: Player,b: Player): String {
  // No tiene sentido comparar un jugador consigo mismo: se rechaza.
  require(a.id!=b.id) { "Elige dos jugadores distintos" }
  // Devuelve las dos identidades y el resultado de comparar el club.
  return identity(a)+"\n"+identity(b)+"\nMismo club: "+sameClub(a,b)
 }
}
