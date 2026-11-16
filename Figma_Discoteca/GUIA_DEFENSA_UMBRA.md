# Guía de defensa — UMBRA (diseño y accesibilidad)

> Léela en orden. Todo lo marcado como **MEDIDO** lo calculé yo sobre las 15 pantallas exportadas de Figma (`pantallas/` = tu diseño original; `pantallas_accesible/` = versión mejorada). Lo marcado como **HECHO** está aplicado en tu archivo de Figma "**Sin título**" (y también en la copia "Sin título (Copia)"). Lo marcado como **PENDIENTE** no está hecho todavía (ver sección 7).

---

## 1. Qué es UMBRA (cómo empezar la defensa)

"UMBRA es una app móvil para **discotecas** donde el público **vota qué canción prefiere** de una selección oficial de 6 temas. El DJ ve los votos en un **panel de cabina** y decide qué suena: los votos **orientan**, no obligan. Hay un voto por persona y noche. Participar da **Frecuencia Coins**, que se canjean por recompensas (2×1 en barra, copa premium, acceso anticipado) y suben de **rango** (Regular → VIP Member)."

**Modelo de negocio (lo que tú dijiste):** la app se vende a las discotecas para que puedan montar una sesión donde el público escoge la música.

**15 pantallas:**

| Grupo | Pantallas |
|---|---|
| Flujo principal | Inicio de la noche · Selección oficial para votar · Confirmación del voto · Clasificación de la noche |
| Comunidad | Perfil del Clubber · Rangos · Check-in (QR del local) · Canjear recompensa |
| Estados | Cargando · Sin conexión · Sin noche activa · Votación cerrada · Ya has votado |
| Extras | Notificación push · Panel DJ (versión escritorio) |

Idea a destacar: **diseñaste los estados raros** (sin conexión, cargando, votación cerrada). Mucha gente solo diseña el camino feliz.

---

## 2. Decisiones de diseño (respuestas para el profesor)

**1. ¿Por qué tema oscuro y verde lima?**
- La sesión de prueba es de **techno / melodic house**, y esa estética (negro, fotos de láseres y humo, neón) es la identidad visual del género.
- Una discoteca es un sitio **oscuro**: un fondo blanco deslumbra y llama la atención de los demás. El tema oscuro cansa menos la vista y gasta menos batería con pantalla OLED.
- El **verde lima** es el color de acento porque **destaca muchísimo** sobre negro (MEDIDO: 11,3:1) y marca siempre lo importante: botones principales, estado "en curso" y elemento seleccionado.

**2. ¿Quién usa la app y en qué condiciones?**
- Gente **joven que sale de fiesta**, de noche, con **poca luz**, **ruido**, **una mano ocupada** (con la copa), a veces con prisa y **sin posibilidad de oír** avisos sonoros.
- Consecuencias de diseño: botones **grandes**, **poco texto**, **una acción principal por pantalla**, mensajes cortos. Todo esto ya lo cumple en general.
- Hay otro usuario: el **DJ**, que usa el panel en una pantalla grande en la cabina.

**3. ¿Cómo sabe una persona ciega qué canción tiene seleccionada?**
Hoy el diseño marca la selección con **borde lima, check y el texto "1 seleccionada / Tu elección: Innerbloom"**. Eso ayuda a mucha gente (no depende solo del color), pero un lector de pantalla necesita además **etiquetas programadas**. En la app real:
- Cada canción es un botón de opción (*radio*) con etiqueta completa: "Innerbloom, RÜFÜS DU SOL, 9 minutos 38 segundos, seleccionada, opción 3 de 6".
- Al elegir, un aviso hablado: "Has elegido Innerbloom".
- El texto "Tu elección: Innerbloom" se anuncia en una zona *live region*.
- (Ver sección 5, anotaciones pantalla a pantalla.)

**4. ¿Qué pasa si alguien pulsa "Confirmar mi voto" sin querer?**
- Ahora mismo: **el voto es definitivo**. Ya hay una protección: primero se **elige** y luego se **confirma** (dos pasos).
- **PROPUESTA (más seguridad):** al pulsar Confirmar, una hoja inferior pregunta "¿Votar por *Innerbloom*? No podrás cambiarlo" con **Votar** y **Cancelar**. Así un toque accidental no cuenta.

