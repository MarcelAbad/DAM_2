// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E10Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(listOf("A","B"),E10Colecciones.add(listOf("A")," B "))
  assertEquals(listOf("A","B"),E10Colecciones.add(listOf("A")," B "))
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E10Colecciones.add(listOf("Tecnica"),"Técnica") }
  assertThrows(IllegalArgumentException::class.java) { E10Colecciones.add(listOf("Tecnica"),"Técnica") }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X10Colecciones.demo())
  assertNotNull(X10Colecciones.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E10Colecciones.add(emptyList(),"   ") }
  assertThrows(IllegalArgumentException::class.java) { E10Colecciones.add(emptyList(),"   ") }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
