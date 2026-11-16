# Guía de defensa — UMBRA (diseño y accesibilidad)

> Léela en orden. Todo lo marcado como **MEDIDO** lo calculé yo sobre las 15 pantallas exportadas de Figma (`pantallas/` = tu diseño original; `pantallas_accesible/` = versión mejorada). Lo marcado como **HECHO** está aplicado en tu archivo de Figma "**Sin título**" (comprobado en las capturas de `pantallas_ia/`, que son la versión final). Lo que no se ha llegado a hacer está recogido como **FUTURAS IMPLEMENTACIONES** (secciones 4 y 7).

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
- ✅ **HECHO (más seguridad):** al pulsar Confirmar, una hoja inferior pregunta "¿Votar por *Innerbloom*?" con el texto "Solo puedes votar una vez y no podrás cambiarlo." y los botones **Votar** y **Cancelar** (pantalla nueva "Confirmar voto"). Así un toque accidental no cuenta.

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
| 5 | Iconos de la barra inferior (Clasificación y Perfil) rotos en varias pantallas | Se veían incompletos. ✅ **HECHO**: ahora salen completos en todas las pantallas revisadas | 1.1.1 / calidad del diseño |
| 6 | Contador "Caduca en 01:59" del QR | No se explicaba cómo ampliarlo. ✅ **HECHO**: botón "Generar otro código" y texto "El código se renueva solo." | 2.2.1 Tiempo ajustable |
| 7 | Votar es definitivo con un solo toque de confirmación | ✅ **HECHO**: hoja "¿Votar por Innerbloom?" con Votar / Cancelar | 3.3.4 Prevención de errores |
| 8 | Sin estado de foco | ✅ **HECHO**: marco "Estado de foco" con anillo lima en el botón principal y en una canción. ⏳ **Futuro**: orden de lectura, textos alternativos y etiquetas de lector de pantalla (sección 5, se programan en la app) | 2.4.7, 1.3.1, 4.1.2 |
| 9 | Solo tema oscuro | ✅ **HECHO en parte**: tema claro en 4 pantallas (Inicio, Selección, Confirmación del voto y Clasificación). ⏳ **Futuro**: el resto | (mejora, no es fallo AA) |
| 10 | Mezcla de idiomas: "Check-in", "Coins", "Clubber", "Sessions" | ✅ **HECHO en parte**: "Check-in" → "Entrada/Registro de entrada". ⏳ **Futuro**: "Upgrade", "Coins", "Clubber" y "Sessions" siguen en inglés | 3.1.2 Idioma de las partes |

Nota: en el Panel DJ no pude medir los contrastes de "Sin marcar" y "Duración" (las capturas son de otro tamaño). Compruébalos tú en Figma con un plugin de contraste.

---

## 4. Mejoras: qué está hecho y qué falta

### ✅ HECHO (aplicado en tu Figma "Sin título"; comprobado en las capturas de `pantallas_ia/`)

**Primera tanda (cambios hechos a mano, medidos):**

| # | Cambio | Antes → Después (MEDIDO) |
|---|---|---|
| 1 | Gris secundario de todos los textos | `#9DA69E` → **`#C9D0CB`**. Subtítulo de "La noche": 3,25:1 → **4,96:1**. En el resto de pantallas medidas: 7 – 10,7:1 |
| 2 | Números de posición de la clasificación (y bordes que usaban ese gris) | `#69746B` → **`#9DA69E`**: 4,0:1 → **7,78:1** |
| 3 | Degradado verde del fondo | Opacidad **20 % → 12 %** (fondo algo más oscuro, texto más legible) |
| 4 | Botones "Canjear" (3) | Alto **32 pt → 44 pt** (tamaño táctil recomendado) |

**Segunda tanda (hecha con la IA de Figma; la he revisado sobre las capturas de `pantallas_ia/`):**

