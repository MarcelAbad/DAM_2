# RA2 y RA3: trazabilidad
La implementación es una oportunidad de evaluación, no una certificación automática.

| Criterio | Código/actividad | Evidencia |
|---|---|---|
| RA2 a | Modelos, Store, MediaArt, BluetoothLink, MainActivity, 32 módulos | Diagrama y explicación |
| RA2 b | Activity, menú, alertas, controles y pantallas | Recorrido e interacción |
| RA2 c | SPP sencillo y E15 bidireccional | Recepción en hardware real |
| RA2 d | Acelerómetro/E05 | Movimiento, ausencia y ciclo de vida |
| RA2 e | HTTPS HTML y JSON/E12 | Respuesta, timeout y estado HTTP |
| RA2 f | SQLite y ficheros/E07/E10/E11 | Persistencia tras reinicio |
| RA2 g | Emulador, tests y checklist | Optimización medida antes/después |
| RA2 h | Compilar/instalar APK | Móvil real con modelo/API |
| RA2 i | README, enunciados y comentarios | Reproducción por otra persona |
| RA2 j | Manifest, micrófono y Bluetooth | Concesión, denegación y mínimos |
| RA3 a | Comparar Android Studio/SDK con otro entorno elegido | Informe razonado de formatos, edición, reproducción, licencias, hardware y despliegue con fuentes oficiales |
| RA3 b | Cámara externa, BitmapFactory, grabación y archivos | Captura y archivo legible |
| RA3 c | Bitmap.compress PNG/JPEG | Entrada de un formato y salida realmente codificada en el otro |
| RA3 d | Canvas/ColorMatrix/E06 | Original y resultado |
| RA3 e | MIME, eventos, excepciones, reproducción | Cancelación, error y fin |
| RA3 f | Animación de giro | Evento e interrupción |
| RA3 g | MediaPlayer y playlist | Inicio, pausa, fin y liberación |
| RA3 h | Comentarios, tests, depuración | Fallo reproducido, corrección y regresión |

La base usa HTTPS y códigos HTTP, no conexiones http:// sin cifrar. Si el docente exige una conexión HTTP sin cifrar, usar servidor local de aula y excepción limitada al dominio, sin datos sensibles. No habilitar tráfico globalmente.

RA3 a requiere una actividad del alumno: comparar otro entorno y justificar la elección. No se considera superado por abrir Android Studio.


## Evidencia adicional automática (edición 2.1)
El botón Comprobar de Academia y CheckerTest ejecutan los mismos contratos que los tests: su resultado verde es evidencia ejecutable de la lógica de cada criterio de la tabla que dependa de E01–E16. La comprobación automática cubre la lógica; las filas marcadas con "hardware real", "móvil real" o "emulador" siguen exigiendo evidencia manual documentada (captura, grabación o firma del docente), y el propio comprobador lo recuerda en pantalla para E05, E07, E08 y E15.
