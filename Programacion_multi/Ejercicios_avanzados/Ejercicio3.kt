class TaskBuilder {
    val tasks = mutableListOf<String>()

    fun task(text: String) {
        tasks.add(text)
    }

    operator fun String.unaryPlus() {
        tasks.add(this)
    }
}

class Agenda(private val title: String) {
    private val days = mutableListOf<Pair<String, List<String>>>()

    val totalTasks: Int
        get() = days.sumOf { it.second.size }

    fun day(name: String, init: TaskBuilder.() -> Unit) {
        val builder = TaskBuilder()
        builder.init()
        days.add(name to builder.tasks)
    }

    fun render(): String = buildString {
        appendLine("== $title ==")
        for ((name, tasks) in days) {
            appendLine("$name:")
            tasks.forEachIndexed { i, t -> appendLine("  ${i + 1}. $t") }
        }
    }
}

fun agenda(title: String, init: Agenda.() -> Unit): Agenda = Agenda(title).apply(init)

fun main() {
    val a = agenda("Agenda") {
        day("Lunes") { task("Gimnasio"); +"Estudiar" }
        day("Martes") { +"Compra"; +"Cocinar"; task("Llamar") }
        day("Miércoles") { +"Descansar" }
    }
    print(a.render())
    println("Total tareas: ${a.totalTasks}")
}