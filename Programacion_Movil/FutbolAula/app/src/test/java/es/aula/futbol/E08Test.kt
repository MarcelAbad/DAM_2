// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E08Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(2,E08Audio.next(0,3,-1))
  assertEquals(2,E08Audio.next(0,3,-1))
  // Paso del flujo: assertNull(E08Audio.next(0,0,1))
  assertNull(E08Audio.next(0,0,1))
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X08Audio.demo())
  assertNotNull(X08Audio.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E08Audio.next(9,3,1) }
  assertThrows(IllegalArgumentException::class.java) { E08Audio.next(9,3,1) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
