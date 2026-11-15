// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E03Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(1,E03Etiquetas.mark(Mark(),listOf("Pase"," pase "),3).tags.size)
  assertEquals(1,E03Etiquetas.mark(Mark(),listOf("Pase"," pase "),3).tags.size)
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E03Etiquetas.mark(Mark(),emptyList(),6) }
  assertThrows(IllegalArgumentException::class.java) { E03Etiquetas.mark(Mark(),emptyList(),6) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X03Etiquetas.demo())
  assertNotNull(X03Etiquetas.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val base=Mark(true,"Conservar")
  // Paso del flujo: assertEquals("Conservar",E03Etiquetas.mark(base,emptyList(),0).note)
  assertEquals("Conservar",E03Etiquetas.mark(base,emptyList(),0).note)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
