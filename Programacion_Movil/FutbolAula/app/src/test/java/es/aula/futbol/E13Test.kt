// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E13Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val q = E13Quiz.build(Seed.players,5)
  // Paso del flujo: assertEquals(5,q.map { it.playerId }.distinct().size)
  assertEquals(5,q.map { it.playerId }.distinct().size)
  // Paso del flujo: assertTrue(q.all { it.options.distinct().size == 2 && it.answer in it.options })
  assertTrue(q.all { it.options.distinct().size == 2 && it.answer in it.options })
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X13Quiz.demo())
  assertNotNull(X13Quiz.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Paso del flujo: assertThrows(IllegalArgumentException::class.java) { E13Quiz.build(Seed.players.take(1),5) }
  assertThrows(IllegalArgumentException::class.java) { E13Quiz.build(Seed.players.take(1),5) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
