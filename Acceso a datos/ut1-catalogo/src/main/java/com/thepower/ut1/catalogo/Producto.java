package com.thepower.ut1.catalogo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * Un producto del catálogo. Clase de datos: ya está completa, no hay
 * ningún TODO aquí.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "nombre", "precio"})
public class Producto {

    @XmlElement private int id;
    @XmlElement private String nombre;
    @XmlElement private double precio;

    /** JAXB necesita un constructor sin argumentos. */
    public Producto() {
    }

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto p = (Producto) o;
        return id == p.id
                && Double.compare(p.precio, precio) == 0
                && Objects.equals(nombre, p.nombre);
    }

    @Override
    public int hashCode() { return Objects.hash(id, nombre, precio); }

    @Override
    public String toString() { return id + ";" + nombre + ";" + precio; }
}
