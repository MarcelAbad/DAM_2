// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa java.text.Normalizer para poder utilizar su API.
import java.text.Normalizer
// Importa org.json.JSONArray para poder utilizar su API.
import org.json.JSONArray
// Importa org.json.JSONObject para poder utilizar su API.
import org.json.JSONObject

// Declara el modelo o componente que agrupa estas responsabilidades.
data class Player(val id: Int, val name: String, val club: String, val position: String, val source: String)
// Declara el modelo o componente que agrupa estas responsabilidades.
data class Mark(val favorite: Boolean = false, val note: String = "", val tags: List<String> = emptyList(), val rating: Int = 0)
// Declara el modelo o componente que agrupa estas responsabilidades.
data class Question(val playerId: Int, val text: String, val options: List<String>, val answer: String)
// Declara el modelo o componente que agrupa estas responsabilidades.
data class CacheResult(val rows: List<Player>, val stale: Boolean, val message: String)
// Declara el modelo o componente que agrupa estas responsabilidades.
object TextKey {
 // Define of; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 // Normaliza el texto: quita espacios, separa los acentos (NFD), los elimina y pasa a minúsculas.
 fun of(value: String): String = Normalizer.normalize(value.trim(), Normalizer.Form.NFD).replace("\\p{M}+".toRegex(), "").lowercase(java.util.Locale.ROOT)
// Cierra el bloque anterior y devuelve el control a su contexto.
}
// Declara el modelo o componente que agrupa estas responsabilidades.
object Seed {
 // Paso del flujo: const val madrid = "https://www.realmadrid.com/en-US/football/first-team/players"
 const val madrid = "https://www.realmadrid.com/en-US/football/first-team/players"
 // Paso del flujo: const val barca = "https://www.fcbarcelona.com/en/"
 const val barca = "https://www.fcbarcelona.com/en/"
 // Paso del flujo: const val athletic = "https://www.athletic-club.eus/en/players/"
 const val athletic = "https://www.athletic-club.eus/en/players/"
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 val players = listOf(
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(1,"Thibaut Courtois","Real Madrid","Portero",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(2,"Andriy Lunin","Real Madrid","Portero",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(3,"Éder Militão","Real Madrid","Defensa",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(4,"Dean Huijsen","Real Madrid","Defensa",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(5,"Jude Bellingham","Real Madrid","Centrocampista",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(6,"Vinícius Júnior","Real Madrid","Delantero",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(7,"Kylian Mbappé","Real Madrid","Delantero",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(8,"Rodrygo","Real Madrid","Delantero",madrid),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(9,"Joan García","FC Barcelona","Portero",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(10,"Pau Cubarsí","FC Barcelona","Defensa",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(11,"Pedri","FC Barcelona","Centrocampista",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(12,"Frenkie de Jong","FC Barcelona","Centrocampista",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(13,"Fermín López","FC Barcelona","Centrocampista",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(14,"Dani Olmo","FC Barcelona","Centrocampista",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(15,"Lamine Yamal","FC Barcelona","Delantero",barca),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(16,"Raphinha","FC Barcelona","Delantero",barca)
 // Paso del flujo: )
 )
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 val extra = listOf(
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(101,"Nico Williams","Athletic Club","Delantero",athletic+"nicholas-williams-arthuer/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(102,"Iñaki Williams","Athletic Club","Delantero",athletic+"inaki-williams-arthuer/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(103,"Unai Simón","Athletic Club","Portero",athletic+"unai-simon-mendibil/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(104,"Aymeric Laporte","Athletic Club","Defensa","https://www.athletic-club.eus/en/news/2026/09/18/unai-simon-laporte-and-nico-williams-in-the-spain-squad/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(105,"Dani Vivian","Athletic Club","Defensa",athletic+"daniel-vivian-moreno/")
 // Paso del flujo: )
 )
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 val atleti = listOf(
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(201,"Jan Oblak","Atlético de Madrid","Portero","https://www.atleticodemadrid.com/jugadores-primer-equipo/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(202,"Koke","Atlético de Madrid","Centrocampista","https://www.atleticodemadrid.com/jugadores-primer-equipo/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(203,"Antoine Griezmann","Atlético de Madrid","Delantero","https://www.atleticodemadrid.com/jugadores-primer-equipo/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(204,"Pablo Barrios","Atlético de Madrid","Centrocampista","https://www.atleticodemadrid.com/jugadores-primer-equipo/"),
  // Añade una ficha del lote de referencia con identificador, nombre, club, posición y fuente.
  Player(205,"Marcos Llorente","Atlético de Madrid","Centrocampista","https://www.atleticodemadrid.com/jugadores-primer-equipo/")
 // Paso del flujo: )
 )
// Cierra el bloque anterior y devuelve el control a su contexto.
}
// Declara el modelo o componente que agrupa estas responsabilidades.
object Wire {
 // Define encode; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun encode(rows: Map<Int, Mark>): String {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val array = JSONArray()
  // Paso del flujo: rows.forEach { (id, mark) -> array.put(JSONObject().put("id",id).put("favorite",mark.favorite).put("note",mark.note)) }
  rows.forEach { (id, mark) -> array.put(JSONObject().put("id",id).put("favorite",mark.favorite).put("note",mark.note)) }
  // Devuelve este resultado al código que invocó la función.
  return JSONObject().put("version",1).put("marks",array).toString()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define decode; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun decode(text: String, known: Set<Int>): Map<Int, Mark> {
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(text.length <= 200000) { "Copia demasiado grande" }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val root = JSONObject(text)
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(root.getInt("version") == 1) { "Versión no admitida" }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val array = root.getJSONArray("marks")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val result = linkedMapOf<Int, Mark>()
  // Paso del flujo: for (index in 0 until array.length()) {
  for (index in 0 until array.length()) {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val item = array.getJSONObject(index)
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val id = item.getInt("id")
   // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
   require(!result.containsKey(id)) { "Identificador duplicado" }
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val favorite = item.get("favorite")
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val note = item.get("note")
   // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
   require(favorite is Boolean && note is String) { "Tipos incorrectos" }
   // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
   require(note.length <= 4000) { "Nota demasiado larga" }
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if (id in known) result[id] = Mark(favorite, note)
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Devuelve este resultado al código que invocó la función.
  return result
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
