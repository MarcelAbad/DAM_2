const val MAX_BALANCE = 200

sealed interface Command {
    data class Recharge(val amount: Int) : Command
    data class Purchase(val amount: Int) : Command
}

fun parse(line: String): Result<Command> = runCatching {
    val parts = line.split(" ")
    require(parts.size == 2) { "Formato inválido" }
    val (op, rawAmount) = parts
    val amount = rawAmount.toIntOrNull() ?: error("Cantidad no numérica: $rawAmount")
    require(amount > 0) { "Cantidad no positiva: $amount" }
    when (op) {
        "RECARGA" -> Command.Recharge(amount)
        "COMPRA" -> Command.Purchase(amount)
        else -> error("Operación desconocida: $op")
    }
}

fun main() {
    val lines = listOf(
        "RECARGA 80", "COMPRA 25", "COMPRA 100", "RECARGA 150",
        "BONO 10", "RECARGA xx", "COMPRA -3", "COMPRA", "RECARGA 120"
    )
    var balance = 0
    var ok = 0
    var errors = 0

    for (line in lines) {
        parse(line)
            .mapCatching { cmd ->
                when (cmd) {
                    is Command.Recharge -> {
                        check(balance + cmd.amount <= MAX_BALANCE) {
                            "Límite superado (saldo $balance, recarga ${cmd.amount})"
                        }
                        balance + cmd.amount
                    }
                    is Command.Purchase -> {
                        check(cmd.amount <= balance) {
                            "Fondos insuficientes (saldo $balance, necesario ${cmd.amount})"
                        }
                        balance - cmd.amount
                    }
                }
            }
            .onSuccess { newBalance ->
                balance = newBalance
                ok++
                println("$line -> OK saldo=$balance")
            }
            .onFailure { e ->
                errors++
                println("$line -> ERROR: ${e.message}")
            }
    }

    println("Saldo final: $balance")
    println("Operaciones OK: $ok")
    println("Errores: $errors")
}