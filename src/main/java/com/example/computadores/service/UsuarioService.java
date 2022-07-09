package com.example.computadores.service;

import com.example.computadores.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    UsuarioRepository repository;

    UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }
}
