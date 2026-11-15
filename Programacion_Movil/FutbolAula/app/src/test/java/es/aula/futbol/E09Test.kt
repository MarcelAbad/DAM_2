// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E09Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertFalse(E09Alineacion.validate(Seed.players.take(11)))
  assertFalse(E09Alineacion.validate(Seed.players.take(11)))
  // Paso del flujo: assertFalse(E09Alineacion.validate(emptyList()))
  assertFalse(E09Alineacion.validate(emptyList()))
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X09Alineacion.demo())
  assertNotNull(X09Alineacion.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val positions=listOf("Portero")+List(4){"Defensa"}+List(3){"Centrocampista"}+List(3){"Delantero"}
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val fixture=positions.mapIndexed { i,p -> Player(9000+i,"Fixture $i","Club de prueba",p,"https://example.test") }
  // Paso del flujo: assertTrue(E09Alineacion.validate(fixture))
  assertTrue(E09Alineacion.validate(fixture))
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
