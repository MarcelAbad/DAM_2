package com.marcel.personas.controller.rest;

import com.marcel.personas.controller.dto.Persona;
import com.marcel.personas.service.PersonasService;
import io.micrometer.common.lang.internal.Contract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonasController {

    private PersonasService service;

    public PersonasController(PersonasService service) {
        this.service = service;
    }

    @GetMapping("/personas")
    public List<Persona> getPersonas(){
        return this.service.findAll();
    }
}
