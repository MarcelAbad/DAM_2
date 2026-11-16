// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa android.content.ContentProvider para poder utilizar su API.
import android.content.ContentProvider
// Importa android.content.ContentValues para poder utilizar su API.
import android.content.ContentValues
// Importa android.database.Cursor para poder utilizar su API.
import android.database.Cursor
// Importa android.database.MatrixCursor para poder utilizar su API.
import android.database.MatrixCursor
// Importa android.net.Uri para poder utilizar su API.
import android.net.Uri
// Importa android.os.ParcelFileDescriptor para poder utilizar su API.
import android.os.ParcelFileDescriptor
// Importa android.provider.OpenableColumns para poder utilizar su API.
import android.provider.OpenableColumns
// Importa java.io.File para poder utilizar su API.
import java.io.File
// Declara el modelo o componente que agrupa estas responsabilidades.
class CardProvider : ContentProvider() {
 // Implementa la respuesta al evento onCreate.
 override fun onCreate(): Boolean = true
 // Define file; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun file(uri: Uri): File {
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(uri.path == "/cromo.png") { "Ruta no autorizada" }
  // Devuelve este resultado al código que invocó la función.
  return File(requireNotNull(context).cacheDir,"cromo.png")
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento getType.
 override fun getType(uri: Uri): String = "image/png"
 // Implementa la respuesta al evento openFile.
 override fun openFile(uri: Uri,mode: String): ParcelFileDescriptor {
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(mode == "r") { "Solo lectura" }
  // Devuelve este resultado al código que invocó la función.
  return ParcelFileDescriptor.open(file(uri),ParcelFileDescriptor.MODE_READ_ONLY)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento query.
 override fun query(uri: Uri,projection: Array<out String>?,selection: String?,selectionArgs: Array<out String>?,sortOrder: String?): Cursor {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val f=file(uri)
  // Devuelve este resultado al código que invocó la función.
  return MatrixCursor(arrayOf(OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE)).apply { addRow(arrayOf(f.name,f.length())) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento insert.
 override fun insert(uri: Uri,values: ContentValues?): Uri? = throw UnsupportedOperationException()
 // Implementa la respuesta al evento update.
 override fun update(uri: Uri,values: ContentValues?,selection: String?,selectionArgs: Array<out String>?): Int = 0
 // Implementa la respuesta al evento delete.
 override fun delete(uri: Uri,selection: String?,selectionArgs: Array<out String>?): Int = 0
// Cierra el bloque anterior y devuelve el control a su contexto.
}
