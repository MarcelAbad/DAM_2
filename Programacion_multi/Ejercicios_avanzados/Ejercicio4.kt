data class Vector2D(val x: Int, val y: Int) : Comparable<Vector2D> {
    private val lengthSquared: Int get() = x * x + y * y

    operator fun plus(o: Vector2D) = Vector2D(x + o.x, y + o.y)
    operator fun minus(o: Vector2D) = Vector2D(x - o.x, y - o.y)
    operator fun times(k: Int) = Vector2D(x * k, y * k)
    operator fun unaryMinus() = Vector2D(-x, -y)

    override fun compareTo(other: Vector2D): Int =
        lengthSquared.compareTo(other.lengthSquared)

    override fun toString() = "($x, $y)"
}

fun <T : Comparable<T>> smallest(list: List<T>): T? {
    if (list.isEmpty()) return null
    var min = list[0]
    for (item in list) if (item < min) min = item
    return min
}

fun main() {
    val a = Vector2D(1, 2)
    val b = Vector2D(3, -1)
    val c = Vector2D(-2, 0)
    val d = a * 3

    println("$a + $b = ${a + b}")
    println("$a - $b = ${a - b}")
    println("$b * 2 = ${b * 2}")
    println("-$c = ${-c}")
    println("Menor vector: ${smallest(listOf(a, b, c, d))}")
    println("Menor entero: ${smallest(listOf(7, 3, 9))}")
    println("Menor palabra: ${smallest(listOf("kiwi", "pera", "manzana"))}")
    println("Lista vacía: ${smallest(emptyList<Int>())}")
    println("Ordenados por longitud: ${listOf(a, b, c, d).sorted()}")
}