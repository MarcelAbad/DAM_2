// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa androidx.test.ext.junit.runners.AndroidJUnit4 para poder utilizar su API.
import androidx.test.ext.junit.runners.AndroidJUnit4
// Importa androidx.test.core.app.ApplicationProvider para poder utilizar su API.
import androidx.test.core.app.ApplicationProvider
// Importa android.content.Context para poder utilizar su API.
import android.content.Context
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Importa org.junit.runner.RunWith para poder utilizar su API.
import org.junit.runner.RunWith
// Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
@RunWith(AndroidJUnit4::class)
// Declara el modelo o componente que agrupa estas responsabilidades.
class PersistenceTest {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun guardarCerrarReabrirBorrar() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val context=ApplicationProvider.getApplicationContext<Context>()
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val name="test_${System.nanoTime()}.db"
  // Paso del flujo: try {
  try {
   // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
   Store(context,name).use { it.save(1,Mark(true,"Persistente",listOf("Pase"),4)) }
   // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
   Store(context,name).use { db -> assertEquals("Persistente",db.mark(1).note); assertTrue(db.mark(1).favorite); assertEquals(4,db.mark(1).rating); db.delete(1); assertEquals(Mark(),db.mark(1)) }
  // Paso del flujo: } finally { context.deleteDatabase(name) }
  } finally { context.deleteDatabase(name) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun restauracionYDatosIndependientes() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val context=ApplicationProvider.getApplicationContext<Context>()
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val name="test_${System.nanoTime()}.db"
  // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
  try { Store(context,name).use { db -> db.save(2,Mark(true,"Otro")); db.restore(mapOf(1 to Mark(true,"Copia"))); assertEquals("Otro",db.mark(2).note); assertEquals("Copia",db.mark(1).note) } } finally { context.deleteDatabase(name) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
