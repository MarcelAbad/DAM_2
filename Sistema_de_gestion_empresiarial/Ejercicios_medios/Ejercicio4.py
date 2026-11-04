
from typing import Callable, Generic, Hashable, TypeVar

T = TypeVar("T")


class Tabla(Generic[T]):
    def __init__(self, pk: Callable[[T], Hashable]):
        self._pk = pk
        self._filas = {}

    def insert(self, fila):
        clave = self._pk(fila)
        if clave in self._filas:
            raise KeyError(f"Codigo duplicado: {clave}")
        self._filas[clave] = fila

    def select(self, where=lambda f: True, order_by=None, desc=False):
        resultado = [f for f in self._filas.values() if where(f)]
        if order_by is not None:
            resultado = sorted(resultado, key=order_by, reverse=desc)
        return resultado


tabla: Tabla[tuple] = Tabla(pk=lambda f: f[0])
tabla.insert((10, "Zoe", 80))
tabla.insert((11, "Biel", 20))
tabla.insert((12, "Nuria", 150))
tabla.insert((13, "Hugo", 45))

try:
    tabla.insert((11, "Otro", 1))
except KeyError as e:
    print("Error:", e)

print("WHERE puntos < 100 ORDER BY puntos ASC")
for fila in tabla.select(where=lambda f: f[2] < 100, order_by=lambda f: f[2]):
    print(fila)

print("WHERE puntos < 0")
print(tabla.select(where=lambda f: f[2] < 0))