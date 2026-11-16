// EVALUABLE E08 · "Playlist de audios" (resuelto)
package es.aula.futbol

object E08Audio {
 // Índice del audio anterior (-1) o siguiente (1), con recorrido circular.
 fun next(current: Int, size: Int, direction: Int): Int? {
  // Solo se admite -1 o 1.
  require(direction == -1 || direction == 1) { "Dirección no válida" }
  // Sin audios no hay a dónde ir: null.
  if (size <= 0) return null
  // El índice actual debe existir en la lista.
  require(current in 0 until size) { "Índice fuera de la lista" }
  // floorMod hace que después del último vuelva el primero (y al revés).
  return Math.floorMod(current + direction, size)
 }
 fun demo(): String = next(0, 3, -1).toString()
}
