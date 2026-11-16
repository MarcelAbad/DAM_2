// EJEMPLO RESUELTO X13 · "Preguntas de pregunta-respuesta"
// ------------------------------------------------------------
// Lee docs/E13.md. Guía para resolver el evaluable E13Quiz.kt.
// La idea clave: una pregunta tiene texto, DOS opciones y UNA correcta.
// El club del jugador es la respuesta; el distractor es otro club distinto.
// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol

// Declara el objeto X13Quiz con las funciones de este ejemplo.
object X13Quiz {
 // ¿La respuesta marcada es exactamente la esperada?
 fun correct(answer: String, expected: String): Boolean {
  return answer == expected
 }
 // demo(): prueba una respuesta correcta.
 fun demo(): String = correct("Real Madrid","Real Madrid").toString()
 // Construye UNA pregunta sobre un jugador usando otro club como distractor.
 fun oneQuestion(player: Player,otherClub: String): Question {
  // El distractor debe ser un club DISTINTO: si no, la pregunta no tiene gracia.
  require(otherClub!=player.club)
  // La respuesta correcta es el club real del jugador; las opciones van desordenadas por quien las mezcla.
  return Question(player.id,"¿Dónde juega ${player.name}?",listOf(player.club,otherClub),player.club)
 }
 // Puntúa una respuesta: 1 punto si acierta, 0 si falla.
 fun score(answer: String,question: Question): Int = if(correct(answer,question.answer)) 1 else 0
}
