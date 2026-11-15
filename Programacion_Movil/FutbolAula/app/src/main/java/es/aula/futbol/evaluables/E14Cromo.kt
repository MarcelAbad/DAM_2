// EVALUABLE E14 · "Cromo compartido" (resuelto)
package es.aula.futbol

object E14Cromo {
 // Devuelve el título limpio si el título y el color son válidos.
 fun title(raw: String, color: Int): String {
  // Solo hay tres colores de fondo: 0, 1 y 2.
  require(color in 0..2) { "Color no válido" }
  val clean = raw.trim()
  // De 1 a 28 caracteres para que quepa en el cromo.
  require(clean.length in 1..28) { "El título debe tener de 1 a 28 caracteres" }
  return clean
 }
 fun demo(): String = title("Mi jugador favorito", 1)
}
