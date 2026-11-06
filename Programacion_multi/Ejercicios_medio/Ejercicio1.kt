fun parsePrice(s: String?): Double? =
    s?.trim()?.toDoubleOrNull()?.takeIf { it > 0 && it <= 1000 }

fun main() {
    val entradas = listOf("19.99", " 5 ", null, "abc", "1500", "-2", "0")
    var suma = 0.0
    for (texto in entradas) {
        val precio = parsePrice(texto)
        println("[$texto] -> ${precio ?: "inválido"}")
        suma += precio ?: 0.0
    }
    println("Suma: $suma")
}