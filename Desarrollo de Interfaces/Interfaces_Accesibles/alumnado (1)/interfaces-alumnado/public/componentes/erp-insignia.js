/**
 * <erp-insignia>: etiqueta de estado (EJERCICIO E5: COMPLETAR).
 * Propiedad: estado (defecto "pending"): pending | paid | shipped | cancelled
 * Evento: "cambio" -> detail { estado }
 */
class ErpInsignia extends HTMLElement {            // componente nuevo
  constructor() {                                  // al crearse
    super();                                       // iniciar clase base
    this.attachShadow({ mode: 'open' });           // zona interna aislada
  }
  // TODO 1: anade observedAttributes (['estado']) y attributeChangedCallback() que llame a pintar()
  // TODO 2: anade get/set estado, con valor por defecto 'pending'
  connectedCallback() {                            // al entrar en la pagina
    this.pintar();                                 // dibujar
    // TODO 3: al hacer clic, pasa al siguiente estado y lanza un CustomEvent 'cambio'
  }
  pintar() {                                       // dibuja el HTML interno
    // TODO 4: shadowRoot.innerHTML con un <span> y un color distinto para cada estado
  }
}
customElements.define('erp-insignia', ErpInsignia); // registra <erp-insignia>
