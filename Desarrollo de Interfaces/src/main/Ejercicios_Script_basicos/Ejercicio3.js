const rutas = [
    { ruta: "/inicio", titulo: "Inicio", activo: true },
    { ruta: "/perfil", titulo: "Perfil", activo: false },
    { ruta: "/ajustes", titulo: "Ajustes", activo: false },
];

function pintarMenu(items) {
    if (items.length === 0) {
        console.log("(menú vacío)");
        return;
    }
    for (const [i, it] of items.entries()) {
        const marca = it.activo ? "[*]" : "[ ]";
        console.log(`${i + 1}. ${marca} ${it.titulo} -> ${it.ruta}`);
    }
}

pintarMenu(rutas);
pintarMenu([]);