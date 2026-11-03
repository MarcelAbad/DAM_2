
class ComponenteBase {
    constructor(nombre) {
        this.nombre = nombre;
    }

    render() {
        return `<${this.nombre}></${this.nombre}>`;
    }
}

class CabeceraComponent extends ComponenteBase {
    constructor(texto) {
        super("app-cabecera");
        this.texto = texto;
    }

    render() {
        return `<${this.nombre}>${this.texto}</${this.nombre}>`;
    }
}

class BotonComponent extends ComponenteBase {
    constructor() {
        super("app-boton");
    }
}

const declaraciones = new Map([
    ["cabecera", () => new CabeceraComponent("Inicio")],
    ["boton", () => new BotonComponent()],
]);

function crear(nombre) {
    if (!declaraciones.has(nombre)) {
        throw new Error(`Componente no declarado: ${nombre}`);
    }
    return declaraciones.get(nombre)();
}

for (const nombre of ["cabecera", "boton", "tabla"]) {
    try {
        const comp = crear(nombre);
        console.log(`${comp.constructor.name}: ${comp.render()}`);
    } catch (e) {
        console.log(`Error: ${e.message}`);
    }
}

console.log(new BotonComponent() instanceof ComponenteBase);
console.log([...declaraciones.keys()].join(","));