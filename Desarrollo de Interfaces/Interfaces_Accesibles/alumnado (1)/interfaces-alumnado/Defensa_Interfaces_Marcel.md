# Defensa de los 7 ejercicios evaluables (Interfaces de Usuario, 2 DAM)

Documento único para estudiar, **en orden**: primero cómo está montado el proyecto y para qué sirve cada pieza, y después los 7 ejercicios (E1 a E7) explicados uno a uno. Lenguaje sencillo, con comparaciones.

## Índice
0. Visión general: qué es el proyecto, estructura y piezas
1. E1 · Dictar un cliente por voz
2. E2 · Filtrar productos por voz
3. E3 · Pasar productos con movimiento
4. E4 · Estado de pedido con la mano
5. E5 · Componente `erp-insignia`
6. E6 · Pruebas, documentación y paquete
7. E7 · Proyecto integrado
8. Cómo probarlo todo

# 0. Visión general

## 0.1 ¿Qué es el proyecto y para qué sirve?
Es una pequeña **web de gestión (un mini ERP)** con clientes, productos y pedidos. Lo importante de la asignatura no es la gestión, sino **cómo interactúa el usuario con la interfaz**:
- **Por voz** (E1, E2, E7): hablas y la página obedece.
- **Por movimiento del cuerpo** (E3): la cámara detecta que mueves el brazo.
- **Por la mano** (E4, E7): la cámara ve tu mano y la usas como un ratón.
- **Con componentes propios** (E5, E6): etiquetas HTML inventadas por nosotros, probadas y empaquetadas.
- **Con realidad aumentada simple** (E7): información flotando sobre la imagen de la cámara.
Todo eso son **interfaces accesibles**: formas alternativas al ratón y teclado para manejar una aplicación.

## 0.2 Estructura de carpetas (y para qué sirve cada una)
```
interfaces-alumnado/
├─ public/                 ← AQUÍ trabajamos (lo que ve el navegador)
│  ├─ index.html           ← página central con enlaces a todos los ejercicios
│  ├─ resueltos/           ← ejemplos resueltos R1–R7 (de dónde aprendemos)
│  ├─ evaluables/          ← los 7 ejercicios E1–E7 (los que se entregan)
│  ├─ componentes/         ← componentes web propios (erp-tarjeta, erp-insignia) + README + paquete.html
│  ├─ dist/                ← el paquete generado (erp-componentes.js)
│  ├─ js/                  ← ayudas.js y logica.js (funciones ya hechas)
│  └─ css/estilos.css      ← colores y tamaños de toda la web
├─ pages/api/[entity].js   ← BACKEND (no se toca): API de clientes, productos y pedidos
├─ data/db.json            ← la "base de datos" (un fichero de texto con los datos)
├─ tools/empaquetar.js     ← une los componentes en un solo fichero
└─ tests/run.js            ← pruebas automáticas del proyecto (npm test)
```
**Frontend** = lo que se ve y se usa en el navegador (`public/`). **Backend** = el servidor que guarda los datos (`pages/api`, `data`). Solo modificamos el frontend.

## 0.3 Cómo está hecho cada ejercicio (estructura común)
Todos los ejercicios son **un único fichero `.html`** con tres partes:
1. **HTML** (arriba): lo que se ve (botones, campos, tabla, vídeo). Ya viene hecho.
2. **Enunciado**: una caja con los pasos que hay que seguir.
3. **`<script type="module">`** (abajo): **el código que escribimos nosotros**, dividido en tres bloques `TODO`:
   - **TODO 1 · Preparar**: coger los elementos de la página, cargar datos o componentes.
   - **TODO 2 · Programar**: las acciones y eventos (qué pasa al hablar, al pulsar, en cada fotograma…).
   - **TODO 3 · Mostrar resultados y errores**: avisar al usuario y controlar los fallos.

## 0.4 Las ayudas ya hechas (no las he modificado)
- **`Ayudas.voz(boton, funcion, mensaje)`**: al pulsar el botón escucha UNA frase en español, se la pasa a tu función y controla los errores del micrófono (permiso denegado, navegador sin voz). Se usa en E2 y E7.
- **`Ayudas.pedir(url)`**: hace `fetch`, **comprueba `ok`** y devuelve el JSON; si el servidor da error, lanza una excepción.
- **`Ayudas.escribir(url, método, datos)`**: igual pero enviando JSON (POST, PUT…). Se usa en E4.
- **El backend** (`pages/api/[entity].js`): `/api/customers`, `/api/products`, `/api/orders` con GET (leer), POST (crear), PUT (modificar) y DELETE (borrar). Guarda todo en `data/db.json`.

## 0.5 Qué he cambiado y dónde (todo dentro de `public/`)
- `evaluables/e1…e7-*.html` → los 7 ejercicios (cada TODO está marcado como HECHO).
- `componentes/erp-insignia.js` → el componente de E5 (lo usan E5, E6 y E7).
- `componentes/README.md` → documentación del componente (E6).
- `componentes/paquete.html` + `dist/erp-componentes.js` → el paquete de E6 y su página de comprobación.

## 0.6 Cómo se ha comprobado cada uno (para que lo cuentes con honestidad)
- Cargados en un navegador real con un servidor de prueba: E2 (voz simulada), E5, E6 (7 pruebas OK) y el paquete.
- E3, E4 y E7: la cámara y la mano **se han simulado** (imágenes y puntos falsos); el código reacciona bien. **La cámara y el micrófono reales tienes que probarlos tú en Chrome.**
- `npm test` del proyecto: 68 pruebas, sin fallos.

