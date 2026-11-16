// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E05Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertNull(E05Sensor.choose(emptyList(),0))
  assertNull(E05Sensor.choose(emptyList(),0))
  // Paso del flujo: assertEquals(1,E05Sensor.choose(Seed.players,16)?.id)
  assertEquals(1,E05Sensor.choose(Seed.players,16)?.id)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X05Sensor.demo())
  assertNotNull(X05Sensor.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertEquals(16,E05Sensor.choose(Seed.players,-1)?.id)
  assertEquals(16,E05Sensor.choose(Seed.players,-1)?.id)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
