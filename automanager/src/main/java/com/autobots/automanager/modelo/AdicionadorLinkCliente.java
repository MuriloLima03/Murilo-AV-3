package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entitades.Usuario;


@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Usuario> {



	@Override
	public void adicionarLink(List<Usuario> lista) {
		for (Usuario usuario : lista) {
			long id = usuario.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ClienteControle.class)
							.obterUsuario(id))
					.withSelfRel();
			usuario.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterUsuarios())
				.withRel("usuarios");
		objeto.add(linkProprio);
	}
}