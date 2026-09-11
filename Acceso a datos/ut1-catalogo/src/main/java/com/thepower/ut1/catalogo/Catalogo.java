package com.thepower.ut1.catalogo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Elemento raíz del XML:
 * &lt;catalogo&gt;&lt;producto&gt;...&lt;/producto&gt;&lt;/catalogo&gt;
 * Ya está completa, no hay ningún TODO aquí.
 */
@XmlRootElement(name = "catalogo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Catalogo {

    @XmlElement(name = "producto")
    private List<Producto> productos = new ArrayList<>();

    public Catalogo() {
    }

    public Catalogo(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
    }

    public List<Producto> getProductos() { return productos; }
}
