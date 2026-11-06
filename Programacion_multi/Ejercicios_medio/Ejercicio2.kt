data class Product(val name: String, val category: String, val price: Double, val stock: Int)

fun main() {
    val productos = listOf(
        Product("Portátil", "Electrónica", 800.0, 5),
        Product("Ratón", "Electrónica", 20.0, 50),
        Product("Silla", "Hogar", 120.0, 10),
        Product("Mesa", "Hogar", 200.0, 4),
        Product("Libro", "Cultura", 15.0, 100)
    )

    println(productos.first())

    productos
        .groupBy { it.category }
        .mapValues { (_, lista) -> lista.sumOf { it.stock } }
        .toSortedMap()
        .forEach { (categoria, total) -> println("$categoria: $total") }

    val masCaro = productos.maxByOrNull { it.price }
    println("Más caro: ${masCaro?.name} (${masCaro?.price})")
}