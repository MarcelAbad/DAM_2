// EJEMPLO RESUELTO X05 · "Sorpresa con el acelerómetro"
// ------------------------------------------------------------
// Lee docs/E05.md. Guía para resolver el evaluable E05Sensor.kt.
// La idea clave: el acelerómetro mide el movimiento en 3 ejes (x, y, z).
// Con esos tres números se calcula la "fuerza" de la sacudida.
// El resto (dar la vuelta al índice) es la parte evaluable.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X05Sensor con las funciones de este ejemplo.
object X05Sensor {
 // Devuelve el primer jugador de la lista, o null si está vacía.
 // El tipo Player? significa "un jugador o nada": hay que comprobarlo antes de usar.
 fun first(rows: List<Player>): Player? {
  // firstOrNull() no lanza error con listas vacías: simplemente devuelve null.
  return rows.firstOrNull()
 }
 // demo(): muestra el nombre del primero, o "Vacío" si no hay nadie.
 // La construcción ?: (operador Elvis) da un valor por defecto cuando hay null.
 fun demo(): String = first(Seed.players)?.name ?: "Vacío"
 // Calcula la magnitud (fuerza) a partir de las tres medidas del sensor.
 fun magnitude(x: Float,y: Float,z: Float): Float = kotlin.math.sqrt(x*x+y*y+z*z)
 // Decide si la sacudida cuenta: fuerza mayor que 22 y con 2 segundos de descanso.
 fun shouldTrigger(magnitude: Float, now: Long, last: Long): Boolean = magnitude>22f && now-last>2000
 // Prueba el cálculo con valores de ejemplo para ver si dispararía el aviso.
 fun describeMotion(): String {
  // Simula una sacudida fuerte con dos medidas del acelerómetro.
  val acceleration=magnitude(20f,20f,0f)
  // Muestra si con esos valores se activaría o no la sorpresa.
  return "¿Avisar?: ${shouldTrigger(acceleration,5000,0)}"
 }
}
