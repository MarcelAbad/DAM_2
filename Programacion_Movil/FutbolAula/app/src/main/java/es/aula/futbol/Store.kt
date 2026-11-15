// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa android.content.Context para poder utilizar su API.
import android.content.Context
// Importa android.content.ContentValues para poder utilizar su API.
import android.content.ContentValues
// Importa android.database.sqlite.SQLiteDatabase para poder utilizar su API.
import android.database.sqlite.SQLiteDatabase
// Importa android.database.sqlite.SQLiteOpenHelper para poder utilizar su API.
import android.database.sqlite.SQLiteOpenHelper
// Importa org.json.JSONArray para poder utilizar su API.
import org.json.JSONArray
// Importa org.json.JSONObject para poder utilizar su API.
import org.json.JSONObject
// Declara el modelo o componente que agrupa estas responsabilidades.
class Store(context: Context, databaseName: String = "futbol.db") : SQLiteOpenHelper(context,databaseName,null,1) {
 // Implementa la respuesta al evento onCreate.
 override fun onCreate(db: SQLiteDatabase) {
  // Paso del flujo: db.execSQL("CREATE TABLE marks(id INTEGER PRIMARY KEY,favorite INTEGER NOT NULL,note TEXT NOT NULL,tags TEXT NOT NULL,rating INTEGER NOT NULL)")
  db.execSQL("CREATE TABLE marks(id INTEGER PRIMARY KEY,favorite INTEGER NOT NULL,note TEXT NOT NULL,tags TEXT NOT NULL,rating INTEGER NOT NULL)")
  // Paso del flujo: db.execSQL("CREATE TABLE settings(key TEXT PRIMARY KEY,value TEXT NOT NULL)")
  db.execSQL("CREATE TABLE settings(key TEXT PRIMARY KEY,value TEXT NOT NULL)")
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento onUpgrade.
 override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { error("Diseña una migración") }
 // Define mark; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun mark(id: Int): Mark {
  // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
  readableDatabase.rawQuery("SELECT favorite,note,tags,rating FROM marks WHERE id=?",arrayOf(id.toString())).use { c ->
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if (!c.moveToFirst()) return Mark()
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val tags = JSONArray(c.getString(2))
   // Devuelve este resultado al código que invocó la función.
   return Mark(c.getInt(0)==1,c.getString(1),(0 until tags.length()).map { tags.getString(it) },c.getInt(3))
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define save; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun save(id: Int, mark: Mark) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val v = ContentValues().apply { put("id",id); put("favorite",if(mark.favorite) 1 else 0); put("note",mark.note); put("tags",JSONArray(mark.tags).toString()); put("rating",mark.rating) }
  // Paso del flujo: writableDatabase.insertWithOnConflict("marks",null,v,SQLiteDatabase.CONFLICT_REPLACE)
  writableDatabase.insertWithOnConflict("marks",null,v,SQLiteDatabase.CONFLICT_REPLACE)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define delete; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun delete(id: Int) { writableDatabase.delete("marks","id=?",arrayOf(id.toString())) }
 // Define get; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun get(key: String, fallback: String = ""): String {
  // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
  readableDatabase.rawQuery("SELECT value FROM settings WHERE key=?",arrayOf(key)).use { return if(it.moveToFirst()) it.getString(0) else fallback }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define put; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun put(key: String, value: String) {
  // Paso del flujo: writableDatabase.insertWithOnConflict("settings",null,ContentValues().apply { put("key",key); put("value",value) },SQLiteDatabase.CONFLICT_REPLACE)
  writableDatabase.insertWithOnConflict("settings",null,ContentValues().apply { put("key",key); put("value",value) },SQLiteDatabase.CONFLICT_REPLACE)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define marks; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun marks(ids: List<Int>): Map<Int,Mark> = ids.associateWith { mark(it) }
 // Define restore; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun restore(data: Map<Int,Mark>) {
  // Paso del flujo: writableDatabase.beginTransaction()
  writableDatabase.beginTransaction()
  // Paso del flujo: try { data.forEach { (id,m) -> save(id,m) }; writableDatabase.setTransactionSuccessful() } finally { writableDatabase.endTransaction() }
  try { data.forEach { (id,m) -> save(id,m) }; writableDatabase.setTransactionSuccessful() } finally { writableDatabase.endTransaction() }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define players; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun players(): List<Player> {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val array = JSONArray(get("players","[]"))
  // Devuelve este resultado al código que invocó la función.
  return if(array.length()==0) Seed.players else (0 until array.length()).map { i -> val p=array.getJSONObject(i); Player(p.getInt("id"),p.getString("name"),p.getString("club"),p.getString("position"),p.getString("source")) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define players; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun players(rows: List<Player>) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val array=JSONArray()
  // Paso del flujo: rows.forEach { array.put(JSONObject().put("id",it.id).put("name",it.name).put("club",it.club).put("position",it.position).put("source",it.source)) }
  rows.forEach { array.put(JSONObject().put("id",it.id).put("name",it.name).put("club",it.club).put("position",it.position).put("source",it.source)) }
  // Paso del flujo: put("players",array.toString())
  put("players",array.toString())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
