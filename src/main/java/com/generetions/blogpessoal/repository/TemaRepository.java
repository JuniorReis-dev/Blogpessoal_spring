package com.generetions.blogpessoal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generetions.blogpessoal.model.Tema;
@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {
    List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
}
