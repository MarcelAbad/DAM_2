// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E15Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(listOf(1,2),E15Bluetooth.decode("{\"version\":1,\"ids\":[1,2]}",setOf(1,2)))
  assertEquals(listOf(1,2),E15Bluetooth.decode("{\"version\":1,\"ids\":[1,2]}",setOf(1,2)))
  // Paso del flujo: assertThrows(Exception::class.java) { E15Bluetooth.decode("{}",setOf(1)) }
  assertThrows(Exception::class.java) { E15Bluetooth.decode("{}",setOf(1)) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X15Bluetooth.demo())
  assertNotNull(X15Bluetooth.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E15Bluetooth.decode("{\"version\":1,\"ids\":[1,1]}",setOf(1)) }
  assertThrows(IllegalArgumentException::class.java) { E15Bluetooth.decode("{\"version\":1,\"ids\":[1,1]}",setOf(1)) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
