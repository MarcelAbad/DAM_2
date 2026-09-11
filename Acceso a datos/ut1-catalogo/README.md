# UT1 · Catálogo de productos

Proyecto del equipo para la unidad 1. Se trabaja **a lo largo de toda la unidad**:
después de cada bloque de teoría se desbloquea un TODO.

## Importar en IntelliJ

`File > Open` y selecciona el fichero **`pom.xml`** de esta carpeta (no la carpeta).
IntelliJ lo reconoce como proyecto Maven y descarga las dependencias solo.
La primera vez tarda un poco.

## Cómo se trabaja

1. Después de cada bloque de teoría, completa el TODO que toque en
   `src/main/java/com/thepower/ut1/catalogo/GestorCatalogo.java`.
2. Lanza los tests (`mvn test`, o el botón de play verde en IntelliJ).
3. Cuando el test de ese TODO esté en verde, escribe tu línea del cuaderno.

Los tests se llaman **TODO 1**, **TODO 2**... para que veas de un vistazo por dónde vas.

| TODO | Qué implementa | Teoría |
|---|---|---|
| 1 | `inicializar()` | Path y Files |
| 2 | `existeFicheroTexto()` / `existeFicheroXml()` | Path y Files |
| 3 | `guardarTexto()` | Streams de escritura |
| 4 | `cargarTexto()` | Streams de lectura |
| 5 | `guardarXmlConJaxb()` | Binding XML |
| 6 | `leerXmlConDom()` | Parsers XML |
| 7 | `cargarCatalogoRobusto()` | Excepciones de E/S |

Cada TODO tiene sus tests **independientes**: puedes tener el 3 en verde aunque
no hayas hecho el 1. No hace falta ir en orden estricto, aunque se recomienda.

No cambies las firmas de los métodos ni los nombres de las clases: los tests
dependen de ellas. `Producto.java` y `Catalogo.java` ya están completos.

## JAXB

No viene en el JDK desde Java 11. Ya está añadido en el `pom.xml`
(`jaxb-api` + `jaxb-runtime`): no tienes que tocar nada.
