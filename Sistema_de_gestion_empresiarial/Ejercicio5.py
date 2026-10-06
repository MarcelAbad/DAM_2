def aplicar_descuento(importe, porcentaje=10):
    return round(importe - importe * porcentaje / 100, 2)


def total_final(importe, porcentaje=10, envio=4.95):
    return round(aplicar_descuento(importe, porcentaje) + envio, 2)


def es_cif_valido(cif):
    return len(cif) == 9


print(aplicar_descuento(200))
print(aplicar_descuento(80, 25))
print(total_final(200))
print(es_cif_valido("B12345678"))
print(es_cif_valido("B1234"))
print(es_cif_valido("B123456789"))