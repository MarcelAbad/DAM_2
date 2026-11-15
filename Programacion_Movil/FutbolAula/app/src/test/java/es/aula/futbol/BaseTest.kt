// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa org.junit.Test para poder utilizar su API.
import org.junit.Test
// Importa org.junit.Assert.* para poder utilizar su API.
import org.junit.Assert.*
// Declara el modelo o componente que agrupa estas responsabilidades.
class BaseTest {
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun idsUnicos() { assertEquals(Seed.players.size,Seed.players.map { it.id }.distinct().size) }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun datosBasicos() { assertTrue(Seed.players.all { it.name.isNotBlank() && it.club.isNotBlank() && it.source.startsWith("https://") }) }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun normalizacion() { assertEquals("mbappe",TextKey.of(" MBAPPÉ ")) }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun copiaIdaVuelta() { val data=mapOf(1 to Mark(true,"Nota con ñ y comillas: \"sí\"")); assertEquals(data,Wire.decode(Wire.encode(data),setOf(1))) }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun versionIncorrecta() { assertThrows(IllegalArgumentException::class.java) { Wire.decode("{\"version\":2,\"marks\":[]}",setOf(1)) } }
 // Caso automatizado: prepara entradas, ejecuta la operación y comprueba el resultado esperado.
 @Test fun desconoceId() { assertTrue(Wire.decode(Wire.encode(mapOf(999 to Mark())),setOf(1)).isEmpty()) }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
