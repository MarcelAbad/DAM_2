// EJEMPLO RESUELTO X07 · "Nombres de archivo para imágenes"
// ------------------------------------------------------------
// Lee docs/E07.md. Guía para resolver el evaluable E07Imagen.kt.
// La idea clave: cada ficha guarda su imagen con un NOMBRE FIJO
// (preview_ID.png). Así la imagen reaparece al reabrir la app.
// Nunca aceptes rutas inventadas por el usuario: solo el nombre controlado.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X07Imagen con las funciones de este ejemplo.
object X07Imagen {
 // Construye el nombre de una imagen temporal para el jugador indicado.
 fun temporaryName(id: Int): String {
  // El ID debe ser positivo: identifica a un jugador real del catálogo.
  require(id > 0)
  // El nombre se construye con el ID: preview_1.png, preview_2.png...
  return "preview_$id.png"
 }
 // demo(): muestra el nombre que tendría la imagen del jugador 1.
 fun demo(): String = temporaryName(1)
 // Relación entre extensión de archivo y tipo MIME (lo que Android usa al compartir).
 fun formats(): Map<String,String> = mapOf("png" to "image/png","jpg" to "image/jpeg")
 // Devuelve el tipo MIME de una extensión; si no existe, error controlado.
 fun mime(extension: String): String = formats()[extension] ?: error("Formato no admitido")
 // Explica que png/jpg son formatos reales de imagen, no solo letras del nombre.
 fun describe(id: Int): String = "Vista temporal: ${temporaryName(id)}. PNG es un formato, no solo una extensión."
}
