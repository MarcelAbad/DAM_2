// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E07Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals("jugador_7.png",E07Imagen.filename(7,"png"))
  assertEquals("jugador_7.png",E07Imagen.filename(7,"png"))
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E07Imagen.filename(1,"../x") }
  assertThrows(IllegalArgumentException::class.java) { E07Imagen.filename(1,"../x") }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X07Imagen.demo())
  assertNotNull(X07Imagen.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E07Imagen.filename(0,"jpg") }
  assertThrows(IllegalArgumentException::class.java) { E07Imagen.filename(0,"jpg") }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