## 0.7 Mini-glosario (palabras que te pueden preguntar)
| Palabra | Qué es, en llano |
|---|---|
| **DOM** | La página convertida en "objetos" que JavaScript puede leer y cambiar. |
| **Evento** | Algo que pasa (un clic, una frase oída…) y a lo que podemos reaccionar con una función. |
| **fetch** | Pedirle datos al servidor (o enviárselos). |
| **JSON** | Texto con formato de objeto (`{"name":"Ana"}`) para intercambiar datos. |
| **GET / POST / PUT** | Leer / crear / modificar en el servidor. |
| **Shadow DOM** | Una "burbuja" dentro del componente: su HTML y CSS no se mezclan con los de la página. |
| **Componente web (Custom Element)** | Una etiqueta HTML inventada por nosotros (`<erp-insignia>`). |
| **MediaPipe** | Librería de Google que encuentra los 21 puntos de una mano en el vídeo. |
| **Landmark** | Cada uno de esos puntos (índice = 8, pulgar = 4, centro de la mano = 9). |
| **Espejo** | El vídeo se ve volteado como un espejo; por eso se usa `1 - x`. |
| **textContent vs innerHTML** | `textContent` pone texto tal cual (seguro). `innerHTML` interpreta HTML (peligroso si viene del usuario: XSS). |
| **Pura (función)** | Función que solo recibe datos y devuelve un resultado, sin tocar la página. |

---

---

# 1. E1 · Dictar un cliente (`public/evaluables/e1-voz-formulario.html`)

**Para qué sirve:** rellenar un formulario **sin teclear**, útil para personas con dificultades motoras o cuando tienes las manos ocupadas.
**Estructura del código (en orden):** ① coger los elementos y definir ayudas (`mostrar`, `capitalizar`, `limpiarEmail`) → ② `procesarFrase` (decide qué campo rellenar) → ③ `escuchar` (micrófono) → ④ `guardar` (validar + `fetch` POST) → ⑤ conectar los botones con `addEventListener`.
**Archivos que toca:** solo `evaluables/e1-voz-formulario.html`. **Usa:** `/api/customers`.

## 1. ¿Qué pide el ejercicio? (en una frase)
Una página con tres campos (nombre, ciudad, correo). El usuario **habla** al micrófono ("nombre Ana", "ciudad Madrid", "email ana arroba mail punto es") y la página rellena los campos sola. Al decir **"guardar"**, envía el cliente al servidor.

## 2. Comparación para entenderlo
Es como un **camarero que apunta la comanda**:
- Tú dices una frase corta ("nombre Ana").
- El camarero (el navegador) la **escribe** (reconocimiento de voz).
- Mira **la primera palabra** para saber en qué casilla apuntarla (nombre, ciudad, correo).
- Cuando dices "guardar", **lleva la comanda a cocina** (el servidor).

## 3. Qué tecnologías usa
| Cosa | Para qué |
|---|---|
| `SpeechRecognition` (`webkitSpeechRecognition` en Chrome) | Convierte tu voz en texto. Es del propio navegador, no hay que instalar nada. Solo funciona bien en **Chrome o Edge**. |
| `startsWith` + `slice` | Detectar con qué palabra empieza la frase y quedarse con lo que va detrás. |
| `fetch` con `POST` | Enviar el cliente al servidor (`/api/customers`). |
| `try / catch` | Controlar errores (sin red, servidor caído…) sin que la página se rompa. |

## 4. El código explicado paso a paso
(Los pasos coinciden con los numerados del enunciado.)

### Paso 0 – Preparar piezas (TODO 1)
Se "cogen" con `document.getElementById` los elementos que vamos a usar (los 3 inputs, el botón Hablar, el párrafo `#msg` de mensajes). Se guardan en constantes para no buscarlos cada vez.

Funciones auxiliares:
- `mostrar(texto, esError)` → escribe un mensaje en la pantalla, **verde** si va bien y **rojo** si es error. Usa `textContent` (no `innerHTML`) para que nada de lo que se diga se interprete como código HTML.
- `capitalizar("ana")` → "Ana". Es un detalle de presentación.
- `limpiarEmail(texto)` → cambia "arroba" por `@`, "punto" por `.` y quita los espacios. Ejemplo: `"ana arroba mail punto es"` → `"ana@mail.es"`.

### Paso 1 – Pulsar Hablar antes de CADA frase (`escuchar`)
Al hacer clic en **Hablar** se crea un reconocedor nuevo con:
- `lang = "es-ES"` → entiende español.
- `continuous = false` → escucha **una sola frase** y se para (lo pide el enunciado: "pulsa Hablar antes de cada frase").
- `interimResults = false` → solo da el resultado final.

Eventos:
- `onstart` → desactiva el botón y dice "Escuchando…".
- `onresult` → llega la frase (la explico abajo).
- `onerror` → controla los fallos (ver apartado 6).
- `onend` → reactiva el botón.

### Paso 2 – Limpiar la frase (dentro de `onresult`)
```js
evento.results[0][0].transcript   // lo que ha entendido el navegador
  .toLowerCase()                  // todo a minúsculas
  .trim()                         // quita espacios de los extremos
  .replace(/[.,;:!?]+$/, "")      // quita puntos finales (Chrome a veces los pone)
```
**Por qué:** si Chrome devuelve `"Nombre Ana"` o `"Guardar."`, sin limpiar no se reconocería. Con la frase limpia la comparación es siempre igual.

