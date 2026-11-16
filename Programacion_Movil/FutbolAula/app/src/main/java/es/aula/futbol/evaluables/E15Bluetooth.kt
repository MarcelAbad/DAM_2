// EVALUABLE E15 · "Intercambio por Bluetooth" (resuelto)
package es.aula.futbol

import org.json.JSONObject

object E15Bluetooth {
 // Decodifica el mensaje de la colección y devuelve los IDs en el orden recibido.
 fun decode(text: String, known: Set<Int>): List<Int> {
  // Tamaño máximo 8192 bytes en UTF-8.
  require(text.toByteArray(Charsets.UTF_8).size <= 8192) { "Mensaje demasiado grande" }
  // Si no es JSON o faltan campos, org.json lanza JSONException (rechazo controlado).
  val root = JSONObject(text)
  require(root.getInt("version") == 1) { "Versión no admitida" }
  val array = root.getJSONArray("ids")
  require(array.length() > 0) { "La lista de IDs está vacía" }
  val ids = (0 until array.length()).map {
   val value = array.get(it)
   require(value is Int) { "Los IDs deben ser enteros" }
   value as Int
  }
  require(ids.distinct().size == ids.size) { "IDs repetidos" }
  require(ids.all { it in known }) { "Hay IDs desconocidos" }
  return ids
 }
 fun demo(): String = decode("{\"version\":1,\"ids\":[1,2]}", setOf(1, 2)).toString()
}
