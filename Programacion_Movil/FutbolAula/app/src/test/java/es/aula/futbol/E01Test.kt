// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E01Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(21, E01Catalogo.addTeam(Seed.players, Seed.extra).size)
  assertEquals(21, E01Catalogo.addTeam(Seed.players, Seed.extra).size)
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E01Catalogo.addTeam(Seed.players, listOf(Seed.players.first())) }
  assertThrows(IllegalArgumentException::class.java) { E01Catalogo.addTeam(Seed.players, listOf(Seed.players.first())) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X01Catalogo.demo())
  assertNotNull(X01Catalogo.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E01Catalogo.addTeam(Seed.players,emptyList()) }
  assertThrows(IllegalArgumentException::class.java) { E01Catalogo.addTeam(Seed.players,emptyList()) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
