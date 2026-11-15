// EVALUABLE E06 · "Filtro de contraste reversible" (resuelto)
package es.aula.futbol

object E06Contraste {
 // Devuelve el par (escala, desplazamiento) para la matriz de color.
 fun parameters(value: Float): Pair<Float, Float> {
  // NaN no es comparable, así que se comprueba aparte; el resto debe estar en 0..3.
  require(!value.isNaN() && value >= 0f && value <= 3f) { "El contraste debe estar entre 0 y 3" }
  // escala = value; desplazamiento = 128 * (1 - value) para mantener el gris medio.
  return Pair(value, 128f * (1f - value))
 }
 fun demo(): String = parameters(1.4f).toString()
}
