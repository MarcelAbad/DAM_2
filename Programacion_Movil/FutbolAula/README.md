# Fútbol Aula — aplicación Android Kotlin

## Qué contiene este ZIP
Aplicación base + 16 archivos Kotlin de ejemplos + 16 archivos Kotlin evaluables + documentación por ejercicio + pruebas locales e instrumentadas. Las carpetas ejemplos/ y evaluables/ contienen exactamente 16 .kt cada una. Los demás .kt son infraestructura o tests, no ejercicios adicionales.

## Estado de verificación (edición 2.1)
Esta edición se ha compilado de verdad y se han ejecutado sus pruebas automáticas en un entorno real con JDK 17, Gradle 8.9 y el SDK de Android 35: `assembleDebug` compila sin errores y `testDebugUnitTest` se ejecuta por completo. El detalle exacto, caso por caso, está en INFORME_VALIDACION.md. No se incluye APK: généralo con `./gradlew assembleDebug`. Las pruebas que necesitan un móvil real (cámara, micrófono, Bluetooth entre dos equipos, sensores) siguen siendo manuales; el propio botón Comprobar te recuerda cuáles.

## Ejecutar
1. Instala Android Studio, JDK 17 y SDK 35.
2. Abre esta carpeta (contiene settings.gradle.kts).
3. Versiones fijadas: AGP 8.7.3, Kotlin 2.0.21, Gradle 8.9. No se presentan como las últimas versiones.
4. Esta edición incluye el Gradle Wrapper completo (gradlew, gradlew.bat y gradle/wrapper). No necesitas instalar Gradle: solo JDK 17 y el SDK de Android.
5. Sincroniza con conexión a Internet y elige un emulador o móvil API 26 o superior. Pulsa Run.
6. Compila con `./gradlew assembleDebug` (Windows: `gradlew.bat assembleDebug`).
7. APK debug: app/build/outputs/apk/debug/app-debug.apk. Instala en móvil con `adb install -r app/build/outputs/apk/debug/app-debug.apk`.
8. Para publicar usa una versión firmada y no entregues claves en el repositorio.

## Probar
- Solo base y ejemplos: `./gradlew testDebugUnitTest --tests 'es.aula.futbol.BaseTest' --tests '*.E*Test.ejemploResuelto'`.
- Solo un ejercicio: `./gradlew testDebugUnitTest --tests 'es.aula.futbol.E02Test'`.
- Todo: `./gradlew testDebugUnitTest`.
- Con emulador/móvil: `./gradlew connectedDebugAndroidTest`.
- Scripts de ayuda en scripts/. No sustituyen instalar las herramientas.
- Informe JUnit: app/build/reports/tests/testDebugUnitTest/index.html.

Hay 64 métodos @Test locales en ALUMNO (54 originales más 10 de CheckerTest, que comprueba que el verificador funciona) y 66 en PROFESOR. Los 54 originales son: 16 de ejemplos, 32 de contratos/límites de evaluables y 6 de base; los nuevos pertenecen a CheckerTest. Hay 3 métodos instrumentados: 2 de persistencia y 1 de arranque de interfaz (requieren emulador o móvil). En ALUMNO, los 32 tests de evaluables deben fallar inicialmente por TODO; no los borres ni los marques ignorados para aparentar éxito.

## Uso y navegación
Catálogo inicial de 16 futbolistas: busca, filtra por club y favoritos, abre ficha y guarda anotaciones. Menú: Academia, equipos, comparar, alineación, colecciones, copia, datos sin conexión, cuestionario, panel y recepción Bluetooth.

Las acciones básicas siguen resueltas. Las acciones E01–E16 llaman al correspondiente módulo evaluable; si está pendiente muestran un aviso y la app sigue utilizable. Academia permite leer cada enunciado dentro de la app, ejecutar el ejemplo X y probar el evaluable E. La interfaz y la infraestructura de hardware se entregan hechas para concentrar la evaluación en las reglas y su integración.

## Comprobar cada ejercicio en la propia app
Cada ejercicio tiene en Academia un botón "Comprobar E##" que ejecuta la función real del alumno con casos normales, casos límite y casos que deben rechazarse, y muestra el resultado con colores: verde CORRECTO, rojo INCORRECTO (con el detalle de qué caso falla y por qué) y gris PENDIENTE (TODO sin resolver). El botón "Comprobar los 16 ejercicios" da un resumen general. Los ejercicios con hardware implicado (E05 agitar, E07 persistir imagen, E08 grabar/reproducir, E15 Bluetooth) muestran además una nota con la prueba que solo puedes hacer en un móvil real. Checker.kt es Kotlin puro: no usa JUnit ni Android, y su lógica es la misma que los tests E*Test, así que el resultado del botón y el de `./gradlew testDebugUnitTest` coinciden.

