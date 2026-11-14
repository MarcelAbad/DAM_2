# Componentes
## erp-tarjeta
| Propiedad | Defecto | Descripcion |
|---|---|---|
| titulo | Sin titulo | Texto pequeno |
| valor | 0 | Numero grande |
| color | #2b5cff | Color del borde |

Evento `seleccionada` (detail: titulo, valor).
Despues de `npm run empaquetar`: `<script src="/dist/erp-componentes.js"></script>`.

## erp-insignia
Etiqueta (boton) de estado de un pedido. Cada clic pasa al siguiente estado.

| Propiedad | Defecto | Descripcion |
|---|---|---|
| estado | pending | `pending` (Pendiente), `paid` (Pagado), `shipped` (Enviado) o `cancelled` (Cancelado). Si falta o es invalido, vale `pending`. |

Ciclo de un clic: `pending` -> `paid` -> `shipped` -> `cancelled` -> `pending`.

Evento `cambio` (detail: `{ estado }`, con `bubbles` y `composed`): se lanza tras cada clic con el estado NUEVO.
Se puede escuchar en la propia etiqueta o en `document`.

Un solo listener de clic por insignia: quitarla y volver a insertarla no duplica eventos.

Ejemplo HTML:
```html
<erp-insignia estado="paid"></erp-insignia>
<script>
  document.addEventListener('cambio', e => console.log(e.detail.estado));
</script>
```

Pruebas: `public/evaluables/e6-pruebas-paquete.html` (7 pruebas: defecto, valido, invalido,
clic + evento, ciclo completo, cambio visual y reconexion sin eventos duplicados).
Paquete: `npm run empaquetar` y pagina `public/componentes/paquete.html` (solo carga `/dist/erp-componentes.js`).
