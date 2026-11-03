
class ErrorCampo extends Error {
    constructor(campo, mensaje) {
        super(mensaje);
        this.name = "ErrorCampo";
        this.campo = campo;
    }
}

const reglas = {
    correo(valor) {
        if (typeof valor !== "string" || valor.trim() === "") {
            throw new ErrorCampo("correo", "obligatorio");
        }
        if (!valor.includes("@")) {
            throw new ErrorCampo("correo", "debe contener @");
        }
    },
    cantidad(valor) {
        if (!Number.isInteger(valor)) {
            throw new ErrorCampo("cantidad", "debe ser entero");
        }
        if (valor < 1) {
            throw new ErrorCampo("cantidad", "debe ser al menos 1");
        }
    },
};

function comprobar(valores) {
    const errores = {};
    for (const campo of Object.keys(reglas)) {
        try {
            reglas[campo](valores[campo]);
        } catch (e) {
            if (!(e instanceof ErrorCampo)) {
                throw e;
            }
            errores[e.campo] = e.message;
        }
    }
    return { valido: Object.keys(errores).length === 0, errores };
}

const pedidos = [
    { correo: "a@b.es", cantidad: 3 },
    { correo: "sin-arroba", cantidad: 0 },
    { correo: "", cantidad: 1.5 },
    {},
];

pedidos.forEach((pedido, i) => {
    const { valido, errores } = comprobar(pedido);
    console.log(`Pedido ${i + 1}: ${valido ? "CORRECTO" : "INCORRECTO"}`);
    for (const [campo, mensaje] of Object.entries(errores)) {
        console.log(`  - ${campo}: ${mensaje}`);
    }
});