// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa androidx.test.ext.junit.runners.AndroidJUnit4 para poder utilizar su API.
import androidx.test.ext.junit.runners.AndroidJUnit4
// Importa androidx.test.core.app.ActivityScenario para poder utilizar su API.
import androidx.test.core.app.ActivityScenario
// Importa androidx.test.espresso.Espresso.onView para poder utilizar su API.
import androidx.test.espresso.Espresso.onView
// Importa androidx.test.espresso.matcher.ViewMatchers.* para poder utilizar su API.
import androidx.test.espresso.matcher.ViewMatchers.*
// Importa androidx.test.espresso.assertion.ViewAssertions.matches para poder utilizar su API.
import androidx.test.espresso.assertion.ViewAssertions.matches
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.runner.RunWith para poder utilizar su API.
import org.junit.runner.RunWith
// Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
@RunWith(AndroidJUnit4::class)
// Declara el modelo o componente que agrupa estas responsabilidades.
class SmokeTest {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun catalogoVisible() {
  // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
  ActivityScenario.launch(MainActivity::class.java).use { onView(withText("Menú de la aplicación")).check(matches(isDisplayed())) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
