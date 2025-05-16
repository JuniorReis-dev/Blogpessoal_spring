package com.generetions.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generetions.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem , Long>{

}
