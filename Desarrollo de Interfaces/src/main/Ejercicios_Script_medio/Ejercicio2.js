
class Almacen {
    #datos = {};
    #oyentes = [];

    observar(fn) {
        this.#oyentes.push(fn);
    }

    establecer(clave, valor) {
        if (this.#datos[clave] === valor) {
            return false;
        }
        const anterior = this.#datos[clave];
        this.#datos[clave] = valor;
        for (const fn of this.#oyentes) {
            fn(clave, valor, anterior);
        }
        return true;
    }

    leer(clave) {
        return this.#datos[clave];
    }
}

const almacen = new Almacen();
const bitacora = [];

almacen.observar((clave, valor, anterior) => {
    bitacora.push(`${clave}: ${anterior === undefined ? "-" : anterior} => ${valor}`);
});
almacen.observar((clave, valor) => {
    console.log(`[panel] ${clave} = "${valor}"`);
});

console.log(almacen.establecer("tema", "claro"));
console.log(almacen.establecer("tema", "claro"));
console.log(almacen.establecer("tema", "oscuro"));
console.log(almacen.establecer("fuente", 14));

console.log(bitacora.join(" | "));
console.log(almacen.leer("idioma"));