// EJEMPLO RESUELTO X11 · "Copias en JSON"
// ------------------------------------------------------------
// Lee docs/E11.md. Guía para resolver el evaluable E11Copia.kt.
// La idea clave: una copia de seguridad es TEXTO en formato JSON.
// Se puede exportar (convertir a texto), importar (volver a leer) y validar.
// Si el texto está corrupto, debe fallar con un error claro, no romper nada.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X11Copia con las funciones de este ejemplo.
object X11Copia {
 // Convierte un ID y una nota a un pequeño JSON: {"id":1,"note":"..."}.
 fun noteJson(id: Int, note: String): String {
  // JSONObject construye el JSON campo a campo y toString() lo serializa.
  return org.json.JSONObject().put("id",id).put("note",note).toString()
 }
 // demo(): crea el JSON de una nota de ejemplo.
 fun demo(): String = noteJson(1,"Observación")
 // Lee un JSON y extrae el ID y la nota; si el texto no es JSON, lanza error.
 fun readNote(text: String): Pair<Int,String> {
  // JSONObject(text) analiza el texto; si está corrupto, lanza excepción controlada.
  val root=org.json.JSONObject(text)
  // Devuelve el par (id, nota) leído del JSON.
  return root.getInt("id") to root.getString("note")
 }
 // Comprueba el "viaje completo": crear un JSON, leerlo y recuperar lo mismo.
 fun roundTrip(): Boolean {
  // Nota de prueba con una ñ: los caracteres especiales también deben sobrevivir.
  val original="Nota con ñ"
  // Compara lo que sale con lo que entró: deben ser idénticos.
  return readNote(noteJson(1,original)).second==original
 }
}
