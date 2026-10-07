////Ejercicio 4
//
//fun logLine(msg: String, level: String = "INFO", brackets: Boolean = false): String =
//    if (brackets) "[$level] $msg" else "$level: $msg"
//
//fun sumTo(start: Int = 1, end: Int): Int {
//    var total = 0
//    for (i in start..end) {
//        total += i
//    }
//    return total
//}
//
//fun isPerfect(n: Int): Boolean {
//    if (n <= 1) return false
//    var sum = 0
//    for (d in 1 until n) {
//        if (n % d == 0) sum += d
//    }
//    return sum == n
//}
//
//fun main() {
//    println(logLine("Arranque"))
//    println(logLine("Disco lleno", "WARN"))
//    println(logLine("Fallo", brackets = true))
//    println(sumTo(end = 10))
//    println(sumTo(5, 8))
//    println(sumTo(end = 100, start = 98))
//
//    val perfectos = (1..500).filter { isPerfect(it) }
//    println("Perfectos hasta 500: $perfectos")
//}