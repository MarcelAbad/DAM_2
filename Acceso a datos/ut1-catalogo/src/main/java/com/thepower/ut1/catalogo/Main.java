package com.thepower.ut1.catalogo;

import java.nio.file.Path;
import java.util.List;

/**
 * Demo para probar a ojo lo que llevas hecho. No se evalúa con tests.
 * Irá funcionando a medida que completes los TODO.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        GestorCatalogo gestor = new GestorCatalogo(Path.of("datos"));
        gestor.inicializar();

        List<Producto> productos = List.of(
                new Producto(1, "Teclado mecánico", 39.90),
                new Producto(2, "Ratón óptico", 12.50),
                new Producto(3, "Monitor 24 pulgadas", 129.00)
        );

        gestor.guardarTexto(productos);
        gestor.guardarXmlConJaxb(productos);

        System.out.println("Desde texto:      " + gestor.cargarTexto());
        System.out.println("Desde XML (DOM):  " + gestor.leerXmlConDom());
        System.out.println("Carga robusta:    " + gestor.cargarCatalogoRobusto());
    }
}
