# Alcance de la revision
Se ejecutan pruebas Node reales de sintaxis, enlaces locales, presencia de enunciados completos, funciones puras de voz/filtros/movimiento/pinza y contratos de componentes con DOM minimo.
Las plantillas evaluables conservan TODO: pasar npm test no significa haber resuelto los ejercicios.
No se ha ejecutado npm install, Next.js ni un navegador grafico en este entorno. No se ha comprobado visualmente el layout en movil. CSS sin altura maxima ni recorte en enunciados y etiquetas de codigo escapadas reducen riesgos, pero no sustituyen inspeccion visual.
Pendiente antes de clase: instalar dependencias, arrancar ambos ZIP, visitar todas las paginas en Chrome/Edge a 320, 768 y 1280 px y zoom 200%; verificar ultima linea de cada enunciado; permitir/denegar camara y voz; comprobar modelos CDN. Las pruebas unitarias no garantizan hardware ni servicios externos.
Next 14.2.15 se conserva del proyecto anterior para continuidad, NO se ha auditado su seguridad. Uso docente local, no desplegar en Internet. Actualizar a una version mantenida y probar antes de despliegue.
La camara usa comparacion de pixeles en R3 y seguimiento de mano en R4/R7. La superposicion 2D no equivale a WebXR.
