// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E02Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals(7,E02Filtros.filter(Seed.players,"MBAPPE","","", emptySet(),false,false).single().id)
  assertEquals(7,E02Filtros.filter(Seed.players,"MBAPPE","","", emptySet(),false,false).single().id)
  // Paso del flujo: assertTrue(E02Filtros.filter(Seed.players,"nadie","","",emptySet(),false,false).isEmpty())
  assertTrue(E02Filtros.filter(Seed.players,"nadie","","",emptySet(),false,false).isEmpty())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X02Filtros.demo())
  assertNotNull(X02Filtros.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val asc=E02Filtros.filter(Seed.players,"","","",emptySet(),false,false)
  // Paso del flujo: assertEquals(asc.reversed(),E02Filtros.filter(Seed.players,"","","",emptySet(),false,true))
  assertEquals(asc.reversed(),E02Filtros.filter(Seed.players,"","","",emptySet(),false,true))
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
