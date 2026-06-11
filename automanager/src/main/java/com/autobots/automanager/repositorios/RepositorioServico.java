package com.autobots.automanager.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Servico;

@Repository
public interface RepositorioServico extends JpaRepository<Servico, Long> {
	List<Servico> findByNome(String nome);
}