**5. ¿Qué tamaño de letra mínimo usas y por qué?**
- Completa este dato con lo que veas en Figma: selecciona el texto más pequeño (por ejemplo "votos" en la clasificación) y mira el tamaño en el panel derecho.
- Mi **estimación** al medir las capturas @2x: etiquetas pequeñas 12–13 pt, notas 14–15 pt, títulos de pantalla unos 32 pt. **Esto es una estimación, compruébalo.**
- Recomendación para defender: **mínimo 12 pt para etiquetas y 14–16 pt para texto de lectura**, porque en una discoteca se lee con poca luz y de pie. Y la app real debe respetar el tamaño de texto que el usuario ponga en el sistema (hasta el 200 %).

**6. ¿Por qué el voto es irreversible?**
- **Integridad del resultado:** si se pudiera cambiar, un grupo podría manipular la clasificación votando y desvotando.
- **Justicia:** un voto por persona, todos los rangos cuentan igual (lo repite la propia app).
- **Honestidad con el usuario:** se avisa **antes** ("Un voto por noche. No podrás cambiarlo") y **después** ("Ya has votado").
- **Mejora posible (accesibilidad):** una ventana de **deshacer de 10 segundos** tras votar, pensando en errores motrices. Es una decisión de negocio que puedes proponer.

