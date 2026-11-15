// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E06Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(0f,E06Contraste.parameters(1f).second,0.001f)
  assertEquals(0f,E06Contraste.parameters(1f).second,0.001f)
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E06Contraste.parameters(Float.NaN) }
  assertThrows(IllegalArgumentException::class.java) { E06Contraste.parameters(Float.NaN) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X06Contraste.demo())
  assertNotNull(X06Contraste.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertEquals(128f,E06Contraste.parameters(0f).second,0.001f)
  assertEquals(128f,E06Contraste.parameters(0f).second,0.001f)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
