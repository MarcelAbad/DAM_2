// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa android.annotation.SuppressLint para poder utilizar su API.
import android.annotation.SuppressLint
// Importa android.bluetooth.BluetoothDevice para poder utilizar su API.
import android.bluetooth.BluetoothDevice
// Importa android.bluetooth.BluetoothServerSocket para poder utilizar su API.
import android.bluetooth.BluetoothServerSocket
// Importa android.bluetooth.BluetoothSocket para poder utilizar su API.
import android.bluetooth.BluetoothSocket
// Importa android.bluetooth.BluetoothAdapter para poder utilizar su API.
import android.bluetooth.BluetoothAdapter
// Importa java.util.UUID para poder utilizar su API.
import java.util.UUID
// Importa java.util.concurrent.Executors para poder utilizar su API.
import java.util.concurrent.Executors
// Importa java.util.concurrent.TimeUnit para poder utilizar su API.
import java.util.concurrent.TimeUnit
// Declara el modelo o componente que agrupa estas responsabilidades.
class BluetoothLink {
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 private val watchdog=Executors.newSingleThreadScheduledExecutor()
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @Volatile private var active: BluetoothSocket?=null
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @Volatile private var server: BluetoothServerSocket?=null
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 private val uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
 // Define cancel; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun cancel() { runCatching { active?.close() }; runCatching { server?.close() } }
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @SuppressLint("MissingPermission")
 // Define send; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun send(device: BluetoothDevice,payload: String,ack: Boolean): String {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val deadline=watchdog.schedule({ cancel() },20,TimeUnit.SECONDS)
  // Paso del flujo: try {
  try {
   // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
   device.createRfcommSocketToServiceRecord(uuid).use { socket ->
    // Paso del flujo: active=socket
    active=socket
    // Establece conexión RFCOMM con el receptor que ofrece el servicio SPP.
    socket.connect()
    // Paso del flujo: socket.outputStream.write((payload+"\n").toByteArray(Charsets.UTF_8))
    socket.outputStream.write((payload+"\n").toByteArray(Charsets.UTF_8))
    // Paso del flujo: socket.outputStream.flush()
    socket.outputStream.flush()
    // Evalúa la condición y ejecuta únicamente el camino correspondiente.
    if(ack) check(readLine(socket)=="ACK") { "Confirmación incorrecta" }
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
   // Devuelve este resultado al código que invocó la función.
   return if(ack) "Recepción confirmada" else "Enviado: comprueba el servidor SPP"
  // Paso del flujo: } finally { active=null; deadline.cancel(false) }
  } finally { active=null; deadline.cancel(false) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define readLine; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun readLine(socket: BluetoothSocket): String {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val bytes=java.io.ByteArrayOutputStream()
  // Paso del flujo: while(true) { val value=socket.inputStream.read(); check(value!=-1) { "Conexión cerrada" }; if(value==10) break; require(bytes.size()<8192) { "Mensaje demasiado largo" }; bytes.write(value) }
  while(true) { val value=socket.inputStream.read(); check(value!=-1) { "Conexión cerrada" }; if(value==10) break; require(bytes.size()<8192) { "Mensaje demasiado largo" }; bytes.write(value) }
  // Convierte los bytes recibidos a texto y quita el salto de línea final, si existe.
  return bytes.toString("UTF-8").trimEnd('\n')
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @SuppressLint("MissingPermission")
 // Define receive; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun receive(adapter: BluetoothAdapter,validate: (String)->List<Int>): List<Int> {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val deadline=watchdog.schedule({ cancel() },30,TimeUnit.SECONDS)
  // Paso del flujo: try {
  try {
   // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
   adapter.listenUsingRfcommWithServiceRecord("FutbolAula",uuid).use { listener ->
    // Paso del flujo: server=listener
    server=listener
    // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
    listener.accept(25000).use { socket ->
     // Paso del flujo: active=socket
     active=socket
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val ids=validate(readLine(socket))
     // Paso del flujo: socket.outputStream.write("ACK\n".toByteArray())
     socket.outputStream.write("ACK\n".toByteArray())
     // Paso del flujo: socket.outputStream.flush()
     socket.outputStream.flush()
     // Devuelve este resultado al código que invocó la función.
     return ids
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
  // Paso del flujo: } finally { active=null; server=null; deadline.cancel(false) }
  } finally { active=null; server=null; deadline.cancel(false) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define close; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun close() { cancel(); watchdog.shutdownNow() }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
