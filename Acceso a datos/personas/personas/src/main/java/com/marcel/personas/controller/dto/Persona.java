package com.marcel.personas.controller.dto;


public record Persona(String dni, String name) {

    public static void main(String[] args) {
        Persona persona = new Persona("1234", "nombre1");
        System.out.println(persona.dni());
        System.out.println(persona.name);
    }
}
