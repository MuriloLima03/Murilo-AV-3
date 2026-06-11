package com.autobots.automanager.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Mercadoria;

@Repository
public interface RepositorioMercadoria extends JpaRepository<Mercadoria, Long> {
	List<Mercadoria> findByNome(String nome);
}
