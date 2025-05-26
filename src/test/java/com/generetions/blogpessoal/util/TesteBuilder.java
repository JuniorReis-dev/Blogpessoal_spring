package com.generetions.blogpessoal.util;

import com.generetions.blogpessoal.model.Usuario;

public class TesteBuilder {
	
	public static Usuario criarUsuario(Long id, String nome, String email, String senha) {
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNome(nome);
		usuario.setUsuario(email);
		usuario.setSenha(senha);
		usuario.setFoto("");
		return usuario;
		
	}
	
	public static Usuario criarUsuarioRoot() {
		return criarUsuario(null,"root", "root@email.com", "rootroot");
	}
}
