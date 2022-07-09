package com.example.computadores.repository;

import com.example.computadores.domain.Computador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputadorRepository extends JpaRepository<Computador, Long> {
}
