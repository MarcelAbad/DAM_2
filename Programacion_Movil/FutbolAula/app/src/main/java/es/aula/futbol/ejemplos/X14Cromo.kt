// EJEMPLO RESUELTO X14 · "Iniciales y textos que caben"
// ------------------------------------------------------------
// Lee docs/E14.md. Guía para resolver el evaluable E14Cromo.kt.
// La idea clave: un cromo es una imagen con un TÍTULO corto y unas
// iniciales grandes. Si el texto no cabe, hay que recortarlo con criterio.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X14Cromo con las funciones de este ejemplo.
object X14Cromo {
 // Calcula las iniciales del jugador: "Lamine Yamal" -> "LY".
 fun initials(player: Player): String {
  // split(" ") separa por espacios; take(2) coge como máximo dos palabras.
  // joinToString une las iniciales: la primera letra de cada palabra.
  return player.name.split(" ").take(2).joinToString("") { it.take(1) }
 }
 // demo(): muestra las iniciales del primer jugador del catálogo.
 fun demo(): String = initials(Seed.players.first())
 // Recorta un texto a un máximo de caracteres, limpiando espacios sobrantes.
 fun fitTitle(text: String,maxLength: Int): String {
  // Un máximo de 0 o negativo no tendría sentido: se rechaza.
  require(maxLength>0)
  // trim quita espacios de los extremos; take corta en el límite indicado.
  return text.trim().take(maxLength)
 }
 // Etiqueta combinada: iniciales grandes + nombre completo debajo.
 fun label(player: Player): String = "${initials(player)} · ${player.name}"
}
