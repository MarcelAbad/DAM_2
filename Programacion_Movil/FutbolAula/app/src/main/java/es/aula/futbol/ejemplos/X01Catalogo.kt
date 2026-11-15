// EJEMPLO RESUELTO X01 · "Añadir un jugador al catálogo"
// ------------------------------------------------------------
// Lee docs/E01.md. Este archivo es un EJEMPLO: ya está resuelto y
// sirve de guía para resolver el evaluable E01Catalogo.kt.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X01Catalogo: agrupa las funciones de este ejemplo.
// Un `object` es una clase con una única copia: se usa como X01Catalogo.addOne(...).
object X01Catalogo {
 // Añade UN jugador a una lista SIEMPRE que su ID no exista ya.
 // `rows` es la lista actual; `player` es la ficha nueva; devuelve la lista resultante.
 fun addOne(rows: List<Player>, player: Player): List<Player> {
  // require comprueba una condición obligatoria: si es falsa, lanza error y para.
  // none { ... } devuelve true si NINGÚN jugador tiene ya ese ID (evita duplicados).
  require(rows.none { it.id == player.id })
  // Devuelve una lista NUEVA: la anterior + el jugador. No modifica la original.
  return rows + player
 }
 // demo() sirve para probar desde la app: añade Nico Williams y cuenta cuántos hay.
 // Seed.players son los 16 iniciales; Seed.extra es el lote del Athletic.
 fun demo(): String = addOne(Seed.players, Seed.extra.first()).size.toString()
 // Comprueba que la ficha tenga datos publicables: fuente https y nombre visible.
 // source es la web oficial del club de donde salen los datos.
 fun validSource(player: Player): Boolean = player.source.startsWith("https://") && player.name.isNotBlank()
 // Construye una línea resumen del jugador: nombre · club · posición.
 fun preview(player: Player): String = "${player.name} · ${player.club} · ${player.position}"
 // Une las piezas anteriores: valida, añade y explica el resultado con texto.
 fun addAndDescribe(rows: List<Player>, player: Player): String {
  // Primero valida; si falla, el mensaje explica qué revisar.
  require(validSource(player)) { "Comprueba nombre y fuente" }
  // Guarda la lista actualizada en una variable para poder contarla.
  val updated=addOne(rows,player)
  // Devuelve un texto claro: cuántos había, cuántos hay y quién se añadió.
  return "Antes: ${rows.size}. Después: ${updated.size}. Añadido: ${preview(player)}"
 }
}
