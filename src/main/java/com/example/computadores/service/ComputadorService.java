package com.example.computadores.service;

import com.example.computadores.domain.Computador;
import com.example.computadores.repository.ComputadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComputadorService {

    private final ComputadorRepository repository;

    public ComputadorService(ComputadorRepository repository) {
        this.repository = repository;
    }

    public Computador create(Computador c){
        return repository.save(c);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public Computador update(Computador c){
        return repository.saveAndFlush(c);
    }

    public Computador findById(Long id){
        Optional<Computador> ComputadorOptional = repository.findById(id);
        return ComputadorOptional.orElse(null);
    }

    public List<Computador> findAll(){
        return repository.findAll();
    }
}