## Capacidades conservadas y ampliadas
- Clases de modelos y repositorio, SQLite, favoritos/notas y borrado confirmado.
- Búsqueda y filtros, menús/alertas, compartir texto.
- HTTPS oficial (HTML) y catálogo JSON remoto con estado/caché.
- Acelerómetro, sorpresa y alternativa sin sensor.
- Bluetooth clásico SPP: envío sencillo y colección bidireccional con ACK, cancelación y límite de espera.
- Cámara externa de miniatura, selector de imágenes, gris y contraste reversible.
- PNG/JPEG reales, imagen persistente por jugador y cromo compartido mediante proveedor privado con permiso temporal.
- Audio propio, pista de audio de vídeo, pausa/fin/errores y playlist.
- Grabación de voz por jugador con permiso, indicador, guardado explícito y descarte al salir.
- Once 4-3-3, colecciones, backup de notas/favoritos, cuestionario y panel de recuentos personales.
- Animación, documentación RA2/RA3 y continuación libre.

## Datos y derechos
FUENTES.md distingue selección base, Athletic y selección histórica del Atlético. No son plantillas completas ni una API en directo. Hay 16 fichas iniciales y dos lotes de referencia de 5 para practicar ampliación. Los nuevos lotes se cargan desde Añadir equipos y dependen de E01. Sin fotografías ni estadísticas inventadas: el avatar usa iniciales. Las valoraciones son personales y las imágenes/audios importados no se verifican por identidad o licencia.

## Permisos
INTERNET: sin diálogo. BLUETOOTH_CONNECT en API 31+ al usar comunicación con emparejados; no hay escaneo. RECORD_AUDIO al grabar. La cámara la maneja otra aplicación, por lo que no se declara CAMERA. Los selectores conceden acceso a lo elegido sin permisos generales de almacenamiento.

## Dispositivos y límites
Bluetooth: móvil con clásico SPP y otro equipo compatible emparejado desde ajustes; auriculares no son un servidor SPP genérico. Emisor y receptor deben compartir UUID y formato; los IDs deben existir en ambos. ACK confirma validación, después el usuario decide importar. No hay autenticación adicional propia del protocolo; no enviar datos privados. Cancelar cierra sockets; los límites son 20 s envío/30 s recepción.

La cámara devuelve miniatura, no foto completa. Se reproduce audio, no superficie de vídeo. Al pausar se libera audio y descarta grabación no guardada; no hay reproducción de fondo ni audio focus completo. Una nota sin guardar se pierde al reconstruir; se restauran consulta/club/favoritos/jugador, no todos los controles temporales. La imagen reaparece solo si E07 la ha guardado. Las exportaciones del laboratorio sobrescriben nombres por ID. El original reversible es el cargado al abrir/importar, no un historial de edición.

Backup v1 contiene solo favorito/nota y se aplica con confirmación en transacción; no incluye medios, catálogo, etiquetas o colecciones y no los borra. El cuestionario no persiste una partida en curso. La alineación guarda cada cambio como borrador y puede necesitar ampliar defensas. La cache remota sustituye el catálogo visible, no realiza una fusión de fichas. Solo se admiten endpoints HTTPS JSON autorizados; no confundir con HTML oficial. SQLite y algunos filtros/dibujos trabajan en UI para este tamaño de datos: medir y pasar a repositorio asíncrono si se amplía. Las peticiones/imágenes usan un ejecutor serial que no se cancela inmediatamente al salir; las descargas grandes deben mejorarse con lectura limitada y cancelación. Se usa API clásica startActivityForResult, disponible pero antigua. Revisar accesibilidad y pruebas reales antes de uso docente.

## Material didáctico
Empieza en docs/EMPIEZA_AQUI.md. E01.md a E16.md: objetivo, pasos, límites, checklist, pistas y entrega. docs/COBERTURA_PROMPTS.md explica la agrupación sin eliminar capacidades. RA_Y_EVIDENCIAS.md relaciona cada criterio. CONTINUA_TU_APP.md permite un producto propio. No se exige rapidez ni una estética concreta.
