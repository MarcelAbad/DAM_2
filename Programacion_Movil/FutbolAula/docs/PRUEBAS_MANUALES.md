# Pruebas Android: pendientes de ejecutar
Registra fecha, modelo/API, pasos, esperado, observado, evidencia y estado. No marques superada una prueba sin hacerla.

1. Catálogo: búsqueda con tildes, club y lista sin resultados. Inicio sin controles tapados.
2. SQLite: guardar favorito/nota, forzar cierre, recuperar, editar y borrar con cancelación.
3. E02: filtros avanzados combinados y orden inverso.
4. E03: etiquetas duplicadas, valoración 6 y persistencia de una válida.
5. E04/E05: comparación sin repetir y selección solo entre resultados; sensor virtual y dispositivo sin sensor.
6. Cámara: captura/cancelación; importación grande/inválida y recuperar original después de filtro.
7. PNG/JPEG: extraer desde Device Explorer y comprobar codificación, no solo extensión.
8. E07: imagen de un jugador persistida sin cambiar otras fichas.
9. Audio: preparar, pausar, continuar, fin, medio inválido y salida de pantalla.
10. Micrófono: denegar permiso; después conceder, grabar, detener y guardar; salir durante grabación debe descartarla.
11. E08: dos audios, anterior circular, siguiente y eliminación.
12. E09: sustitución compatible, ausencia de duplicados, borrador y once completo.
13. E10: dos colecciones, mismo jugador en ambas, renombrar, duplicado y borrar sin afectar a otra.
14. E11: exportar/importar, JSON inválido, ID desconocido y cancelación; comprobar que etiquetas/valoración no desaparecen.
15. E12: endpoint JSON autorizado correcto, sin red, HTTP 500, JSON inválido y caché conservada. La web HTML oficial no es un endpoint de catálogo.
16. E13: cinco preguntas, una respuesta por pregunta, resultado y mejor resultado persistido.
17. E14: título vacío/largo, tres colores, compartir PNG y abrir desde otra app.
18. Bluetooth: dos equipos reales emparejados; envío simple y colección con ACK. Denegar permiso, apagar Bluetooth, cancelar, timeout y mensaje inválido. Documentar ambos extremos.
19. E16: cero favoritos y recuentos de dos clubes.
20. Rotación: catálogo y ficha; documentar qué se restaura y los límites listados en README.
21. Emulador: medir una mejora antes/después, no inventar tiempos.
22. APK instalada en móvil real: registrar modelo/API y repetir recorrido básico.

Depura con breakpoints en Store.save, task y onActivityResult. Registra al menos tres incidencias reproducibles con causa, corrección y regresión. Si no hay hardware, marca la prueba no ejecutada.
