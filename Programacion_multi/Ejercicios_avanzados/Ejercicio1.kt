data class Parcel(val name: String, val grams: Int, val qty: Int)

data class Shipment(val id: Int, val city: String, val parcels: List<Parcel>) {
    val weight: Int
        get() = parcels.sumOf { it.grams * it.qty }
}

fun kilos(g: Int): String = "%d.%03d".format(g / 1000, g % 1000)

fun main() {
    val shipments = listOf(
        Shipment(1, "Madrid", listOf(Parcel("Lámpara", 1200, 2))),
        Shipment(2, "Sevilla", listOf(Parcel("Libro", 400, 3), Parcel("Lámpara", 1200, 1))),
        Shipment(3, "Madrid", listOf(Parcel("Libro", 400, 5), Parcel("Cuaderno", 250, 4))),
        Shipment(4, "Bilbao", listOf(Parcel("Cuaderno", 250, 10), Parcel("Libro", 400, 1)))
    )

    val total = shipments.fold(0) { acc, s -> acc + s.weight }
    println("Peso total: ${kilos(total)} kg")

    println("Peso por ciudad:")
    shipments
        .groupBy { it.city }
        .mapValues { (_, list) -> list.sumOf { it.weight } }
        .toList()
        .sortedByDescending { it.second }
        .forEach { (city, w) -> println("$city: ${kilos(w)} kg") }

    println("Unidades por producto:")
    shipments
        .flatMap { it.parcels }
        .groupBy { it.name }
        .mapValues { (_, list) -> list.sumOf { it.qty } }
        .toList()
        .sortedByDescending { it.second }
        .forEach { (name, units) -> println("$name: $units") }

    val limit = 2800
    val first = shipments.asSequence().first { it.weight > limit }
    println("Primer envío > ${kilos(limit)} kg: #${first.id} (${kilos(first.weight)} kg)")
}