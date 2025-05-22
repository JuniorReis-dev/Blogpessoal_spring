package com.generetions.blogpessoal.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generetions.blogpessoal.model.Usuario;
import com.generetions.blogpessoal.model.UsuarioLogin;
import com.generetions.blogpessoal.repository.TemaRepository;
import com.generetions.blogpessoal.repository.UsuarioRepository;
import com.generetions.blogpessoal.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final TemaRepository temaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    UsuarioController(TemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    @PostMapping("/cadastrar")
        public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
            return usuarioService.cadastrarUsuario(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
        Optional<Usuario> existe = usuarioRepository.findByUsuario(usuario.getUsuario());

        if (existe.isPresent() && !existe.get().getId().equals(usuario.getId())) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já está cadastrado!");

        return usuarioService.cadastrarUsuario(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioLogin> autenticar(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) {
        return usuarioService.autenticarUsuario(usuarioLogin)
            .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}