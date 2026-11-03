package com.marcel.personas.repository;

import com.marcel.personas.model.Empresa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository {

    public List<Empresa> getPersona();
}
