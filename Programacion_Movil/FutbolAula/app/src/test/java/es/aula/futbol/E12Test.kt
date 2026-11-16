// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E12Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertTrue(E12Cache.resolve(null,Seed.players,"error").stale)
  assertTrue(E12Cache.resolve(null,Seed.players,"error").stale)
  // Paso del flujo: assertFalse(E12Cache.resolve(emptyList(),Seed.players,null).stale)
  assertFalse(E12Cache.resolve(emptyList(),Seed.players,null).stale)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X12Cache.demo())
  assertNotNull(X12Cache.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertTrue(E12Cache.resolve(null,emptyList(),"sin red").rows.isEmpty())
  assertTrue(E12Cache.resolve(null,emptyList(),"sin red").rows.isEmpty())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
