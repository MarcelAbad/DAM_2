// EVALUABLE E07 · "Imagen persistente por jugador" (resuelto)
package es.aula.futbol

object E07Imagen {
 // Nombre de archivo seguro para la carpeta privada: jugador_ID.extensión.
 fun filename(id: Int, extension: String): String {
  require(id > 0) { "El ID debe ser positivo" }
  // Solo png o jpg; así "../x" o cualquier ruta del usuario se rechaza.
  require(extension == "png" || extension == "jpg") { "Extensión no admitida" }
  return "jugador_$id.$extension"
 }
 fun demo(): String = filename(7, "png")
}
