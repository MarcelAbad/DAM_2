
libros = [
    {"id": 1, "titulo": "Quijote", "autor_id": 1},
    {"id": 2, "titulo": "Ficciones", "autor_id": 2},
    {"id": 3, "titulo": "Anonimo", "autor_id": None},
    {"id": 4, "titulo": "Emma", "autor_id": 77},
]

autores = [
    {"id": 1, "nombre": "Cervantes"},
    {"id": 2, "nombre": "Borges"},
    {"id": 3, "nombre": "Austen"},
]


def inner_join(izq, der, col_izq, col_der="id"):
    indice = {fila[col_der]: fila for fila in der}
    return [(f, indice[f[col_izq]]) for f in izq if f[col_izq] in indice]


def left_join(izq, der, col_izq, col_der="id"):
    indice = {fila[col_der]: fila for fila in der}
    return [(f, indice.get(f[col_izq])) for f in izq]


print("INNER JOIN")
for libro, autor in inner_join(libros, autores, "autor_id"):
    print(f"{libro['titulo']} -> {autor['nombre']}")

print("LEFT JOIN")
for libro, autor in left_join(libros, autores, "autor_id"):
    nombre = autor["nombre"] if autor is not None else "NULL"
    print(f"{libro['titulo']} -> {nombre}")

print(inner_join([], autores, "autor_id"))