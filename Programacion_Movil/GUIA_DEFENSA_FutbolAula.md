# Guía de defensa — Proyecto final Programación Móvil (Fútbol Aula)

> Lee esto en orden. Cada paso explica **qué hice, por qué y qué decir** en la defensa.

---

## PARTE 0 · Cómo abrir el proyecto (Android Studio)

En tu PC **no está instalado Android Studio**, así que hay que descargarlo.

1. Descarga **Android Studio** (gratis): https://developer.android.com/studio → botón "Download Android Studio". Pesa ~1 GB.
2. Instálalo con todo por defecto (Next, Next…). Ya trae el **JDK 17** dentro, no hace falta instalar Java aparte.
3. Al abrirlo la primera vez, deja que descargue el **Android SDK** (asistente "Standard"). Tarda un rato y necesita Internet.
4. En Android Studio: **File → Open** y elige la carpeta:
   `C:\Users\Marcel\Desktop\DAM 2\DAM_2\Programacion_Movil\FutbolAula`
   (la carpeta que contiene `settings.gradle.kts`, NO la carpeta `app`).
5. Espera a que abajo termine "Gradle sync" (la primera vez descarga cosas, 5-15 min). Si pide instalar "Android SDK Platform 35", pulsa **Install**.
6. Para ejecutar: arriba elige un móvil virtual (**Device Manager → Create Device**, por ejemplo Pixel con API 34/35) o conecta tu móvil con depuración USB, y pulsa el triángulo verde **Run ▶**.
7. Para pasar los tests: abajo a la izquierda abre una **Terminal** y escribe:
   ```
   gradlew.bat testDebugUnitTest
   ```
   Informe: `app/build/reports/tests/testDebugUnitTest/index.html`.

**Importante:** yo no he podido compilar ni ejecutar nada aquí (no hay Android SDK en este PC). El código está revisado a mano contra los tests, pero **tienes que ejecutar los tests tú** antes de entregar y apuntar el resultado real.

---

## PARTE 1 · Qué es el proyecto (lo primero que dices)

"Es una app Android en Kotlin de **fichas de futbolistas**: catálogo con búsqueda, favoritos, notas, comparador, alineación, colecciones, copia de seguridad, cuestionario, panel y recepción por Bluetooth. La app ya venía hecha; mi trabajo fue **implementar 16 funciones de reglas** (E01–E16) que estaban vacías con un `TODO`, sin cambiar ninguna firma."

Dónde está mi trabajo: `app/src/main/java/es/aula/futbol/evaluables/` (16 archivos).
Dónde están los ejemplos resueltos que consulté: carpeta `ejemplos/` (X01–X16).
Cómo se comprueba: tests `E01Test…E16Test` y botón **"Comprobar E##"** dentro de la app (pantalla Academia).

Idea clave para toda la defensa: **cada función valida la entrada con `require(...)`**. Si algo es inválido lanza `IllegalArgumentException` (rechazo controlado) y la app muestra un aviso en vez de romperse.

---

## PARTE 2 · Demo en vivo (orden recomendado, 5 min)

1. Abre la app → catálogo de 16 jugadores.
2. Busca "mbappe" sin tilde → sale Mbappé (E02).
3. Abre una ficha → pon etiquetas "Pase, pase" y valoración 6 → se rechaza; con 4 → se guarda sin duplicar (E03).
4. Menú **Academia → Comprobar los 16 ejercicios** → todo en verde (los de hardware avisan de prueba manual).
5. Ejecuta los tests en terminal y enseña el informe en verde.

---

## PARTE 3 · Los 16 ejercicios, uno a uno

Para cada uno: **qué hace → cómo → qué decir si te preguntan**.

### E01 · Más equipos (`addTeam`)
- **Qué hace:** añade un lote de jugadores de un club al catálogo.
- **Cómo:** rechaza lote vacío, dos clubes mezclados, posición no válida, fuente que no sea `https`, ID repetido y el mismo jugador (mismo club y nombre sin tildes) con otro ID. Si todo va bien devuelve `current + incoming`.
- **Pregunta típica:** *¿Por qué `TextKey.of`?* → para que "Éder" y "Eder" cuenten como el mismo nombre.