| # | Cambio | Qué se ve en el diseño |
|---|---|---|
| 5 | Barra inferior | Iconos de **Clasificación** y **Perfil** completos y con el mismo trazo en las pantallas revisadas (Check-in, Votación cerrada, Perfil…) |
| 6 | QR que caduca | En "Canjear recompensa": botón **"Generar otro código"** (borde lima) encima de "Cerrar" y el texto "El código se renueva solo." |
| 7 | Confirmar voto | Pantalla nueva **"Confirmar voto"**: hoja inferior "¿Votar por Innerbloom?", "Solo puedes votar una vez y no podrás cambiarlo.", botones **Votar** (lima) y **Cancelar** (borde lima) |
| 8 | Foco visible | Marco nuevo **"Estado de foco"** con anillo lima de 3 pt en el botón "Elegir mi canción" y en una canción seleccionada, y el texto "Estado de foco: visible al navegar con teclado." |
| 9 | Tamaños de texto | Notas y textos de ayuda claramente más grandes en las pantallas móviles (por ejemplo, el nombre del artista y las notas del pie) |
| 10 | Idioma | "FRECUENCIA / CHECK-IN" → **"FRECUENCIA / REGISTRO DE ENTRADA"**; "Check-in temprano completado: +Bonus activo" → **"Entrada temprana registrada: bonificación activa"** |
| 11 | Tema claro (4 pantallas) | Variantes claras de **Inicio, Selección, Confirmación del voto y Clasificación** (fondo #F5F7F0, texto #4A524C, botón principal #2F6B12) |

**Contrastes de lo nuevo (MEDIDO):**

| Elemento | Contraste |
|---|---|
| Tema claro: texto gris #4A524C sobre #F5F7F0 | **7,47:1** |
| Tema claro: texto gris sobre tarjeta blanca | **8,07:1** |
| Tema claro: texto blanco sobre botón verde #2F6B12 | **6,5:1** |
| Tema claro: etiqueta verde #2F6B12 sobre #F5F7F0 | **6,02:1** |
| Hoja de confirmar voto: texto #F5F7F0 sobre #151816 | **16,56:1** |
| Hoja de confirmar voto: "Cancelar" lima sobre #151816 | **14,96:1** |
| Números de posición (oscuro) #9DA69E | **7,78:1** |
| Nota del pie (oscuro) #C9D0CB sobre fondo oscuro | **10,33:1** |

Todo lo medido cumple AA (mínimo 4,5:1).

### ⏳ FUTURAS IMPLEMENTACIONES (no se han llegado a hacer)

| # | Qué falta | Por qué / cómo |
|---|---|---|
| A | **Tema claro en el resto de pantallas** (Perfil, Rangos, Check-in, Canjear, estados, Panel DJ) | Solo hay 4 de las 15. Faltan 11 más el Panel DJ |
| B | **Cambio automático claro/oscuro** | Que la app siga el ajuste del móvil; hoy son diseños separados |
| C | **Idioma completo** | Siguen en inglés "Upgrade a copa premium", "Coins", "Clubber" y "Sessions" |
| D | **Panel DJ** | No se han verificado ni ajustado sus tamaños de texto y contrastes ("Sin marcar", "Duración") |
| E | **Tamaño de texto escalable al 200 %** | Es una regla de programación de la app (Dynamic Type / escala de fuente); en Figma solo se puede indicar |
| F | **Lector de pantalla** | Orden de lectura, etiquetas y textos alternativos (sección 5): se programan en la app real |
| G | **"Reducir movimiento"** | Respetar el ajuste del sistema en barras de carga y transiciones |
| H | **Deshacer el voto durante 10 segundos** | Para errores motrices; es una decisión de negocio (hoy el voto es definitivo) |
| I | **Estado de foco en todos los componentes** | Se ha mostrado en 2 ejemplos; falta definirlo en el resto (opciones, pestañas, botones secundarios) |
| J | **Enlazar el flujo en el prototipo** | Que "Elegir mi canción" → "Confirmar voto" → "Voto confirmado" sea navegable (no verificado) |

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
| ¿Tu diseño cumple AA? | Detecté 10 fallos o carencias (sección 3) y he corregido la mayoría: contrastes, botones táctiles, iconos, confirmación del voto, QR, foco e idioma. Lo que queda está en "futuras implementaciones". Lo medido cumple AA. |
| ¿Por qué no basta el color? | Hay daltonismo y poca luz. Por eso la selección lleva check, borde y texto. |
| ¿Qué tamaño mínimo de botón? | 44×44 pt recomendado (Apple/Google); 24×24 px es el mínimo de WCAG 2.2. |
| ¿Cómo lo prueba un ciego? | Con TalkBack (Android) o VoiceOver (iPhone): se navega por el orden de lectura y las etiquetas. |
| ¿Y una persona mayor? | Texto escalable hasta 200 %, botones grandes, mensajes claros. |
| ¿Por qué hay tema oscuro y claro? | El oscuro es el principal porque el contexto es una discoteca. He añadido un tema claro en 4 pantallas para quien lo prefiera (baja visión, mucha luz). Contrastes medidos entre 6 y 8:1. |
| ¿Qué cambiarías con más tiempo? | Tema claro en todas las pantallas y en el Panel DJ, idioma completo, deshacer el voto 10 segundos, estado de foco en todos los componentes y programar el lector de pantalla (sección 7). |

---

## 7. Estado real (importante, léelo antes de defender)

- ✅ **Hecho en tu Figma "Sin título":** 4 mejoras medidas (colores de texto, números de posición, degradado, botones "Canjear") y, con la IA de Figma, las demás: iconos de la barra, "Generar otro código", hoja de confirmar voto, estado de foco, tamaños de texto, idioma ("Check-in") y tema claro en 4 pantallas. Lo he **revisado sobre capturas** (carpeta `pantallas_ia/`).
- 🖼️ **Carpetas de capturas:** `pantallas/` = diseño original · `pantallas_accesible/` = tras la primera tanda · `pantallas_ia/` = versión final con todo lo nuevo.
- 🔙 **Cómo volver atrás:** en Figma, menú del archivo → **Mostrar historial de versiones**; ahí puedes recuperar el estado anterior.
- ℹ️ Además hay un ajuste de exportación (PNG 2x) en los marcos para sacar las capturas; no afecta al diseño.
- ⏳ **Futuras implementaciones (no hechas):** ver la tabla de la sección 4 (A–J): tema claro en el resto de pantallas, cambio automático claro/oscuro, idioma completo, Panel DJ, texto escalable al 200 %, lector de pantalla, "reducir movimiento", deshacer el voto, foco en todos los componentes y enlazar el prototipo. Preséntalas como **trabajo futuro**: es lo normal en un diseño.
- ⚠️ **Limitación de mi revisión:** los tamaños de texto los he comprobado **a ojo** sobre las capturas (se ven más grandes), no sé el tamaño exacto en puntos. Mide el más pequeño en Figma (panel Texto) y apúntalo para la pregunta 5.
- ⚠️ **Dato corregido:** en una versión anterior de esta guía dije que el contraste llegaba a 2,06:1 en el punto más claro del degradado. En ese punto solo hay titulares blancos (4,8:1). El fallo real era el subtítulo gris (3,25:1) y ya está arreglado.
- ⚠️ **No medí todo:** en el Panel DJ no pude medir "Sin marcar" y "Duración" (la captura tenía otro tamaño). Compruébalos tú con un plugin de contraste.
- Las respuestas 5–8 de las preguntas que te hice las rellené yo con datos del diseño; **revísalas y cámbialas por tus palabras**, el profesor notará si no las entiendes. La del tamaño de letra (5) es una **estimación**.