### Paso 3 – Detectar el campo con `startsWith` y `slice` (`procesarFrase`)
| Frase | Se detecta con | Valor que se coge |
|---|---|---|
| `nombre ana` | `startsWith("nombre ")` | `slice(7)` → `ana` |
| `ciudad madrid` | `startsWith("ciudad ")` | `slice(7)` → `madrid` |
| `email ana arroba…` | `startsWith("email ")` | `slice(6)` → `ana arroba…` |

**Por qué 7 y 6:** `"nombre "` tiene 7 caracteres (6 letras + el espacio), `"ciudad "` también 7, y `"email "` tiene 6 (5 letras + el espacio). `slice(7)` "corta" esos 7 primeros caracteres y devuelve el resto. El resultado se asigna a `.value` del input.

### Paso 4 – Arroba y punto
Con `limpiarEmail`: `replace(/arroba/g, "@")`, `replace(/punto/g, ".")` y `replace(/\s+/g, "")`. La `g` significa "todas las veces que aparezca" y `\s+` significa "uno o más espacios".

### Paso 5 – Validar al decir "guardar" (`guardar`)
1. Recoge los tres `.value` con `.trim()`.
2. Si nombre o ciudad están vacíos → error y **no se envía**.
3. El correo se comprueba con una expresión regular: `^[^\s@]+@[^\s@]+\.[^\s@]+$` = *algo + @ + algo + punto + algo, sin espacios*.

### Paso 6 – Enviar con `fetch`
```js
const respuesta = await fetch("/api/customers", {
  method: "POST",                                   // POST = crear
  headers: { "Content-Type": "application/json" },  // "te mando JSON"
  body: JSON.stringify({ name, city, email })       // objeto → texto JSON
});
if (!respuesta.ok) { ... }                          // ok es false si hay error 400/500
```
- `await` = esperar la respuesta sin congelar la página.
- `respuesta.ok` se comprueba **siempre**: `fetch` solo "falla" si no hay red; un error 500 del servidor NO lanza excepción, por eso hay que mirar `ok`.

### Paso 7 – Probar
Ana, Madrid, ana@mail.es → abrir `http://localhost:3000/api/customers` y comprobar que aparece el cliente nuevo.

## 5. Qué he mejorado respecto a la versión anterior
(Esto es lo que puedes contar si te preguntan "qué cambiaste".)
1. **Quitar el punto final** (`.replace(/[.,;:!?]+$/, "")`): Chrome a veces devuelve `"guardar."` o `"ana@mail.es."`. Antes, "guardar." no funcionaba y el correo salía inválido.
2. **Candado anti doble envío** (`guardando` + botón desactivado + `finally`): si se pulsa Guardar dos veces seguidas, antes se creaban **dos clientes iguales**.
3. **`try/catch` alrededor de `rec.start()`**: si el micrófono no puede arrancar, el botón no se queda bloqueado.
4. **Comentarios** más claros, uno por línea importante y con la numeración del enunciado.

## 6. Errores que controla (el enunciado pide explicarlos)
| Situación | Qué hace la página |
|---|---|
| Navegador sin voz (Firefox…) | Avisa: "prueba con Chrome o Edge" |
| Permiso de micrófono denegado (`not-allowed`) | Explica cómo permitirlo en el candado |
| No se oye nada (`no-speech`) | Pide repetir |
| Frase que no empieza por nombre/ciudad/email/guardar | Muestra lo que oyó y qué se puede decir |
| Nombre o ciudad vacíos | Error, no envía |
| Correo inválido | Error, no envía |
| Servidor responde error (400/500) | Muestra el código; **no borra lo escrito** |
| Sin conexión / servidor caído | Muestra error de red; **no borra lo escrito** |

## 7. Preguntas que puede hacer el profesor (con respuesta)
**¿Por qué `continuous = false`?** Porque el enunciado dice que se pulsa Hablar antes de cada frase. Así el micrófono no queda abierto todo el rato (más privacidad).

**¿Por qué `toLowerCase()` y `trim()`?** Para que "Nombre Ana " y "nombre ana" sean iguales y `startsWith` funcione siempre.

**¿Qué hace `slice(7)`?** Corta los 7 primeros caracteres ("nombre ") y devuelve el resto.

**¿Por qué `slice(6)` para email?** Porque "email " tiene un carácter menos que "nombre ".

**¿Para qué sirve `respuesta.ok`?** `fetch` no lanza error cuando el servidor contesta 400 o 500, solo cuando no hay red. `ok` nos dice si el código fue 200–299.

**¿Por qué POST y no GET?** GET es para leer; POST es para crear algo nuevo.

**¿Por qué `Content-Type: application/json`?** Para que el servidor sepa que el cuerpo es JSON y lo lea bien (si no, `req.body` llegaría vacío).

**¿Qué pasa si se cae la red?** Salta el `catch`, se muestra el error y los inputs conservan lo escrito.

**¿Por qué `textContent` y no `innerHTML`?** Con `innerHTML` un texto malicioso podría inyectar código HTML/JS (ataque XSS). `textContent` lo trata siempre como texto.

**¿Funciona en todos los navegadores?** No: la voz solo va bien en Chrome/Edge. Por eso se comprueba si existe y se avisa.

