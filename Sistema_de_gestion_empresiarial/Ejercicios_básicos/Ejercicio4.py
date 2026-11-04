gastos = [60.0, 25.5, 110.0, 15.0, 80.5]

print(f"Nº gastos: {len(gastos)}")
print(f"Total: {sum(gastos):.2f}€")
print(f"Menor: {min(gastos):.2f}€")
print(f"Media: {sum(gastos) / len(gastos):.2f}€")

gastos.append(40.5)
total = sum(gastos)
media = total / len(gastos)
print(f"Tras añadir -> Total: {total:.2f}€, Media: {media:.2f}€")

superiores = []
for g in gastos:
    if g > 50:
        superiores.append(g)
print(f"Superiores a 50€: {superiores}")

superiores = [g for g in gastos if g > 50]
print(f"Superiores a 50€ (comprehension): {superiores}")