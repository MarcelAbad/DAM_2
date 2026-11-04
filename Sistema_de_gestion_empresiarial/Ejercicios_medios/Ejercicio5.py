
from itertools import islice


def leer_filas(filas):
    for fila in filas:
        print(f"  [cargando {fila['id']}]")
        yield fila


def limit_offset(it, limit, offset=0):
    return list(islice(it, offset, offset + limit))


filas = [{"id": i, "titulo": f"Tema{i}"} for i in range(1, 7)]

print("LIMIT 3 OFFSET 2")
pagina = limit_offset(leer_filas(filas), limit=3, offset=2)
print([f["titulo"] for f in pagina])

print("LIMIT 2 OFFSET 20")
pagina = limit_offset(leer_filas(filas), limit=2, offset=20)
print([f["titulo"] for f in pagina])

print("Cursor perezoso")
c = leer_filas(filas)
print(next(c)["titulo"])
print(next(c)["titulo"])