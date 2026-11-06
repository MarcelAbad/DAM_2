fun String.countVowels(): Int = count { it.lowercaseChar() in "aeiou" }

fun List<String>.mapIf(cond: (String) -> Boolean, f: (String) -> String): List<String> =
    map { if (cond(it)) f(it) else it }

fun main() {
    for (palabra in listOf("Kotlin", "Programacion", "Sky", "AEIOU")) {
        println("$palabra -> ${palabra.countVowels()}")
    }

    val resultado = listOf("sol", "luna", "mar", "estrella")
        .mapIf({ it.length > 3 }, { it.uppercase() })
    println(resultado)
}