**¿Qué hace el `finally`?** Se ejecuta siempre, haya ido bien o mal; ahí reactivo el botón.

## 8. Cómo probarlo
1. Abrir una terminal en la carpeta `interfaces-alumnado` y ejecutar `npm install` (solo la primera vez) y `npm run dev`.
2. Abrir **Chrome** en `http://localhost:3000/evaluables/e1-voz-formulario.html`.
3. Pulsar Hablar → "nombre Ana"; Hablar → "ciudad Madrid"; Hablar → "email ana arroba mail punto es"; Hablar → "guardar".
4. Abrir `http://localhost:3000/api/customers` y comprobar el registro.
5. Probar también: guardar con campos vacíos, denegar el micrófono, apagar el servidor y guardar.

> Nota honesta: he comprobado la lógica con una simulación en Node (frases → campos → POST correcto, errores de red/500/correo malo). El micrófono real no se puede probar sin navegador y voz, así que esa parte hay que probarla tú en Chrome.


---

# 2. E2 · Filtrar por voz (`e2-voz-productos.html`)

**Para qué sirve:** consultar y filtrar una lista **hablando** (por ejemplo, ver rápido qué productos se están acabando).
**Estructura del código (en orden):** ① lista `productos` + `mostrar` → ② `pintar(lista)` (dibuja la tabla) → ③ `cargar` (pide los datos) → ④ `ejecutarOrden` (interpreta la frase) → ⑤ `Ayudas.voz` conecta el micrófono → ⑥ `cargar()` arranca todo.
**Archivos que toca:** `evaluables/e2-voz-productos.html`. **Usa:** `/api/products` y `Ayudas`.

## Qué pide
Al cargar la página se ve una tabla de productos. Diciendo **"stock bajo"**, **"todos"** o **"buscar teclado"** la tabla se filtra.

## Comparación
Es un **dependiente de tienda con la lista completa**: tú le dices "enséñame los de poco stock" y él te enseña **solo esos**, pero **su lista completa sigue intacta** para cuando pidas "todos".

## Cómo está hecho
1. **`productos`**: al cargar, `Ayudas.pedir("/api/products")` guarda la respuesta. **Esa lista no se sustituye nunca** (si la sustituyera por la filtrada, "todos" no podría recuperar lo perdido).
2. **`pintar(lista)`**: vacía la tabla, crea la cabecera (Nombre, Precio, Stock) y **una fila por producto** con `createElement` y `textContent` (seguro: un nombre con `<` no rompe nada).
3. **`ejecutarOrden(frase)`**: limpia la frase (`toLowerCase().trim()` y quita el punto final) y decide:
   - `"stock bajo"` → `productos.filter(p => p.stock < 5)` (con los datos originales queda solo **Teclado**).
   - `"todos"` → `pintar(productos)`.
   - empieza por `"buscar "` → `slice(7)` (quita esos 7 caracteres) y `p.name.toLowerCase().includes(busqueda)`.
   - otra cosa → "No he entendido…".
4. **Sin resultados**: si la lista está vacía se pinta una fila **"Sin coincidencias"** (y no falla).

## Conceptos clave
- **`filter`** crea una **lista nueva** con los elementos que cumplen la condición; la original no se toca.
- **`includes`** mira si un texto **contiene** otro ("Teclado" contiene "tec").
- `toLowerCase()` en ambos lados para que mayúsculas/minúsculas no importen.

## Errores que controla
Servidor caído al cargar (mensaje rojo), micrófono denegado/sin voz (lo gestiona `Ayudas.voz`), frase no entendida, y búsqueda sin resultados.

## Preguntas del profesor
- **¿Por qué no sustituyes `productos` al filtrar?** Para poder volver a "todos". `filter` devuelve una lista nueva.
- **¿Por qué `slice(7)`?** "buscar " tiene 7 caracteres (6 letras + espacio).
- **¿Qué pasa con "buscar xyz"?** `filter` devuelve lista vacía y `pintar` enseña "Sin coincidencias"; no hay error.
- **¿Por qué `textContent`?** Evita XSS: el texto nunca se interpreta como HTML.
- **¿Por qué usas `Ayudas.voz`?** Es la ayuda proporcionada: escucha una frase por pulsación y controla errores; así no repito código.

---

# 3. E3 · Pasar productos con movimiento (`e3-movimiento-productos.html`)

**Para qué sirve:** manejar una aplicación **moviendo el cuerpo** delante de la cámara, sin tocar nada (por ejemplo, en un puesto donde no puedes usar las manos en el teclado).
**Estructura del código (en orden):** ① constantes (tamaño, umbrales) y elementos → ② `mostrarProducto` (actualiza la tarjeta) → ③ `bucle` (compara fotogramas y decide) → ④ `iniciar` (carga productos, pide la cámara y arranca el bucle).
**Archivos que toca:** `evaluables/e3-movimiento-productos.html`. **Usa:** `componentes/erp-tarjeta.js`, `/api/products` y `Ayudas`.

## Qué pide
Con la cámara, **mover el brazo a la derecha** pasa al siguiente producto y **a la izquierda** al anterior. Se ve en una tarjeta (`<erp-tarjeta>`) con el nombre y el stock.

## Comparación
Como el **"juego de las diferencias"**: la cámara hace fotos muy seguidas y **compara cada foto con la anterior**. Los píxeles que han cambiado mucho son "movimiento". Si el cambio está en la mitad derecha de la foto → derecha; si está en la izquierda → izquierda.

