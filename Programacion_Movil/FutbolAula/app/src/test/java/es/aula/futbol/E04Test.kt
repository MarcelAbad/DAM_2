// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E04Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertTrue(E04Comparador.compare(Seed.players.take(2)).contains("Courtois"))
  assertTrue(E04Comparador.compare(Seed.players.take(2)).contains("Courtois"))
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E04Comparador.compare(listOf(Seed.players.first(),Seed.players.first())) }
  assertThrows(IllegalArgumentException::class.java) { E04Comparador.compare(listOf(Seed.players.first(),Seed.players.first())) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X04Comparador.demo())
  assertNotNull(X04Comparador.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E04Comparador.compare(emptyList()) }
  assertThrows(IllegalArgumentException::class.java) { E04Comparador.compare(emptyList()) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
