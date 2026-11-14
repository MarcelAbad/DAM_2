/* erp-insignia.js */
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

/* erp-tarjeta.js */
/**
 * <erp-tarjeta>: tarjeta con un titulo y un numero grande.
 * Propiedades: titulo (defecto "Sin titulo"), valor (defecto "0"), color (defecto "#2b5cff")
 * Evento: "seleccionada" (al hacer clic), con detail { titulo, valor }
 * Uso: <erp-tarjeta titulo="Clientes" valor="12"></erp-tarjeta>
 */
class ErpTarjeta extends HTMLElement {            // nuestro componente = una clase que extiende HTMLElement
  static get observedAttributes() {               // lista de atributos que vigilamos
    return ['titulo', 'valor', 'color'];          // si cambian, se avisa al componente
  }
  constructor() {                                 // se ejecuta al crear el componente
    super();                                      // obligatorio: inicia la clase base
    this.attachShadow({ mode: 'open' });          // zona interna aislada (Shadow DOM) para HTML y CSS propios
  }
  get titulo() { return this.getAttribute('titulo') || 'Sin titulo'; } // leer titulo (o valor por defecto)
  set titulo(v) { this.setAttribute('titulo', v); } // escribir titulo
  get valor() { return this.getAttribute('valor') || '0'; } // leer valor (defecto 0)
  set valor(v) { this.setAttribute('valor', v); }   // escribir valor
  get color() { return this.getAttribute('color') || '#2b5cff'; } // leer color (defecto azul)
  set color(v) { this.setAttribute('color', v); }   // escribir color
  connectedCallback() {                           // se ejecuta cuando el componente entra en la pagina
    this.pintar();                                // dibujamos su contenido
    if(!this._escucha) this.addEventListener('click', () => {        // al hacer clic sobre el componente...
      this.dispatchEvent(new CustomEvent('seleccionada', { // ...lanzamos nuestro propio evento
        detail: { titulo: this.titulo, valor: this.valor }, // datos que viajan con el evento
        bubbles: true,                            // el evento sube por los elementos padre
        composed: true,                           // y atraviesa el Shadow DOM
      }));
    });
    this._escucha=true; // No duplica eventos al reconectar.
  }
  attributeChangedCallback() { this.pintar(); }   // si cambia un atributo, volvemos a dibujar
  seguro(s) { return String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c])); } // Texto, nunca HTML ejecutable.
  pintar() {                                      // dibuja el HTML interno
    this.shadowRoot.innerHTML = `
      <style>
        .t { background:#fff; border-left:6px solid ${/^#[0-9a-f]{3,8}$/i.test(this.color)?this.color:'#2b5cff'}; border-radius:10px; padding:14px;
             box-shadow:0 1px 3px rgba(0,0,0,.15); cursor:pointer; font-family:system-ui,sans-serif; }
        small { color:#6b7488; }
        b { display:block; font-size:1.8rem; }
      </style>
      <div class="t"><small>${this.seguro(this.titulo)}</small><b>${this.seguro(this.valor)}</b></div>`;
  }
}
customElements.define('erp-tarjeta', ErpTarjeta); // registramos la etiqueta <erp-tarjeta> en el navegador
