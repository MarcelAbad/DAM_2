
from collections import defaultdict

lecturas = [
    (1, "Norte", 300),
    (2, "Sur", 500),
    (3, "Norte", 700),
    (4, "Este", 1000),
    (5, "Sur", 500),
    (6, "Norte", 0),
    (7, "Oeste", 1500),
]


def agrupar_por(filas, clave):
    grupos = defaultdict(list)
    for fila in filas:
        grupos[clave(fila)].append(fila)
    return grupos


def resumir(filas):
    grupos = agrupar_por(filas, lambda f: f[1])
    resumen = [
        (estacion, len(lista), sum(f[2] for f in lista))
        for estacion, lista in grupos.items()
    ]
    # Suma descendente; en empate, nombre ascendente
    resumen.sort(key=lambda r: (-r[2], r[0]))
    return resumen


for estacion, n, total in resumir(lecturas):
    print(f"{estacion:<6}| COUNT={n} | SUM={total} wh")

print(resumir([]))