**Importante (lo dice el enunciado):** **no reconoce poses ni personas**, solo diferencias de imagen. Por eso **un cambio de luz también puede activarlo**.

## Cómo está hecho
1. **Preparación**: se importa `/componentes/erp-tarjeta.js` (registra la etiqueta), se crea un `canvas` de 160×120 (pequeño = rápido) y un `video` invisible.
2. **`mostrarProducto()`**: `tarjeta.titulo = p.name` y `tarjeta.valor = p.stock`.
3. **`bucle()`** (se repite ~60 veces/segundo con `requestAnimationFrame`):
   - Dibuja el fotograma en el canvas **volteado** (espejo) y lo lee con `getImageData`. Resultado: una lista de números **R,G,B,A por cada píxel (4 números por píxel)**.
   - **`for (i = 0; i < foto.length; i += 4)`**: avanza **de 4 en 4** (un píxel por vuelta).
   - **Brillo** = R + G + B. Si `|brillo − brilloAntes| > 90`, ese píxel "ha cambiado".
   - **Columna** = `(i/4) % 160` (`i/4` = nº de píxel; `% 160` = posición dentro de su fila). Si `< 80` → izquierda, si no → derecha.
   - Si un contador **supera 600** y **ya terminó el bloqueo** (`performance.now() > bloqueadoHasta`): derecha `actual = (actual+1) % n`; izquierda `actual = (actual-1+n) % n`.
   - Actualiza la tarjeta, **bloquea 1000 ms** y guarda **`foto.slice()`** (una **copia**) para la próxima vuelta.
4. **`iniciar()`**: carga productos, pide permiso de cámara con `getUserMedia`, enciende el vídeo y arranca el bucle.

## Conceptos clave
- **El módulo `%`** hace que al pasar del último vuelva al primero. Para retroceder se suma `n` antes (`-1 % 3` daría negativo).
- **Bloqueo de 1 s**: sin él cambiaría de producto 20 veces con un solo gesto.
- **`slice()` sin argumentos = copia**. Sin copia, "la foto anterior" y "la actual" serían el mismo objeto y siempre saldría "sin cambios".
- **Umbral 600**: si la cámara es muy sensible, se sube; si no detecta, se baja.

## Errores que controla
Cámara denegada (`NotAllowedError`), sin cámara (`NotFoundError`), servidor caído, **lista de productos vacía** (no se accede a `productos[0]`) y doble pulsación (se desactiva el botón para no lanzar dos bucles).

## Preguntas del profesor
- **¿Por qué se avanza de 4 en 4?** Cada píxel ocupa 4 posiciones (R, G, B, A).
- **¿Por qué el efecto espejo?** Para que tu izquierda sea la izquierda de la pantalla.
- **¿Por qué 160×120?** Menos píxeles = menos cálculos = va fluido.
- **¿Por qué el bloqueo?** Evitar cambios repetidos con un solo movimiento.
- **¿Se puede engañar?** Sí: cualquier cambio fuerte de luz o de imagen cuenta como movimiento. No reconoce brazos.
- **¿Por qué `foto.slice()`?** Para guardar una copia y compararla en la siguiente vuelta.

---

# 4. E4 · Estado de pedido con la mano (`e4-mano-pedidos.html`)

**Para qué sirve:** usar la mano como **puntero sin contacto** (útil en entornos donde no se puede tocar la pantalla: cocinas, quirófanos, kioscos).
**Estructura del código (en orden):** ① elementos y variables (`pinzaAntes`, `manos`) → ② `prepararManos` (MediaPipe) → ③ `cambiarEstado` (PUT) y botones con `data-e` → ④ `bucle` (punto, pinza y clic) → ⑤ `iniciar` (cámara).
**Archivos que toca:** `evaluables/e4-mano-pedidos.html`. **Usa:** MediaPipe (CDN), `/api/orders` y `Ayudas`.

## Qué pide
Cuatro botones (Pendiente, Pagado, Enviado, Cancelado). Con la mano: **mover el dedo índice** mueve un punto rojo y **juntar índice y pulgar (pinza)** "pulsa" el botón que haya debajo. Cada botón cambia el estado del **pedido 1** en el servidor.

## Comparación
Es un **ratón hecho con tu mano**: el índice es el cursor y la **pinza es el clic**.

## Cómo está hecho
1. **MediaPipe** (`prepararManos`) se descarga solo al activar la cámara (pesa). Devuelve **21 puntos** por mano: **`m[8]` = punta del índice, `m[4]` = punta del pulgar**. Cada punto tiene `x` e `y` entre 0 y 1.
2. **Mover el punto**: `x = (1 − m[8].x) · ancho` y `y = m[8].y · alto`. El **`1 −`** compensa el espejo del vídeo.
3. **Pinza**: `Math.hypot(difX, difY) < 0.05` (distancia entre dedos por Pitágoras; menos de 0.05 = juntos).
4. **Un clic por pinza**: solo se pulsa si **`pinza && !pinzaAntes`** (ahora hay pinza y antes no). Si no, mantener los dedos juntos pulsaría 60 veces por segundo.
5. **Qué se pulsa**: `document.elementFromPoint(caja.left + x, caja.top + y)` da el elemento bajo el punto (coordenadas de **pantalla**, por eso se suma la posición del recuadro). Solo se hace `click()` si **es un `BUTTON` y `zona.contains(elemento)`** (así nunca pulsa botones de fuera, como "Activar cámara").
6. **Si desaparece la mano**: se esconde el punto y `pinzaAntes = false` (si no, al volver la mano contaría como pinza falsa).
7. **Cada botón** lleva `data-e` (su estado). Al hacer clic (con ratón **o** con pinza) se envía `PUT /api/orders?id=1` con `{status: estado}` mediante `Ayudas.escribir` y se muestra el resultado o el error.