**7. ¿Cómo evitas que el degradado verde dificulte la lectura?**
Lo medí y en el diseño original **no se cumplía**: el subtítulo gris de "La noche" (#9DA69E) sobre el verde (#1E5A3C) daba **3,25:1** (mínimo exigido 4,5:1). Lo arreglé de dos maneras (**HECHO**):
1. Subí el gris secundario a **#C9D0CB** en todas las pantallas → el subtítulo pasa a **4,96:1**.
2. Bajé la opacidad del degradado verde del **20 % al 12 %**, para que el fondo sea algo más oscuro.

Así el texto sobre el degradado cumple AA en todas las pantallas que medí (mínimo 4,96:1).

**8. Diferencias entre la app y el Panel DJ**

| | App del público | Panel DJ |
|---|---|---|
| Dispositivo | Móvil, vertical, una mano | Pantalla grande o portátil, horizontal |
| Objetivo | Votar, ver clasificación, canjear | Ver votos en directo y **marcar** cada tema (Sonada / Descartada), **cerrar la votación** |
| Información | Poca, una cosa por pantalla | Mucha: tabla de 6 canciones, totales, registro de cabina |
| Mensaje clave | "Tu voto orienta al DJ" | "La clasificación es de votos, no una cola de reproducción: tú decides" |

---

## 3. Auditoría de accesibilidad (MEDIDO)

Norma de referencia: **WCAG 2.2 nivel AA**. Contraste mínimo: **4,5:1** para texto normal y **3:1** para iconos, bordes y texto muy grande.

### Lo que está bien ✅
| Elemento | Medida |
|---|---|
| Texto oscuro sobre botón lima ("Elegir mi canción", "Confirmar mi voto") | **14,9:1** |
| Texto lima sobre fondo oscuro (etiquetas, "Votación abierta") | **11,3:1** |
| Gris secundario (#9DA69E) sobre fondos oscuros sólidos | 7,1 – 7,8:1 |
| Borde del radio sin marcar (#69746B) | 3,67:1 (≥ 3:1 ✔ para componentes) |
| Selección: no depende solo del color | borde + check + texto ✔ |
| Pestaña activa: color + negrita + icono | ✔ |
| Estados vacíos y de error diseñados (cargando, sin conexión, sin noche, cerrada) | ✔ |

### Fallos ❌
| # | Problema | Dato | Norma |
|---|---|---|---|
| 1 | Subtítulo gris sobre el degradado verde de "La noche" | **3,25:1** (#9DA69E sobre #1E5A3C). ✅ **HECHO** → 4,96:1 | 1.4.3 Contraste |
| 2 | Verde del degradado en su punto más claro (#2C7B53) | Allí solo hay titulares blancos (4,8:1 ✔), pero es el punto más delicado. ✅ **HECHO** (opacidad 20 % → 12 %) | 1.4.3 |
| 3 | Números de posición "02–06" | 4,0:1 (gris #69746B sobre #0B0D0C). ✅ **HECHO** → 7,78:1 | 1.4.3 |
| 4 | Botones "Canjear" del perfil | 32 pt de alto (recomendado ≥ 44 pt; mínimo WCAG 2.2: 24 px). ✅ **HECHO** → 44 pt | 2.5.8 Tamaño del objetivo |
| 5 | Iconos de la barra inferior (Clasificación y Perfil) rotos en varias pantallas | Se ven incompletos (un óvalo y tres rayas sueltas) | 1.1.1 / calidad del diseño |
| 6 | Contador "Caduca en 01:59" del QR | No se explica cómo ampliarlo | 2.2.1 Tiempo ajustable |
| 7 | Votar es definitivo con un solo toque de confirmación | Sin cuadro de confirmación | 3.3.4 Prevención de errores |
| 8 | Sin anotar: estado de foco, orden de lectura, textos alternativos, etiquetas de lector de pantalla | — | 2.4.7, 1.3.1, 4.1.2 |
| 9 | Solo tema oscuro | No hay tema claro | (mejora, no es fallo AA) |
| 10 | Mezcla de idiomas: "Check-in", "Coins", "Clubber", "Sessions" | — | 3.1.2 Idioma de las partes |

Nota: en el Panel DJ no pude medir los contrastes de "Sin marcar" y "Duración" (las capturas son de otro tamaño). Compruébalos tú en Figma con un plugin de contraste.

---

## 4. Mejoras: qué está hecho y qué falta

### ✅ HECHO (aplicado en tu Figma "Sin título" y en la copia; comprobado en las capturas de `pantallas_accesible/`)

| # | Cambio | Antes → Después (MEDIDO) |
|---|---|---|
| 1 | Gris secundario de todos los textos | `#9DA69E` → **`#C9D0CB`**. Subtítulo de "La noche": 3,25:1 → **4,96:1**. En el resto de pantallas medidas: 7 – 10,7:1 |
| 2 | Números de posición de la clasificación (y bordes que usaban ese gris) | `#69746B` → **`#9DA69E`**: 4,0:1 → **7,78:1** |
| 3 | Degradado verde del fondo | Opacidad **20 % → 12 %** (fondo algo más oscuro, texto más legible) |
| 4 | Botones "Canjear" (3) | Alto **32 pt → 44 pt** (tamaño táctil recomendado) |

### ⏳ PENDIENTE (no lo he hecho; está definido para que lo apliques o lo expliques)

| # | Cambio | Qué hacer |
|---|---|---|
| 5 | Barra inferior | Rehacer los iconos **Clasificación** y **Perfil** en las pantallas donde salen incompletos (Votación cerrada, Ya has votado, Sin noche activa, Sin conexión, Check-in, Rangos) con el mismo trazo que en "La noche" |
| 6 | Caduca en 01:59 | Añadir botón **"Generar otro código"** y texto "El código se renueva solo" |
| 7 | Confirmar voto | Añadir hoja de confirmación: "¿Votar por Innerbloom? No podrás cambiarlo" → **Votar / Cancelar** |
| 8 | Foco visible | Añadir estado de foco: anillo lima de 3 pt y 2 pt de separación (WCAG 2.4.7 / 2.4.11) |
| 9 | Tamaños de texto | Mínimo 12 pt en etiquetas; 14 pt en notas; permitir hasta 200 % |
| 10 | Idioma | "Check-in" → "Registro de entrada"; "Coins" → "monedas" (o mantener "Frecuencia Coins" como nombre propio) |
| 11 | Movimiento | En la app: respetar "Reducir movimiento" del sistema |

---

## 5. Anotaciones de lector de pantalla (TalkBack / VoiceOver)

Orden de lectura y etiquetas, pantalla a pantalla:

| Pantalla | Orden y etiquetas |
|---|---|
| **Inicio de la noche** | 1) Título "UMBRA" (encabezado nivel 1) · 2) "Noche en curso" (estado) · 3) Tarjeta del evento: "Frecuencia, melodic techno y house, sábado 10 de octubre de 2026, de 23:00 a 06:00" · 4) "Votación abierta hasta las 02:00. 6 canciones oficiales. Un voto por persona" · 5) Botón "Elegir mi canción" · 6) Barra: "La noche, seleccionada, pestaña 1 de 4" |
| **Selección** | Grupo "Selección oficial, elige una canción". Cada canción = opción: "Innerbloom, RÜFÜS DU SOL, 9:38, opción 3 de 6, seleccionada". Aviso hablado al elegir. Botón "Confirmar mi voto" (desactivado hasta elegir, y dice por qué) |
| **Confirmación** | Al abrir, anunciar "Voto confirmado" (rol *alert*). Foco en el título. Imagen de portada **decorativa** (sin etiqueta) porque el título ya lo dice |
| **Clasificación** | Tabla con cabeceras: posición, canción, artista, votos. "Posición 1, Losing It, FISHER, 186 votos". Marcar "tu voto" con texto, no solo con color |
| **Perfil / Rangos** | Barra de progreso: "Progreso de rango, 850 de 1.000 puntos, 85 %". Cada recompensa: "2×1 en barra, 150 monedas, botón Canjear" |
| **Canjear (hoja)** | Foco atrapado dentro de la hoja; "Cerrar" devuelve el foco a "Canjear". El QR lleva la etiqueta "Código QR de canje" y también el código en texto para quien no pueda mostrarlo |
| **Cargando / Sin conexión** | "Cargando selección" (rol *status*). Error con rol *alert*: "Sin conexión. No se ha enviado ningún voto" |
| **Notificación push** | Texto completo en el título de la notificación; no depender de sonido ni vibración |
| **Panel DJ** | Tabla con cabeceras. Botones "Marcar Losing It como sonada" (no solo "Sonada", que repetido 6 veces no se distingue) |

