class Queue<T> {
    private val items = mutableListOf<T>()

    fun enqueue(x: T) {
        items.add(x)
    }

    fun dequeue(): T? = if (items.isEmpty()) null else items.removeAt(0)

    val isEmpty: Boolean
        get() = items.isEmpty()

    val size: Int
        get() = items.size
}

fun main() {
    val cola = Queue<String>()
    val comandos = listOf("ADD A", "ADD B", "PRINT", "ADD C", "PRINT", "PRINT", "PRINT", "ADD D")

    for (comando in comandos) {
        val partes = comando.split(" ")
        when (partes[0]) {
            "ADD" -> cola.enqueue(partes[1])
            "PRINT" -> {
                val doc = cola.dequeue()
                if (doc != null) println("Imprimiendo: $doc") else println("Cola vacía")
            }
        }
    }

    println("Pendientes: ${cola.size}")
}