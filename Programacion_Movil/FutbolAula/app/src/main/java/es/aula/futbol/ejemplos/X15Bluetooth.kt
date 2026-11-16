// EJEMPLO RESUELTO X15 · "Mensajes de texto por Bluetooth"
// ------------------------------------------------------------
// Lee docs/E15.md. Guía para resolver el evaluable E15Bluetooth.kt.
// La idea clave: los datos viajan como TEXTO con formato acordado
// (aquí: "id;nombre;club"). Lo importante es poder crear el mensaje
// y VALIDAR lo recibido antes de aceptarlo.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X15Bluetooth con las funciones de este ejemplo.
object X15Bluetooth {
 // Convierte un jugador en una línea de texto lista para enviar.
 fun line(player: Player): String {
  // El formato es fijo: tres campos separados por ; y salto de línea al final.
  return "${player.id};${player.name};${player.club}\n"
 }
 // demo(): muestra la línea que se enviaría del primer jugador.
 fun demo(): String = line(Seed.players.first())
 // Comprueba que una línea recibida tiene el formato correcto antes de usarla.
 fun validateLine(text: String): Boolean {
  // Se quita el salto de línea y se separa por ;: deben salir 3 campos.
  val fields=text.trim().split(';')
  // El primero debe ser un número; los otros dos, textos no vacíos.
  return fields.size==3 && fields[0].toIntOrNull()!=null && fields[1].isNotBlank() && fields[2].isNotBlank()
 }
 // Viaje completo: crear el mensaje y validarlo como haría el receptor.
 fun roundTrip(player: Player): Boolean = validateLine(line(player))
}
