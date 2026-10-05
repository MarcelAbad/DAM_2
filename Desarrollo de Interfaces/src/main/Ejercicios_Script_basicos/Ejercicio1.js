const ajustes = {
    modulo: "Login",
    version: 3,
    habilitado: false,
    descripcion: null,
    etiqueta: undefined,
};

for (const clave of ["modulo", "version", "habilitado", "descripcion", "etiqueta"]) {
    const valor = ajustes[clave];
    console.log(`${clave}: ${valor ?? "(sin valor)"} -> ${typeof valor}`);
}

console.log(`${ajustes.version == "3"}`);
console.log(`${ajustes.version === "3"}`);