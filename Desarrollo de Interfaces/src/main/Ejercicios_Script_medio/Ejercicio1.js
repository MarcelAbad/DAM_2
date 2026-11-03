
class PaginadorComponent {
    #pagina;
    total;

    constructor(inicial = 1, total = 4) {
        this.#pagina = inicial;
        this.total = total;
    }

    get pagina() {
        return this.#pagina;
    }

    siguiente() {
        if (this.#pagina < this.total) {
            this.#pagina += 1;
            return true;
        }
        return false;
    }

    render() {
        return `<nav>Página ${this.#pagina}/${this.total}</nav>`;
    }
}

const p = new PaginadorComponent(2);
console.log(p.render());

for (let i = 0; i < 3; i++) {
    const avanzo = p.siguiente();
    console.log(`${avanzo ? "avanza" : "se queda"} -> ${p.render()}`);
}

console.log(p.pagina);
console.log(typeof p.pagina);
