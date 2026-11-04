producto = "Ratón"
precio = 12.5
unidades = 4
tipo_iva = 10

base =  precio * unidades
cuota_iva = base * tipo_iva / 100
total = base + cuota_iva

print(f"{producto} x {unidades}")
print(f"Base: {base:.2f}€")
print(f"IVA ({tipo_iva}%): {cuota_iva:.2f}€")
print(f"Total: {total:.2f}€")
print(type(producto), type(precio), type(unidades))