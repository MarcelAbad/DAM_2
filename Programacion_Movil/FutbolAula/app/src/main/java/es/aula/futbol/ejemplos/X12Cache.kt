// EJEMPLO RESUELTO X12 · "Datos guardados sin conexión"
// ------------------------------------------------------------
// Lee docs/E12.md. Guía para resolver el evaluable E12Cache.kt.
// La idea clave: si no hay red, la app debe seguir funcionando con lo
// guardado. Eso se decide con un resultado "stale" (caducado) o actualizado.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X12Cache con las funciones de este ejemplo.
object X12Cache {
 // ¿Hay algo guardado en el dispositivo? Cualquier lista no vacía cuenta.
 fun hasCache(saved: List<Player>): Boolean {
  // isNotEmpty() es simplemente lo contrario de isEmpty().
  return saved.isNotEmpty()
 }
 // demo(): comprueba si el catálogo inicial cuenta como datos guardados.
 fun demo(): String = hasCache(Seed.players).toString()
 // Texto claro del estado de los datos guardados, con fecha de actualización.
 fun label(saved: List<Player>,timestamp: String): String {
  // Si hay datos se cuenta cuántos; si no, se explica que aún no hay nada.
  return if(hasCache(saved)) "${saved.size} guardados. Fecha: $timestamp" else "Todavía no hay datos guardados"
 }
 // Qué devolver si la descarga falla: los datos guardados, marcados como caducados.
 fun onFailure(saved: List<Player>): CacheResult = CacheResult(saved,true,label(saved,"desconocida"))
}
