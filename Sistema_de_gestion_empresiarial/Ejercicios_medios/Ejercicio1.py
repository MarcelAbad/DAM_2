
from dataclasses import dataclass


class RestriccionError(Exception):
    pass


@dataclass(frozen=True)
class Vehiculo:
    matricula: str
    marca: str
    km: int


class TablaVehiculos:
    def __init__(self):
        self._filas = {}

    def insert(self, v):
        if v.matricula in self._filas:
            raise RestriccionError(f"PRIMARY KEY duplicada: {v.matricula}")
        if v.marca == "":
            raise RestriccionError("NOT NULL violado: marca")
        if v.km < 0:
            raise RestriccionError(f"CHECK violado: km {v.km!r}")
        self._filas[v.matricula] = v

    def count(self):
        return len(self._filas)


tabla = TablaVehiculos()
datos = [
    Vehiculo("1111AAA", "Seat", 52000),
    Vehiculo("2222BBB", "Ford", 0),
    Vehiculo("1111AAA", "Opel", 10),
    Vehiculo("3333CCC", "", 500),
    Vehiculo("4444DDD", "Audi", -5),
]

for fila in datos:
    try:
        tabla.insert(fila)
        print(f"OK     matricula={fila.matricula}")
    except RestriccionError as e:
        print(f"ERROR  {e}")

print("Filas:", tabla.count())
print(Vehiculo("1111AAA", "Seat", 52000) == datos[0])