## Conceptos clave
- **Flanco** (`pinza && !pinzaAntes`): reaccionar solo al **instante del cambio**, no mientras dure.
- **`pointer-events: none`** en el punto (en el CSS): por eso `elementFromPoint` ve el botón y no el punto.
- **PUT** = modificar un registro que ya existe.
- Se guarda `ultimoFrame` para no analizar dos veces el mismo fotograma (MediaPipe falla si se le repite).

## Errores que controla
Cámara denegada/inexistente, fallo al cargar MediaPipe (por ejemplo sin internet), mano que desaparece, error de red o HTTP en el PUT (se muestra), y pinza fuera de los botones (no hace nada).

## Preguntas del profesor
- **¿Qué son `m[8]` y `m[4]`?** Puntas del índice y del pulgar entre los 21 puntos.
- **¿Por qué `1 − x`?** El vídeo está en espejo.
- **¿Para qué `pinzaAntes`?** Para que una pinza sea UN clic y no cientos.
- **¿Por qué compruebas `BUTTON` y `zona.contains`?** Seguridad: solo se pulsan los botones del ejercicio.
- **¿Y el PUT?** Modifica el pedido 1; el servidor guarda el nuevo `status` en `db.json`. Se comprueba en `/api/orders`.
- **¿Por qué se prueba primero con ratón?** Para separar fallos: si el ratón funciona, el problema de la mano está en la cámara, no en el PUT.

---

# 5. E5 · Componente `erp-insignia` (`erp-insignia.js` + `e5-componente-insignia.html`)

**Para qué sirve:** crear **piezas reutilizables** de interfaz. Una vez hecha la insignia, se usa en cualquier página con una sola etiqueta, siempre igual y con su propio aspecto y comportamiento.
**Estructura del código (en orden):** ① constantes de estados y colores → ② clase `ErpInsignia` (`observedAttributes`, `attributeChangedCallback`, constructor con el clic, `get/set estado`, `connectedCallback`, `pintar`) → ③ `customElements.define` → ④ en la página E5, escuchar el evento `cambio`.
**Archivos que toca:** `componentes/erp-insignia.js` y `evaluables/e5-componente-insignia.html`.

## Qué pide
Crear **nuestra propia etiqueta HTML**, `<erp-insignia>`, que muestra el estado de un pedido como un botón de color y **cambia de estado al hacer clic**.

## Comparación
Es una **luz de semáforo personalizada**: la colocas donde quieras (`<erp-insignia estado="paid">`) y ella sola sabe dibujarse, cambiar de color y avisar de que ha cambiado.

## Cómo está hecho (el componente)
- **Es una clase** que `extends HTMLElement` y se registra con `customElements.define('erp-insignia', ErpInsignia)`.
- **Shadow DOM** (`attachShadow`): su HTML y CSS quedan aislados.
- **`observedAttributes` → `['estado']`** y **`attributeChangedCallback()`** llama a `pintar()`: si cambia el atributo, se redibuja sola.
- **`get estado` / `set estado`**: el getter devuelve el atributo **si es válido**; si no existe o es inválido (`xyz`) devuelve **`pending`**. El setter escribe el atributo (y eso dispara el repintado).
- **`pintar()`**: dibuja un `<button>` con el **color y texto de su estado** (Pendiente naranja, Pagado verde, Enviado azul, Cancelado rojo). Se usa `<button>` y no `<span>` porque así se puede usar con **teclado** (Tab + Enter): accesibilidad.
- **Un único listener de clic**: se registra en el **constructor** (que se ejecuta **una vez** por insignia). Cada clic avanza al siguiente estado `(posición + 1) % 4` (tras `cancelled` vuelve a `pending`) y lanza el evento propio.
- **`CustomEvent('cambio', { detail: { estado }, bubbles: true, composed: true })`**: `detail` lleva el dato; `bubbles` hace que suba por los padres; `composed` hace que **atraviese el Shadow DOM**.

## Cómo está hecho (la página E5)
Hay 3 insignias. `document.addEventListener("cambio", …)` escucha a **todas** (gracias a `bubbles` + `composed`), averigua cuál fue con `evento.target` y añade un `<li>` al log con `textContent`. Un botón extra **quita y vuelve a insertar** la primera insignia para probar que no se duplican eventos.

## Conceptos clave
- **Constructor vs `connectedCallback`**: el constructor se ejecuta una vez; `connectedCallback` cada vez que el elemento **entra** en la página (también al reinsertarlo). Si el listener estuviera en `connectedCallback` sin protección, **al reinsertar se duplicaría** (un clic avanzaría 2 estados).
- **Propiedad vs atributo**: `t.estado = "paid"` ↔ `<erp-insignia estado="paid">`. El setter los mantiene sincronizados.
- **`composed: true`** es imprescindible: sin él, el evento se quedaría atrapado dentro del Shadow DOM.

