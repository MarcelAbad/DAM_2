// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class E11Test {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun contrato() {
  // Paso del flujo: assertEquals("n",E11Copia.restore(emptyMap(),Wire.encode(mapOf(1 to Mark(true,"n"))),setOf(1))[1]?.note)
  assertEquals("n",E11Copia.restore(emptyMap(),Wire.encode(mapOf(1 to Mark(true,"n"))),setOf(1))[1]?.note)
  // Paso del flujo: assertThrows(Exception::class.java) { E11Copia.restore(emptyMap(),"no-json",setOf(1)) }
  assertThrows(Exception::class.java) { E11Copia.restore(emptyMap(),"no-json",setOf(1)) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun ejemploResuelto() {
  // Paso del flujo: assertNotNull(X11Copia.demo())
  assertNotNull(X11Copia.demo())
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun casoLimite() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val old=mapOf(2 to Mark(true,"Conservar"))
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val result=E11Copia.restore(old,Wire.encode(mapOf(1 to Mark(false,"Nueva"))),setOf(1,2))
  // Paso del flujo: assertEquals("Conservar",result[2]?.note)
  assertEquals("Conservar",result[2]?.note)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
