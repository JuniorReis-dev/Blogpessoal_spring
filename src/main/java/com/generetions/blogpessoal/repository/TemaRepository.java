package com.generetions.blogpessoal.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generetions.blogpessoal.model.Tema;


public interface TemaRepository extends JpaRepository<Tema, Long>{

	List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
}