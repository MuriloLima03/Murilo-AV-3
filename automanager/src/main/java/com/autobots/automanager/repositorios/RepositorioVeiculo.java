package com.autobots.automanager.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Veiculo;

@Repository
public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {
	List<Veiculo> findByPlaca(String placa);
}
