// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*

// ─────────────────────────────────────────────────────────────────────────────
// QUÉ ES ESTE ARCHIVO
//
// CheckerTest comprueba que el COMPROBADOR (Checker.kt) funciona bien.
// Es decir: no comprueba los ejercicios del alumno, sino la herramienta
// que se usa para comprobarlos. Así el alumno puede confiar en lo que ve.
//
// ¿Qué se prueba aquí?
// 1. Que verdict() clasifica bien: pendiente, correcto e incorrecto.
// 2. Que run() detecta un TODO, un fallo y un acierto.
// 3. Que las 16 comprobaciones devuelven resultados completos y bien formados.
//
// Este test NO falla por tener ejercicios pendientes: el estado PENDIENTE
// es válido y esperado en la edición de ALUMNO. Fallaría si el comprobador
// se rompiera o devolviera resultados sin sentido.
// ─────────────────────────────────────────────────────────────────────────────

// Declara la clase de pruebas del comprobador.
class CheckerTest {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun veredictoPendiente() {
  // Un caso pendiente (TODO sin resolver) clasifica el ejercicio como PENDIENTE.
  val pending=listOf(CaseOutcome("caso",pending=true,ok=false,detail="x"))
  // El veredicto debe ser PENDIENTE aunque los demás casos pasen.
  assertEquals(CheckState.PENDIENTE,Checker.verdict(pending+CaseOutcome("otro",ok=true)))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun veredictoCorrecto() {
  // Todos los casos superados clasifican el ejercicio como CORRECTO.
  assertEquals(CheckState.CORRECTO,Checker.verdict(listOf(CaseOutcome("a",ok=true),CaseOutcome("b",ok=true))))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun veredictoIncorrecto() {
  // Un solo caso fallido clasifica el ejercicio como INCORRECTO, aunque compile.
  assertEquals(CheckState.INCORRECTO,Checker.verdict(listOf(CaseOutcome("a",ok=true),CaseOutcome("b",ok=false,detail="fallo"))))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun runDetectaPendiente() {
  // Si el bloque lanza TODO(), run debe marcar el caso como pendiente, no como error.
  val outcome=Checker.run("prueba") { TODO("E00 pendiente") }
  // Comprobamos las tres propiedades que definen un caso pendiente.
  assertTrue(outcome.pending && !outcome.ok && outcome.detail.contains("E00"))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun runDetectaFallo() {
  // Si el bloque lanza una comprobación fallida, run debe marcarlo como fallo con su mensaje.
  val outcome=Checker.run("prueba") { throw AssertionError("se esperaba 21") }
  // Comprobamos que no es pendiente, que no está ok y que se guarda la explicación.
  assertTrue(!outcome.pending && !outcome.ok && outcome.detail.contains("se esperaba 21"))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun runDetectaExcepcionInesperada() {
  // Una excepción que no es un TODO también se clasifica como fallo, nunca cierra la app.
  val outcome=Checker.run("prueba") { error("boom") }
  // El detalle del fallo contiene el mensaje de la excepción original.
  assertTrue(!outcome.pending && !outcome.ok && outcome.detail.contains("boom"))
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun runDetectaExito() {
  // Si el bloque se ejecuta sin lanzar nada, el caso se supera.
  val outcome=Checker.run("prueba") { }
  // El caso queda marcado como correcto con su detalle estándar.
  assertTrue(!outcome.pending && outcome.ok && outcome.detail=="Correcto")
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun losDieciseisResultadosSonValidos() {
  // Recorremos los 16 ejercicios comprobando que el resultado es utilizable.
  (1..16).forEach { n ->
   // Ejecutamos la comprobación del ejercicio n.
   val r=Checker.check(n)
   // El número del ejercicio coincide con el pedido.
   assertEquals(n,r.exercise)
   // El título nunca puede estar vacío: se muestra en la interfaz.
   assertTrue(r.title.isNotBlank())
   // Cada ejercicio trae al menos un caso de prueba.
   assertTrue(r.cases.isNotEmpty())
   // Los casos tienen nombres legibles para el alumno.
   assertTrue(r.cases.all { it.name.isNotBlank() })
   // El estado es uno de los tres posibles, nunca otro valor raro.
   assertTrue(r.state in listOf(CheckState.PENDIENTE,CheckState.CORRECTO,CheckState.INCORRECTO))
  }
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun comprobarTodoDevuelveDieciseis() {
  // La comprobación completa devuelve exactamente 16 resultados, uno por ejercicio.
  assertEquals(16,Checker.all().size)
  // Y los números de ejercicio van del 1 al 16 sin repetirse.
  assertEquals((1..16).toList(),Checker.all().map { it.exercise })
 }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun numeroDesconocidoSeRechaza() {
  // Pedir el ejercicio 17 (que no existe) debe lanzar un error controlado.
  assertThrows(IllegalStateException::class.java) { Checker.check(17) }
 }
}
