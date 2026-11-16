for importe in [30, 100, 200]:
    if importe < 50:
        envio = 5.95
    elif importe < 150:
        envio = 2.95
    else:
        envio = 0

    total = importe + envio

    print(f"Pedido {importe:.2f}€ -> envío {envio:.2f}€ -> total {total:.2f}€")