### E02 · Buscar, filtrar y ordenar (`filter`)
- **Qué hace:** filtra por texto, club, posición y favoritos a la vez, y ordena por nombre.
- **Cómo:** normaliza el texto con `TextKey`, aplica todas las condiciones con `&&` (AND), ordena con `sortedBy` y, si es descendente, hace `.reversed()`.
- **Pregunta típica:** *¿Y si no hay resultados?* → devuelve lista vacía, no da error.

### E03 · Etiquetas y valoración (`mark`)
- **Qué hace:** guarda hasta 5 etiquetas únicas y una valoración 0–5.
- **Cómo:** `trim`, quita vacías, quita repetidas ignorando tildes/mayúsculas (`distinctBy`), rechaza >5 etiquetas, etiquetas >24 caracteres y valoración fuera de 0–5. Usa `copy(...)` para **no perder favorito ni nota**.
- **Pregunta típica:** *¿Qué es `copy`?* → crea un objeto nuevo igual al anterior cambiando solo lo que indico (los `data class` son inmutables).

### E04 · Comparador (`compare`)
- **Qué hace:** compara de 2 a 4 jugadores.
- **Cómo:** `require(size in 2..4)`, `require` de IDs distintos, y una línea por jugador `nombre | club | posición`. No inventa ganador ni estadísticas.

### E05 · Sorpresa (`choose`)
- **Qué hace:** elige un jugador "al azar" de la lista visible al agitar el móvil.
- **Cómo:** si no hay candidatos devuelve `null`; si hay, usa `Math.floorMod(index, size)` para que cualquier índice (incluso negativo o enorme) caiga en una posición válida.
- **Pregunta típica:** *¿Por qué `floorMod` y no `%`?* → porque `-1 % 16` da `-1` (posición inexistente) y `floorMod(-1,16)` da `15`.
- **Prueba en móvil:** agitar con un filtro activo y ver que la ficha sale del filtro.

### E06 · Contraste (`parameters`)
- **Qué hace:** da los dos números para la matriz de color: escala y desplazamiento.
- **Cómo:** valida que sea un número entre 0 y 3 (rechaza `NaN`) y devuelve `(value, 128·(1−value))`. Con contraste 1 el desplazamiento es 0 (imagen igual); con 0 es 128 (gris).
- **Reversible:** la pantalla aplica el filtro a una copia y conserva el original.

### E07 · Imagen por jugador (`filename`)
- **Qué hace:** da el nombre seguro del archivo de imagen: `jugador_7.png`.
- **Cómo:** ID > 0 y extensión solo `png` o `jpg`. Así `../x` (intento de salir de la carpeta privada) se rechaza.
- **Prueba en móvil:** importar imagen, "conservar", cerrar y reabrir la app: reaparece solo en esa ficha.

### E08 · Playlist de audios (`next`)
- **Qué hace:** calcula el audio anterior/siguiente.
- **Cómo:** dirección solo −1 o 1; sin audios devuelve `null`; índice actual dentro de rango; y recorrido **circular** con `floorMod` (tras el último vuelve al primero).

### E09 · Alineación 4-3-3 (`validate`)
- **Qué hace:** dice si un once es válido.
- **Cómo:** exactamente 11 jugadores, 11 IDs distintos y reparto 1 portero / 4 defensas / 3 centrocampistas / 3 delanteros (cuento posiciones con `groupingBy…eachCount`). Devuelve `Boolean` (no lanza error).

### E10 · Colecciones (`add`)
- **Qué hace:** valida el nombre de una colección nueva.
- **Cómo:** `trim`, longitud 1–40, y rechaza si ya existe otro equivalente ignorando tildes/mayúsculas. Devuelve la lista anterior + el nombre limpio.

### E11 · Copia de seguridad (`restore`)
- **Qué hace:** restaura favoritos y notas desde un JSON.
- **Cómo:** `Wire.decode` valida **todo** antes de tocar nada (si no es JSON lanza excepción). Parto de los datos actuales (lo que no está en la copia **se conserva**) y la copia sobrescribe favorito y nota; etiquetas y valoración se mantienen.

