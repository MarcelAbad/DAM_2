// EJEMPLO RESUELTO X06 · "Grises y contraste"
// ------------------------------------------------------------
// Lee docs/E06.md. Guía para resolver el evaluable E06Contraste.kt.
// La idea clave: cada píxel tiene 3 canales (rojo, verde, azul) de 0 a 255.
// Para gris se mezclan con pesos "luminosos"; para contraste se estira la escala.
// El píxel es matemática pura: por eso se puede probar sin móvil.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X06Contraste con las funciones de este ejemplo.
object X06Contraste {
 // Pesos estándar para convertir un color a gris según cómo ve el ojo humano.
 // El verde pesa más porque el ojo es más sensible a él.
 fun grayWeights(): List<Float> {
  return listOf(0.213f,0.715f,0.072f)
 }
 // demo(): muestra los pesos del gris.
 fun demo(): String = grayWeights().toString()
 // Convierte un píxel de color (rojo, verde, azul) a su tono de gris.
 fun grayPixel(red: Int,green: Int,blue: Int): Int {
  // Los tres canales deben estar en el rango válido 0..255: si no, se rechaza.
  require(listOf(red,green,blue).all { it in 0..255 })
  // Se leen los pesos una sola vez y se guardan.
  val w=grayWeights()
  // Mezcla: multiplica cada canal por su peso, suma y redondea al entero más cercano.
  // coerceIn(0,255) asegura que el resultado no se salga del rango válido.
  return (red*w[0]+green*w[1]+blue*w[2]).toInt().coerceIn(0,255)
 }
 // Explica con un caso concreto: un rojo puro se vuelve un gris medio-oscuro.
 fun describePixel(): String = "Rojo puro se aproxima a gris ${grayPixel(255,0,0)}. El procesamiento real está en MediaArt.gray."
}
