const servicios = [
    { nombre: "usuarios", latenciaMs: 1250, activo: true },
    { nombre: "pedidos", latenciaMs: 80, activo: true },
    { nombre: "informes", latenciaMs: 4020, activo: false },
    { nombre: "login", latenciaMs: 305, activo: true },
];

const mayusculas = (t) => t.toUpperCase();

const segundos = (ms) =>
    `${Math.floor(ms / 1000)},${String(ms % 1000).padStart(3, "0")} s`;

function filtrarVista(lista, maxMs) {
    return lista
        .filter((s) => s.activo && s.latenciaMs <= maxMs)
        .sort((a, b) => a.latenciaMs - b.latenciaMs)
        .map((s) => `${mayusculas(s.nombre)} - ${segundos(s.latenciaMs)}`);
}

for (const max of [2000, 50]) {
    const lineas = filtrarVista(servicios, max);
    console.log(`Máximo ${segundos(max)}: ${lineas.length} resultado(s)`);
    for (const linea of lineas) {
        console.log(`  ${linea}`);
    }
}