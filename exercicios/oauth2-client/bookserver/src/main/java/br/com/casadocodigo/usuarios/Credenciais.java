package br.com.casadocodigo.usuarios;

import javax.persistence.Embeddable;

import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class Credenciais implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	private String email;

	@Getter
	private String senha;

	@Deprecated
	Credenciais() {}

	public Credenciais(String email, String senha) {
		super();
		this.email = email;
		this.senha = senha;
	}

}