---

## 6. Preguntas que puede hacer el profesor (con respuesta corta)

| Pregunta | Respuesta |
|---|---|
| ¿Qué es WCAG? | Pautas internacionales de accesibilidad web; el nivel AA es el estándar legal en la UE. |
| ¿Qué es el contraste y cómo se mide? | La relación de brillo entre texto y fondo. Se mide con una fórmula; AA exige 4,5:1 (texto) y 3:1 (iconos y bordes). |
| ¿Tu diseño cumple AA? | En su mayor parte sí, **excepto** 10 puntos detectados (sección 3). Tengo propuesta con valores para cada uno. |
| ¿Por qué no basta el color? | Hay daltonismo y poca luz. Por eso la selección lleva check, borde y texto. |
| ¿Qué tamaño mínimo de botón? | 44×44 pt recomendado (Apple/Google); 24×24 px es el mínimo de WCAG 2.2. |
| ¿Cómo lo prueba un ciego? | Con TalkBack (Android) o VoiceOver (iPhone): se navega por el orden de lectura y las etiquetas. |
| ¿Y una persona mayor? | Texto escalable hasta 200 %, botones grandes, mensajes claros. |
| ¿Por qué solo tema oscuro? | El contexto de uso es una discoteca; se documenta como decisión y el tema claro sería una mejora futura. |
| ¿Qué cambiarías con más tiempo? | Confirmación del voto, iconos de la barra, contraste del degradado, foco visible, tema claro. |

---

## 7. Estado real (importante, léelo antes de defender)

- ✅ **Hecho:** revisé las 15 pantallas, medí los contrastes y apliqué **4 mejoras** (colores de texto, números de posición, degradado, botones "Canjear") en tu archivo **"Sin título"** de Figma y también en la copia "Sin título (Copia)". Las capturas del antes y el después están en `pantallas/` (original) y `pantallas_accesible/` (mejorado).
- 🔙 **Cómo volver atrás:** en Figma, menú del archivo → **Mostrar historial de versiones**; ahí puedes recuperar el estado anterior. Las capturas de `pantallas/` guardan el diseño original.
- ℹ️ Además añadí a los marcos un ajuste de exportación (PNG 2x) para sacar las capturas; no afecta al diseño y puedes quitarlo en el panel Exportar.
- ⚠️ **PENDIENTE (7 cosas):** iconos rotos de la barra, "Generar otro código" en el QR, hoja de confirmación del voto, estado de foco, tamaños de texto, idioma y tema claro. No están en el Figma; están descritas en la sección 4 y en la 5 (etiquetas de lector de pantalla). Si te da tiempo, hazlas tú siguiendo la tabla; si no, defiéndelas como "mejoras previstas".
- ⚠️ **Dato corregido:** en una versión anterior de esta guía dije que el contraste llegaba a 2,06:1 en el punto más claro del degradado. En ese punto solo hay titulares blancos (4,8:1). El fallo real era el subtítulo gris (3,25:1) y ya está arreglado.
- ⚠️ **No medí todo:** en el Panel DJ no pude medir "Sin marcar" y "Duración" (la captura tenía otro tamaño). Compruébalos tú con un plugin de contraste.
- Las respuestas 5–8 de las preguntas que te hice las rellené yo con datos del diseño; **revísalas y cámbialas por tus palabras**, el profesor notará si no las entiendes. La del tamaño de letra (5) es una **estimación**.
