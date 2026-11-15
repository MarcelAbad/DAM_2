// EJEMPLO RESUELTO X10 · "Listas de IDs (mini-colección)"
// ------------------------------------------------------------
// Lee docs/E10.md. Guía para resolver el evaluable E10Colecciones.kt.
// La idea clave: una colección es, al final, una lista de IDs de jugadores.
// Las reglas: IDs positivos, sin repetir, y poder añadir/quitar/cambiar.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X10Colecciones con las funciones de este ejemplo.
object X10Colecciones {
 // Añade un ID a la lista evitando duplicados.
 fun addMember(ids: List<Int>, id: Int): List<Int> {
  // Un ID debe ser positivo: 0 o negativo no identifican a nadie.
  require(id > 0)
  // distinct() quita repetidos: aunque llegue dos veces, se queda una.
  return (ids + id).distinct()
 }
 // demo(): añade el ID 1 a una lista que ya lo tiene; el resultado no lo duplica.
 fun demo(): String = addMember(listOf(1),1).toString()
 // Quita un ID de la lista; si no estaba, la lista no cambia.
 fun removeMember(ids: List<Int>,id: Int): List<Int> = ids.filter { it!=id }
 // Sustituye un ID viejo por uno nuevo, sin duplicados.
 fun replaceMember(ids: List<Int>,oldId: Int,newId: Int): List<Int> {
  // El ID nuevo debe ser válido antes de tocar la lista.
  require(newId>0)
  // map recorre la lista y cambia solo el ID viejo; distinct evita repetidos.
  return ids.map { if(it==oldId) newId else it }.distinct()
 }
 // Describe la lista con un texto para el usuario.
 fun describe(ids: List<Int>): String = "Lista de seguimiento: ${ids.size} integrantes"
}
