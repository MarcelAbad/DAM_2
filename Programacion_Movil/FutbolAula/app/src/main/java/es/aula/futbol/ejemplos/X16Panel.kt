// EJEMPLO RESUELTO X16 · "Recuentos para el panel personal"
// ------------------------------------------------------------
// Lee docs/E16.md. Guía para resolver el evaluable E16Panel.kt.
// La idea clave: el panel muestra TUS recuentos (cuántos favoritos
// por club, etc.). Son datos personales, no estadísticas deportivas.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X16Panel con las funciones de este ejemplo.
object X16Panel {
 // Cuenta cuántos jugadores hay en total.
 fun total(rows: List<Player>): Int {
  return rows.size
 }
 // demo(): cuenta los 16 del catálogo inicial.
 fun demo(): String = total(Seed.players).toString()
 // Devuelve solo los jugadores marcados como favoritos.
 fun favorites(rows: List<Player>,marks: Map<Int,Mark>): List<Player> = rows.filter { marks[it.id]?.favorite==true }
 // Texto resumen para el panel: totales y favoritos.
 fun summary(rows: List<Player>,marks: Map<Int,Mark>): String {
  // Se calculan primero los favoritos para poder contarlos.
  val selected=favorites(rows,marks)
  // Se deja claro que son recuentos personales, no datos oficiales.
  return "Total: ${total(rows)}. Favoritos: ${selected.size}. Son recuentos personales."
 }
}