## Errores que controla
Estado ausente o inválido → `pending`; reinsertar sin duplicar eventos; texto del log con `textContent`.

## Preguntas del profesor
- **¿Qué es un Custom Element?** Una etiqueta HTML propia, creada con una clase y registrada con `customElements.define` (el nombre debe llevar un guion).
- **¿Para qué `observedAttributes`?** Para que el navegador avise (`attributeChangedCallback`) cuando cambie ese atributo.
- **¿Qué es el Shadow DOM?** Una zona interna aislada: el CSS de la página no afecta al componente ni al revés.
- **¿Por qué `bubbles` y `composed`?** Para que el evento suba por la página y salga del Shadow DOM.
- **¿Por qué el listener en el constructor?** Para que haya uno solo por componente aunque se quite y se vuelva a poner.
- **¿Cómo vuelve de `cancelled` a `pending`?** Con el módulo: `(3 + 1) % 4 = 0`.

---

# 6. E6 · Pruebas, documentación y paquete (`e6-pruebas-paquete.html`)

**Para qué sirve:** asegurar que el componente **funciona hoy y seguirá funcionando mañana** (pruebas), que **otros sepan usarlo** (documentación) y que se pueda **distribuir fácilmente** (paquete).
**Estructura (en orden):** ① importar `test` e `igual` y el componente → ② función `crear` → ③ las 7 pruebas → ④ `README.md` → ⑤ `npm run empaquetar` → ⑥ `paquete.html`.
**Archivos que toca:** `evaluables/e6-pruebas-paquete.html`, `componentes/README.md`, `componentes/paquete.html` y `dist/erp-componentes.js` (generado).

## Qué pide
1. **Pruebas automáticas** del componente. 2. **Documentarlo**. 3. **Empaquetarlo** en un solo fichero.

## Comparación
Las pruebas son **un inspector de calidad**: cada una prueba un caso y dice **OK** o **FALLA** sin que tengas que comprobarlo a mano cada vez.

## 1) Pruebas (7)
`test(nombre, función, lista)` ejecuta la función; si no lanza error → OK, si lanza → FALLA. `igual(real, esperado)` lanza error si no son iguales. **Cada test crea su propia insignia** (`crear()`), la inserta y comprueba un caso:
1. Sin atributo → `pending`.
2. `estado="paid"` → `paid`.
3. `estado="xyz"` → `pending`.
4. Un clic: `pending` → `paid` **y** el evento `cambio` trae `detail.estado = "paid"`.
5. (añadida) **Ciclo completo**: 4 clics → `paid, shipped, cancelled, pending`.
6. (añadida) **Cambio visual**: el HTML interno antes y después del clic es distinto.
7. (añadida) **Reconectar** (quitar y volver a insertar) y hacer clic: **un solo evento** y un solo cambio de estado.

Resultado comprobado en el navegador: **7 de 7 OK**.

## 2) Documentación
`public/componentes/README.md`: tabla de propiedades (`estado`, defecto `pending`, valores posibles), el ciclo de estados, el evento `cambio` y su `detail`, un **ejemplo HTML** y dónde están las pruebas.

## 3) Paquete
`npm run empaquetar` ejecuta `tools/empaquetar.js`: **une todos los componentes** (`erp-tarjeta.js` + `erp-insignia.js`, sin el de pruebas) en **`public/dist/erp-componentes.js`**. La página `componentes/paquete.html` carga **solo ese fichero** y comprueba con `customElements.get(...)` que **las dos etiquetas existen** (resultado: `erp-tarjeta: OK | erp-insignia: OK`).

## Conceptos clave
- **Prueba unitaria**: probar una pieza pequeña aislada.
- **No cargar a la vez paquete y fuentes originales**: `customElements.define` con el mismo nombre dos veces da error.
- En `erp-insignia.js` las constantes tienen nombre propio (`ESTADOS_INSIGNIA`) porque al juntar ficheros en uno **comparten el mismo ámbito** y podrían chocar.

## Preguntas del profesor
- **¿Por qué cada test crea su propia insignia?** Para que no se influyan entre sí.
- **¿Qué hace `igual`?** Compara con `!==` y lanza un error con "esperaba X pero salió Y".
- **¿Qué ventaja tiene empaquetar?** Una sola petición/etiqueta `<script>` en lugar de varias.
- **¿Qué pasa si cargas paquete y originales?** Error "ya está registrado" (el nombre se define dos veces).
- **¿Por qué documentar?** Para que otra persona use el componente sin leer el código.

---

# 7. E7 · Proyecto integrado (`e7-proyecto-integrado.html`)

**Para qué sirve:** ver cómo **se combinan** todas las técnicas en una aplicación real: datos del servidor, voz, componente propio y cámara con la mano (realidad aumentada simple).
**Estructura del código (en orden):** ① variables `pedidos`, `visibles`, `i` → ② `pintarLista` y `cargar` → ③ `ejecutarOrden` (voz) → ④ `prepararManos` → ⑤ `ocultarTarjeta` y `bucle` (mano, tarjeta y pinza) → ⑥ `iniciar` (cámara).
**Archivos que toca:** `evaluables/e7-proyecto-integrado.html`. **Usa:** `erp-insignia` (E5), MediaPipe, `/api/orders` y `Ayudas`.

## Qué pide
Juntar lo anterior: una **lista de pedidos** (cliente, total, insignia), **voz** para filtrar (`pagados` / `todos`) y **mano** para ver una **tarjeta anclada a la mano** que cambia de pedido con la **pinza**.

