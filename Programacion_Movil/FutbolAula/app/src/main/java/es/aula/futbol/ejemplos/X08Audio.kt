// EJEMPLO RESUELTO X08 · "Estados del reproductor de audio"
// ------------------------------------------------------------
// Lee docs/E08.md. Guía para resolver el evaluable E08Audio.kt.
// La idea clave: no se puede reproducir audio mientras se graba, y solo
// se puede pausar cuando el reproductor ya está preparado.
// Esos "estados" se deciden con booleanos, igual que harás con tu playlist.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X08Audio con las funciones de este ejemplo.
object X08Audio {
 // ¿Se puede reproducir ahora? Solo si está preparado y NO estamos grabando.
 fun canPlay(prepared: Boolean, recording: Boolean): Boolean {
  // Las dos condiciones deben cumplirse a la vez (&& significa "y").
  return prepared && !recording
 }
 // demo(): prueba con "preparado y sin grabar" (el caso normal).
 fun demo(): String = canPlay(true,false).toString()
 // Traduce el estado a un texto claro para el usuario.
 fun playbackState(prepared: Boolean,recording: Boolean): String {
  // Primero se mira si está grabando: grabar manda sobre reproducir.
  return if(recording) "Grabando: no reproducir" else if(canPlay(prepared,false)) "Listo para reproducir" else "Preparando o sin audio"
 }
 // Convierte segundos a un reloj legible tipo 03:25 para el usuario.
 fun durationLabel(seconds: Int): String {
  // Los segundos negativos no existen: se rechazan.
  require(seconds>=0)
  // format construye el texto: minutos a la izquierda y segundos a la derecha.
  return "%02d:%02d".format(seconds/60,seconds%60)
 }
}
