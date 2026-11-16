// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E16Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(8,E16Panel.counts(Seed.players)["Real Madrid"])
  assertEquals(8,E16Panel.counts(Seed.players)["Real Madrid"])
  // Paso del flujo: assertTrue(E16Panel.counts(emptyList()).isEmpty())
  assertTrue(E16Panel.counts(emptyList()).isEmpty())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X16Panel.demo())
  assertNotNull(X16Panel.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertEquals(1,E16Panel.counts(listOf(Seed.players[0],Seed.players[0]))["Real Madrid"])
  assertEquals(1,E16Panel.counts(listOf(Seed.players[0],Seed.players[0]))["Real Madrid"])
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
