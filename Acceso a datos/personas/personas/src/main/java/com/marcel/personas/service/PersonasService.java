package com.marcel.personas.service;

import com.marcel.personas.controller.dto.Persona;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonasService {

    public List<Persona> findAll(){
        return List.of(new Persona("0002", "La S sin S"));
    }
}
