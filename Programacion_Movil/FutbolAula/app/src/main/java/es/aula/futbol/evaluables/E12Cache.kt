// EVALUABLE E12 · "Datos sin conexión (caché)" (resuelto)
package es.aula.futbol

object E12Cache {
 // Decide qué lista se ve: la remota si todo fue bien, o la guardada si falló.
 fun resolve(remote: List<Player>?, saved: List<Player>, error: String?): CacheResult {
  // Respuesta válida (aunque venga vacía): son los datos actuales y no están caducados.
  if (remote != null && error == null) {
   val msg = if (remote.isEmpty()) "Respuesta válida sin datos" else "Datos actualizados"
   return CacheResult(remote, false, msg)
  }
  // Falló la descarga: se conservan los datos guardados y se marca stale = true.
  val msg = if (saved.isEmpty()) "Sin conexión y sin datos guardados" else "Sin conexión: se muestran los datos guardados"
  return CacheResult(saved, true, msg)
 }
 fun demo(): String = resolve(null, Seed.players, "sin red").message
}
