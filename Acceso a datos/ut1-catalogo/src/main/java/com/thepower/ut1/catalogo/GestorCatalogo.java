package com.thepower.ut1.catalogo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo de productos persistido en fichero de texto y en XML.
 * Unidad 1 · Persistencia en ficheros (RA1).
 *
 * Hay 7 TODO, uno por bloque de teoría. Después de cada bloque completas
 * el TODO que toca, lanzas los tests y escribes tu línea en el cuaderno.
 *
 * NO cambies las firmas de los métodos (nombre, parámetros, tipo devuelto):
 * los tests dependen de ellas tal cual están.
 */
public class GestorCatalogo {

    private final Path carpetaDatos;
    private final Path ficheroTexto;
    private final Path ficheroXml;

    public GestorCatalogo(Path carpetaDatos) {
        this.carpetaDatos = carpetaDatos;
        this.ficheroTexto = carpetaDatos.resolve("productos.txt");
        this.ficheroXml = carpetaDatos.resolve("productos.xml");
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 1 · Crear la carpeta de datos          (teoría: Path y Files)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Crea la carpeta de datos si todavía no existe.
     * Si ya existe, no debe fallar: se puede llamar dos veces seguidas.
     *
     * Pista: Files.createDirectories(...)
     */
    public void inicializar() throws IOException {
        //throw new UnsupportedOperationException("TODO 1 sin implementar");

        Files.createDirectories(carpetaDatos);
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 2 · Comprobar si los ficheros existen  (teoría: Path y Files)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Devuelve true si el fichero de texto existe. No lo crea ni lo toca.
     *
     * Pista: Files.exists(...)
     */
    public boolean existeFicheroTexto() {

        //throw new UnsupportedOperationException("TODO 2 sin implementar");

        return Files.exists(ficheroTexto);

    }


    /**
     * Igual que el anterior, pero para el fichero XML.
     */
    public boolean existeFicheroXml() {
        //throw new UnsupportedOperationException("TODO 2 sin implementar");

        return Files.exists(ficheroXml);
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 3 · Guardar en texto            (teoría: streams de escritura)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Guarda la lista de productos en productos.txt, un producto por línea,
     * con el formato "id;nombre;precio", en UTF-8.
     *
     * Cada vez que se guarda, el fichero queda solo con estos productos:
     * no se acumulan los de la llamada anterior.
     *
     * Pista: Files.newBufferedWriter con StandardOpenOption.CREATE y
     * TRUNCATE_EXISTING, dentro de un try-with-resources. Y newLine().
     */
    public void guardarTexto(List<Producto> productos) throws IOException {
        throw new UnsupportedOperationException("TODO 3 sin implementar");
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 4 · Leer de texto                (teoría: streams de lectura)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Lee productos.txt y reconstruye la lista de productos.
     * Si el fichero todavía no existe, devuelve una lista vacía
     * (no lanza excepción).
     *
     * Pista: Files.readAllLines(...) y String.split(";")
     */
    public List<Producto> cargarTexto() throws IOException {
        throw new UnsupportedOperationException("TODO 4 sin implementar");
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 5 · Guardar en XML con JAXB              (teoría: binding XML)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Guarda la lista de productos como productos.xml, usando JAXB
     * (JAXBContext + Marshaller), envuelta en un Catalogo.
     *
     * Pista: JAXBContext.newInstance(Catalogo.class), createMarshaller(),
     * la propiedad JAXB_FORMATTED_OUTPUT a true, y marshal(...).
     */
    public void guardarXmlConJaxb(List<Producto> productos) throws JAXBException {
        throw new UnsupportedOperationException("TODO 5 sin implementar");
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 6 · Leer XML con un parser DOM           (teoría: parsers XML)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Lee productos.xml con un parser DOM —NO con JAXB— y reconstruye la
     * lista recorriendo a mano los elementos <producto>.
     *
     * Pista: DocumentBuilderFactory.newInstance(), newDocumentBuilder(),
     * parse(...), getElementsByTagName("producto") y, dentro de cada uno,
     * getElementsByTagName("id").item(0).getTextContent()
     *
     * Nota: al probar el TODO 7 veréis en consola una línea "[Fatal Error] ...".
     * NO es un fallo vuestro: la imprime el propio parser cuando le llega el XML
     * roto que el test le pasa a propósito. Si os molesta, se quita con
     * builder.setErrorHandler(...) — buscad ErrorHandler en la documentación.
     */
    public List<Producto> leerXmlConDom() throws Exception {
        throw new UnsupportedOperationException("TODO 6 sin implementar");
    }

    // ═══════════════════════════════════════════════════════════════
    // TODO 7 · Carga robusta                    (teoría: excepciones E/S)
    // ═══════════════════════════════════════════════════════════════
    /**
     * Carga el catálogo de la forma más segura posible:
     *   1. Si existe el XML, lo intenta leer.
     *   2. Si el XML no existe o está mal formado, cae en el fichero de texto.
     *   3. Si tampoco hay texto, devuelve una lista vacía.
     *
     * Este método NUNCA lanza una excepción hacia fuera: fíjate en que no
     * declara "throws". Todo error de E/S se captura aquí dentro.
     */
    public List<Producto> cargarCatalogoRobusto() {
        throw new UnsupportedOperationException("TODO 7 sin implementar");
    }
}
