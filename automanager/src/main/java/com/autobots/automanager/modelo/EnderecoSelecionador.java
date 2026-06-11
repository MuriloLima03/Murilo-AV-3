package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Endereco;

@Component
public class EnderecoSelecionador {

    public Endereco selecionar(List<Endereco> enderecos, long id) {
        for (Endereco endereco : enderecos) {
            if (endereco.getId() == id) {
                return endereco;
            }
        }
        return null;
    }
}