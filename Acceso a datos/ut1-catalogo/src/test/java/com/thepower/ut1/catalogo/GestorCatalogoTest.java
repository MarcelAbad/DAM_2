package com.thepower.ut1.catalogo;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Un test por TODO. Cada uno es independiente: si no has hecho el TODO 1,
 * los tests del 3 en adelante siguen pudiendo ponerse en verde.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UT1 · Catálogo de productos")
class GestorCatalogoTest {

    Path carpeta;
    GestorCatalogo gestor;

    static final List<Producto> DEMO = List.of(
            new Producto(1, "Teclado mecánico", 39.90),
            new Producto(2, "Ratón óptico", 12.50)
    );

    static final String XML_DEMO = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <catalogo>
                <producto><id>1</id><nombre>Teclado mecánico</nombre><precio>39.9</precio></producto>
                <producto><id>2</id><nombre>Ratón óptico</nombre><precio>12.5</precio></producto>
            </catalogo>
            """;

    @BeforeEach
    void preparar() throws IOException {
        // La carpeta se crea aquí, no con inicializar(): así el TODO 1
        // no bloquea al resto de tests.
        carpeta = Files.createTempDirectory("ut1-catalogo-");
        gestor = new GestorCatalogo(carpeta);
    }

    @AfterEach
    void limpiar() throws IOException {
        if (carpeta != null && Files.exists(carpeta)) {
            try (var flujo = Files.walk(carpeta)) {
                flujo.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
            }
        }
    }

    // ───────────────────────────── TODO 1 ─────────────────────────────

    @Test @Order(1)
    @DisplayName("TODO 1 · crea la carpeta de datos, y llamarlo dos veces no falla")
    void todo1() throws IOException {
        Files.delete(carpeta);
        assertFalse(Files.exists(carpeta));

        gestor.inicializar();
        assertTrue(Files.exists(carpeta), "inicializar() debería haber creado la carpeta");

        assertDoesNotThrow(() -> gestor.inicializar(),
                "llamarlo con la carpeta ya creada no debe fallar");
    }

    // ───────────────────────────── TODO 2 ─────────────────────────────

    @Test @Order(2)
    @DisplayName("TODO 2 · dice si cada fichero existe, sin crearlo")
    void todo2() throws IOException {
        assertFalse(gestor.existeFicheroTexto());
        assertFalse(gestor.existeFicheroXml());

        Files.writeString(carpeta.resolve("productos.txt"), "1;Algo;1.0\n", StandardCharsets.UTF_8);
        assertTrue(gestor.existeFicheroTexto());
        assertFalse(gestor.existeFicheroXml(), "el XML sigue sin existir");
    }

    // ───────────────────────────── TODO 3 ─────────────────────────────

    @Test @Order(3)
    @DisplayName("TODO 3 · guarda en texto, una línea por producto y en UTF-8")
    void todo3() throws IOException {
        gestor.guardarTexto(DEMO);

        List<String> lineas = Files.readAllLines(carpeta.resolve("productos.txt"), StandardCharsets.UTF_8);
        assertEquals(2, lineas.size(), "una línea por producto");
        assertEquals("1;Teclado mecánico;39.9", lineas.get(0));
        assertTrue(lineas.get(1).contains("Ratón óptico"), "los acentos deben conservarse (UTF-8)");
    }

    @Test @Order(4)
    @DisplayName("TODO 3 · guardar dos veces no acumula los productos anteriores")
    void todo3NoAcumula() throws IOException {
        gestor.guardarTexto(DEMO);
        gestor.guardarTexto(List.of(new Producto(9, "Monitor", 129.0)));

        List<String> lineas = Files.readAllLines(carpeta.resolve("productos.txt"), StandardCharsets.UTF_8);
        assertEquals(1, lineas.size(), "debe quedar solo el último guardado, no los 3");
    }

    // ───────────────────────────── TODO 4 ─────────────────────────────

    @Test @Order(5)
    @DisplayName("TODO 4 · lee el fichero de texto y reconstruye los productos")
    void todo4() throws IOException {
        Files.writeString(carpeta.resolve("productos.txt"),
                "1;Teclado mecánico;39.9\n2;Ratón óptico;12.5\n", StandardCharsets.UTF_8);

        assertEquals(DEMO, gestor.cargarTexto());
    }

    @Test @Order(6)
    @DisplayName("TODO 4 · si el fichero no existe devuelve lista vacía, no excepción")
    void todo4SinFichero() throws IOException {
        assertTrue(gestor.cargarTexto().isEmpty());
    }

    // ───────────────────────────── TODO 5 ─────────────────────────────

    @Test @Order(7)
    @DisplayName("TODO 5 · guarda el catálogo como XML con JAXB")
    void todo5() throws Exception {
        gestor.guardarXmlConJaxb(DEMO);

        Path xml = carpeta.resolve("productos.xml");
        assertTrue(Files.exists(xml), "debería haberse creado productos.xml");

        String contenido = Files.readString(xml, StandardCharsets.UTF_8);
        assertTrue(contenido.contains("<catalogo>"), "falta el elemento raíz <catalogo>");
        assertTrue(contenido.contains("<nombre>Teclado mecánico</nombre>"),
                "faltan los productos dentro del XML");
    }

    // ───────────────────────────── TODO 6 ─────────────────────────────

    @Test @Order(8)
    @DisplayName("TODO 6 · lee el XML con un parser DOM")
    void todo6() throws Exception {
        Files.writeString(carpeta.resolve("productos.xml"), XML_DEMO, StandardCharsets.UTF_8);

        assertEquals(DEMO, gestor.leerXmlConDom());
    }

    // ───────────────────────────── TODO 7 ─────────────────────────────

    @Test @Order(9)
    @DisplayName("TODO 7 · sin ficheros devuelve lista vacía y no lanza excepción")
    void todo7SinNada() {
        assertDoesNotThrow(() -> assertTrue(gestor.cargarCatalogoRobusto().isEmpty()));
    }

    @Test @Order(10)
    @DisplayName("TODO 7 · si hay XML y texto, gana el XML")
    void todo7PrefiereXml() throws IOException {
        Files.writeString(carpeta.resolve("productos.txt"), "9;Version texto;1.0\n", StandardCharsets.UTF_8);
        Files.writeString(carpeta.resolve("productos.xml"), XML_DEMO, StandardCharsets.UTF_8);

        List<Producto> resultado = gestor.cargarCatalogoRobusto();
        assertEquals(2, resultado.size());
        assertEquals("Teclado mecánico", resultado.get(0).getNombre());
    }

    @Test @Order(11)
    @DisplayName("TODO 7 · si el XML está corrupto, cae en el fichero de texto")
    void todo7XmlCorrupto() throws IOException {
        Files.writeString(carpeta.resolve("productos.txt"),
                "3;Version texto de rescate;3.0\n", StandardCharsets.UTF_8);
        Files.writeString(carpeta.resolve("productos.xml"),
                "<catalogo><producto>esto no cierra bien", StandardCharsets.UTF_8);

        List<Producto> resultado = assertDoesNotThrow(() -> gestor.cargarCatalogoRobusto());
        assertEquals(1, resultado.size());
        assertEquals("Version texto de rescate", resultado.get(0).getNombre());
    }
}
