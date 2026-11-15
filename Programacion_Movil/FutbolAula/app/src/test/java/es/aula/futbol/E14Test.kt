// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E14Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals("Hola",E14Cromo.title(" Hola ",0))
  assertEquals("Hola",E14Cromo.title(" Hola ",0))
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E14Cromo.title("",0) }
  assertThrows(IllegalArgumentException::class.java) { E14Cromo.title("",0) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X14Cromo.demo())
  assertNotNull(X14Cromo.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E14Cromo.title("x".repeat(29),0) }
  assertThrows(IllegalArgumentException::class.java) { E14Cromo.title("x".repeat(29),0) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
