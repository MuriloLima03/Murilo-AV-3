package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Telefone;

@Component
public class TelefoneSelecionador {

    public Telefone selecionar(List<Telefone> telefones, long id) {
        for (Telefone telefone : telefones) {
            if (telefone.getId() == id) {
                return telefone;
            }
        }
        return null;
    }
}