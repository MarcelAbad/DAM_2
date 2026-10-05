factura = 240
num_cuotas = 4
cuota_valor = factura / num_cuotas

for cuota in range(1, num_cuotas + 1):
    pagado = cuota_valor * cuota
    pendiente = factura - pagado
    print(f"Cuota {cuota}: pagado {pagado:.2f}€, pendiente {pendiente:.2f}€")


caja = 100
gasto = 30
gastos_pagados = 0

while caja >= gasto:
    caja -= gasto
    gastos_pagados += 1

print(f"Gastos pagados: {gastos_pagados}, caja restante: {caja}")