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
