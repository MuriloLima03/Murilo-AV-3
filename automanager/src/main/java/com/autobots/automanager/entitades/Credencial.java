package com.autobots.automanager.entitades;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "tipo"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = CredencialUsuarioSenha.class, name = "senha")
})
public abstract class Credencial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Date criacao;

	@Column
	private Date ultimoAcesso;

	@Column(nullable = false)
	private boolean inativo;

	@PrePersist
	public void prePersist() {
		this.criacao = new Date();
		this.inativo = false;
	}

	@PreUpdate
	public void preUpdate() {
		this.ultimoAcesso = new Date();
	}
}