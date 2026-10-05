function completar(plantilla, datos) {
    let resultado = "";
    let pos = 0;

    while (pos < plantilla.length) {
        const inicio = plantilla.indexOf("{{", pos);
        if (inicio === -1) {
            resultado += plantilla.slice(pos);
            break;
        }
        const fin = plantilla.indexOf("}}", inicio + 2);
        if (fin === -1) {
            resultado += plantilla.slice(pos);
            break;
        }

        resultado += plantilla.slice(pos, inicio);
        const nombre = plantilla.slice(inicio + 2, fin).trim();
        const valor = datos[nombre];
        resultado += valor == null ? "" : String(valor);
        pos = fin + 2;
    }

    return resultado;
}

const datos = { usuario: "Marcos", avisos: 7, equipo: null };

console.log(completar("<p>Hola {{ usuario }}</p>", datos));
console.log(completar("Avisos: {{avisos}} | Equipo: [{{ equipo }}] | Extra: [{{ zona }}]", datos));
console.log(completar("Texto fijo", datos));