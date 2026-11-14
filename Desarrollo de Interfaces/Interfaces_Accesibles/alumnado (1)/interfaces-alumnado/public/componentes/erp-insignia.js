/**
 * <erp-insignia>: etiqueta de estado de un pedido (EJERCICIO E5).
 * Propiedad: estado (defecto "pending"): pending | paid | shipped | cancelled
 * Evento: "cambio" -> detail { estado }  (al hacer clic pasa al siguiente estado)
 * Uso: <erp-insignia estado="paid"></erp-insignia>
 */

// Lista de estados EN ORDEN del ciclo, con el texto y el color de cada uno.
// Se define fuera de la clase (una sola vez) y con nombre propio para no chocar
// con otras variables cuando se junta todo en dist/erp-componentes.js.
const ESTADOS_INSIGNIA = {                         // objeto: clave = estado, valor = como se ve
  pending:   { texto: 'Pendiente', color: '#b45309' }, // naranja oscuro
  paid:      { texto: 'Pagado',    color: '#17753a' }, // verde
  shipped:   { texto: 'Enviado',   color: '#1d4ed8' }, // azul
  cancelled: { texto: 'Cancelado', color: '#c0262d' }  // rojo
};                                                 // (colores oscuros: el texto blanco se lee bien)
const ORDEN_INSIGNIA = Object.keys(ESTADOS_INSIGNIA); // ['pending','paid','shipped','cancelled'] en ese orden

class ErpInsignia extends HTMLElement {            // componente nuevo = clase que extiende HTMLElement
  // TODO 1 (HECHO): atributos vigilados + reaccion a sus cambios
  static get observedAttributes() {                // lista de atributos que el navegador vigila
    return ['estado'];                             // solo vigilamos "estado"
  }
  attributeChangedCallback() {                     // se llama solo cuando cambia el atributo "estado"
    this.pintar();                                 // volvemos a dibujar con el estado nuevo
  }

  constructor() {                                  // se ejecuta UNA vez al crear cada insignia
    super();                                       // obligatorio: inicia la clase base
    this.attachShadow({ mode: 'open' });           // zona interna aislada (Shadow DOM): su HTML y CSS no se mezclan con la pagina
    // TODO 3 (HECHO): UN unico listener de clic por componente.
    // Esta en el constructor (se ejecuta una sola vez por insignia) y NO en connectedCallback
    // (que se ejecuta cada vez que se quita y se vuelve a insertar): asi nunca se duplica.
    this.addEventListener('click', () => {         // al hacer clic en la insignia...
      const siguiente = ORDEN_INSIGNIA[(ORDEN_INSIGNIA.indexOf(this.estado) + 1) % ORDEN_INSIGNIA.length]; // ...posicion actual + 1; el % 4 vuelve a pending tras cancelled
      this.estado = siguiente;                     // guardamos el nuevo estado (esto repinta solo)
      this.dispatchEvent(new CustomEvent('cambio', { // lanzamos nuestro propio evento
        detail: { estado: siguiente },             // dato que viaja con el evento
        bubbles: true,                             // sube por los elementos padre
        composed: true                             // y atraviesa el Shadow DOM
      }));
    });
  }

  // TODO 2 (HECHO): get/set estado con valor por defecto 'pending'
  get estado() {                                   // leer el estado
    const valor = this.getAttribute('estado');     // lo que haya en el atributo (o null si no existe)
    return ORDEN_INSIGNIA.includes(valor) ? valor : 'pending'; // si no existe o es invalido (xyz) -> pending
  }
  set estado(valor) {                              // escribir el estado
    this.setAttribute('estado', valor);            // se guarda en el atributo (y eso dispara attributeChangedCallback)
  }

  connectedCallback() {                            // al entrar en la pagina
    this.pintar();                                 // dibujar (tambien al reinsertarla)
  }

  // TODO 4 (HECHO): dibujar un boton con un color distinto por estado
  pintar() {                                       // dibuja el HTML interno
    const e = ESTADOS_INSIGNIA[this.estado];       // texto y color del estado actual (siempre valido gracias al getter)
    this.shadowRoot.innerHTML = `
      <style>
        button { background:${e.color}; color:#fff; border:0; border-radius:999px;
                 padding:6px 14px; font:600 .9rem system-ui,sans-serif; cursor:pointer; }
        button:focus-visible { outline:3px solid #000; outline-offset:2px; }
      </style>
      <button type="button" title="Estado: ${this.estado}. Pulsa para cambiar">${e.texto}</button>`; // <button>: se puede usar con teclado (Tab + Enter)
  }
}
customElements.define('erp-insignia', ErpInsignia); // registra la etiqueta <erp-insignia>