### E12 · Datos sin conexión (`resolve`)
- **Qué hace:** decide qué lista se muestra.
- **Cómo:** si `remote != null` y no hay error → son datos actuales (`stale=false`), **aunque vengan vacíos**. Si falla la descarga → se usan los guardados con `stale=true`. Mensaje distinto si no hay nada guardado.
- **Idea clave:** distinguir "respuesta vacía válida" de "error".

### E13 · Cuestionario (`build`)
- **Qué hace:** crea 5 preguntas "¿Dónde juega X?".
- **Cómo:** rechaza cantidad ≤ 0, IDs repetidos, menos jugadores que preguntas y menos de 2 clubes. Una pregunta por jugador distinto, 2 opciones distintas (club correcto + otro club) y la respuesta siempre está entre las opciones. Alterno el orden para que la correcta no esté siempre primera.

### E14 · Cromo (`title`)
- **Qué hace:** valida el título del cromo para compartir.
- **Cómo:** color 0–2, `trim` y longitud 1–28. Devuelve el título limpio.

### E15 · Bluetooth (`decode`)
- **Qué hace:** lee el mensaje JSON recibido con la lista de IDs.
- **Cómo:** máx. 8192 bytes, JSON válido, `version == 1`, lista `ids` no vacía de enteros, sin repetidos y todos conocidos. Devuelve los IDs en el orden recibido. Un JSON mal formado lanza `JSONException`, también un rechazo controlado.
- **Prueba en móvil:** necesita dos móviles emparejados (no lo puedo probar en el emulador).

### E16 · Panel (`counts`)
- **Qué hace:** cuenta jugadores por club.
- **Cómo:** `distinctBy { id }` (un repetido cuenta una vez) → `groupingBy { club }.eachCount()` → `toSortedMap()` (orden alfabético). Lista vacía → mapa vacío.

---

## PARTE 4 · Preguntas que pueden hacerte

- **¿Qué es `require`?** Comprueba una condición; si es falsa lanza `IllegalArgumentException` con un mensaje.
- **¿Qué es `TODO()`?** Una función que lanza error "no implementado"; el comprobador la marca como PENDIENTE (gris). Yo lo sustituí por la lógica.
- **¿Por qué `object`?** Es un singleton: no hace falta crear instancias, se llama `E02Filtros.filter(...)`.
- **¿Qué es un `data class`?** Clase para guardar datos; da `copy`, `equals` y `toString` automáticos (`Player`, `Mark`).
- **¿Cómo sabes que funciona?** Tests JUnit `E01Test…E16Test` (contrato, ejemplo, caso límite) y el botón *Comprobar E##*, que ejecutan los mismos casos.
- **¿Qué no se puede probar automáticamente?** Agitar (E05), guardar imagen (E07), grabar/reproducir (E08) y Bluetooth (E15): se prueban a mano en un móvil.
- **¿Cambiaste firmas o IDs?** No. Solo sustituí el cuerpo de cada función.

---

## PARTE 5 · Qué entregar (según el enunciado)

Por cada ejercicio: el `.kt` con tu solución y comentarios, **una captura del caso correcto y otra de una validación**, el **resultado real de los tests** (indica lo que no pudiste ejecutar) y cinco líneas: qué hiciste, qué error encontraste y cómo lo comprobaste.

Plantilla de las cinco líneas (cámbiala con tus palabras):
1. Implementé `<función>` en `E##<Nombre>.kt` sin cambiar la firma.
2. Valida `<reglas>` con `require`.
3. Error que encontré: `<p. ej. -1 % 16 daba -1; lo arreglé con floorMod>`.
4. Lo comprobé con `E##Test` y con el botón Comprobar E## en Academia.
5. Resultado: `<verde / lo que falle>`.

> Antes de la defensa: abre el proyecto, ejecuta `gradlew.bat testDebugUnitTest` y haz las capturas. Si algún test falla, dime el mensaje y lo arreglo.
