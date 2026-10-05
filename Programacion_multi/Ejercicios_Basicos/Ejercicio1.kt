// EJERCICIO 1 

fun main() {
    val kilo = listOf (0,5,33,100)
    var total = 0
    
    for (kg in kilo) {
        val lb = kg * 22 / 10
        println ("$kg Kg = $lb Lb")
        total += kg
    }
    val media = total.toDouble() / kilo.size
    println("Suma: $total")
    println("Media: $media")
}