**Aviso:** es una **superposición 2D anclada** (una tarjeta HTML que sigue un punto de la imagen), **no es WebXR ni seguimiento espacial 3D**. Es importante decirlo así en la defensa.

## Comparación
Es **un catálogo con asistente de voz y una "etiqueta flotante" pegada a tu mano**: dices "pagados" y la lista se filtra; enseñas la mano y aparece una etiqueta sobre ella; con la pinza pasas de un pedido a otro.

## Cómo está hecho
1. **Datos**: `pedidos` (original, **no se modifica**), `visibles` (lo que se ve ahora) e `i = 0` (índice de la tarjeta). Se cargan con `Ayudas.pedir("/api/orders")`.
2. **`pintarLista()`**: por cada pedido en `visibles`, crea una fila con `cliente - total EUR` (`textContent`, texto seguro) y un **`<erp-insignia>`** con su estado (`inert = true`: aquí solo informa; si se pudiera pulsar cambiaría solo en pantalla y no en el servidor). Si no hay pedidos: "No hay pedidos que mostrar".
3. **Voz** (`Ayudas.voz`): `pagados` → `pedidos.filter(p => p.status === "paid")`; `todos` → `visibles = pedidos`. **Tras filtrar `i = 0`**: si no, `i` podría apuntar a un pedido que ya no está en la lista.
4. **Cámara** con botón: `prepararManos()` (MediaPipe) + `getUserMedia`.
5. **`bucle()`**:
   - **Sin mano o sin pedidos** → `ocultarTarjeta()` (esconde la tarjeta y `pinzaAntes = false`). **No se accede a una lista vacía.**
   - **Con mano**: la tarjeta se coloca sobre **`m[9]`** (el centro de la mano) con espejo: `left = (1 − m[9].x) · ancho`, `top = m[9].y · alto`.
   - Muestra **`visibles[i % visibles.length]`** (el `%` impide salirse de la lista).
   - **Pinza** (`hypot(m[8], m[4]) < 0.05`) y `!pinzaAntes` → `i++` (pasa de abierta a cerrada = un solo avance).

## Conceptos clave
- **Dos listas** (`pedidos` y `visibles`): el filtro no destruye los datos.
- **Reiniciar `i`** al filtrar.
- **`i % visibles.length`**: índice circular y seguro.
- **Resetear `pinzaAntes`** cuando desaparece la mano.
- **Combinar sin variables globales duplicadas**: todo está en un único `<script type="module">`, cada variable declarada una sola vez.

## Errores que controla (el enunciado pide probarlos por separado)
| Caso | Qué pasa |
|---|---|
| Lista | Se carga y pinta; si el servidor falla, mensaje rojo |
| Voz | `pagados` / `todos`; otra frase → "No he entendido" |
| Filtro sin resultados | "Sin resultados", tarjeta oculta, sin errores |
| Mano | Tarjeta anclada a `m[9]` |
| Pinza | Avanza un pedido por pinza |
| Mano desaparece | Tarjeta oculta y pinza reseteada |
| Permiso de cámara denegado | Mensaje explicando cómo permitirlo |

## Preguntas del profesor
- **¿Es realidad aumentada?** Es una versión **simple 2D**: información digital superpuesta a la imagen. **No** es WebXR ni seguimiento 3D.
- **¿Por qué `m[9]`?** Es un punto central de la mano (la base del dedo corazón), estable para anclar la tarjeta.
- **¿Por qué dos variables `pedidos` y `visibles`?** Para filtrar sin perder los datos originales.
- **¿Por qué `i = 0` tras filtrar?** Evita apuntar fuera de la lista nueva.
- **¿Qué evita `i % visibles.length`?** Salirse de la lista y acceder a `undefined`.
- **¿Qué pasa con 0 resultados?** Se oculta la tarjeta y no se lee `visibles[...]`.
- **¿Qué es `inert`?** Atributo que hace que el elemento no responda a clics ni teclado: así las insignias de la lista solo informan.

---

# 8. Cómo probarlo todo (Chrome)
1. Abre una terminal en la carpeta `interfaces-alumnado` y ejecuta `npm install` (solo la primera vez) y `npm run dev`.
2. Abre `http://localhost:3000` y entra en cada ejercicio desde el índice (usa **Chrome o Edge**).
3. **E1/E2/E7 (voz)**: permite el micrófono, pulsa **Hablar** y di la frase (una por pulsación).
4. **E3/E4/E7 (cámara)**: permite la cámara; buena luz y fondo quieto. E4 y E7 necesitan internet la primera vez (descargan MediaPipe).
5. **E5/E6**: no necesitan nada; E6 debe mostrar 7 líneas verdes.
6. **Paquete**: `npm run empaquetar` y abre `http://localhost:3000/componentes/paquete.html`.
7. **Prueba de errores**: deniega un permiso, apaga el servidor y repite una acción, prueba una frase absurda.
8. **Entrega**: tu HTML, `componentes/erp-insignia.js`, `componentes/README.md` y una captura de cada prueba. **No incluyas `node_modules`.**

> Nota: E4 modifica el pedido 1 en `data/db.json` cuando lo pruebas de verdad (es lo que pide el enunciado). Si quieres dejarlo como estaba, vuelve a poner el pedido 1 en `paid` con el botón "Pagado".

