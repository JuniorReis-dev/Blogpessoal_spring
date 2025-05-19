package com.generetions.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_postagens")
public class Postagem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(length = 100)
	@NotBlank(message = "O atributo titulo é obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo titulo deve ter no minimo 5 caracteres e no maximo 100 caracteres.")
	private String titulo;
	
	@Column(length = 1000)
	@NotBlank(message = "O atributo texto é obrigatório!")
	@Size(min = 5, max = 1000, message = "O atributo titulo deve ter no minimo 10 caracteres e no maximo 1000 caracteres.")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getTime() {
		return data;
	}

	public void setTime(LocalDateTime data) {
		this.data = data;
	}
	
}

