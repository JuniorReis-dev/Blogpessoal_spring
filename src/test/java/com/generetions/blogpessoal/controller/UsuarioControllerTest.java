package com.generetions.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generetions.blogpessoal.model.Usuario;
import com.generetions.blogpessoal.model.UsuarioLogin;
import com.generetions.blogpessoal.repository.UsuarioRepository;
import com.generetions.blogpessoal.service.UsuarioService;
import com.generetions.blogpessoal.util.TesteBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String USUARIO_ROOT_EMAIL = "root@email.com";
	private static final String USUARIO_ROOT_SENHA = "rootroot";
	private static final String BASE_URL_USUARIOS = "/usuarios";
	
	@BeforeAll
	void start(){
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TesteBuilder.criarUsuarioRoot());
		
	}
	
	@Test
	@DisplayName("✔️Deve cadastrar um novo usuário com sucesso")
	public void deveCadastrarUsuario() {
		
		// Given
		Usuario usuario = TesteBuilder.criarUsuario(null, "Paulo Antunes",
				"paulo_antunes@email.com.br", "13465278");

		// When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Paulo Antunes", resposta.getBody().getNome());
		assertEquals("paulo_antunes@email.com.br", resposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		
		//Given
		Usuario usuario = TesteBuilder.criarUsuario(null, "Maria da Silva",
				"maria_silva@email.com.br", "13465278");
		usuarioService.cadastrarUsuario(usuario);

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve atualizar um usuário existente")
	public void deveAtualizarUmUsuario() {
		
		//Given
		Usuario usuario = TesteBuilder.criarUsuario(null, "Juliana Andrews", "juliana_andrews@email.com.br",
				"juliana123");
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
		
		Usuario usuarioUpdate = TesteBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Juliana Ramos", 
				"juliana_ramos@email.com.br", "juliana123");

		//When
		HttpEntity<Usuario> requisicao = new HttpEntity<>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Juliana Ramos", resposta.getBody().getNome());
		assertEquals("juliana_ramos@email.com.br", resposta.getBody().getUsuario());
	}
	@Test
	@DisplayName("Deve listar todos os usuários")
	public void deveListarTodosUsuarios() {
		
		//Given
		usuarioService.cadastrarUsuario(TesteBuilder.criarUsuario(null, "Ana Clara",
				"ana@email.com", "senha123"));
		usuarioService.cadastrarUsuario(TesteBuilder.criarUsuario(null, "Carlos Souza",
				"carlos@email.com", "senha123"));

		//When
		ResponseEntity<Usuario[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/all", HttpMethod.GET, null, Usuario[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	@Test
	@DisplayName("Deve retornar status NOT_FOUND ao buscar usuário por ID inexistente")
	public void deveRetornarNotFoundAoBuscarPorIdInexistente() {
		
		// Given
		// Um ID que sabemos que não existe no banco de dados (ex: um ID muito grande ou negativo)
		Long idInexistente = 999999999L; 

		// When
		// Envia uma requisição GET para um ID que não existe
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_USUARIOS + "/" + idInexistente, HttpMethod.GET, null, Usuario.class);

		// Then
		// Verifica se o status da resposta é NOT_FOUND (404)
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}
	@Test
	@DisplayName("Não deve autenticar usuário com credenciais inválidas")
	public void naoDeveAutenticarComCredenciaisInvalidas() {
		
		// Given
		// Não precisamos cadastrar um usuário válido, pois estamos testando credenciais inválidas
		UsuarioLogin usuarioLoginInvalido = new UsuarioLogin();
		usuarioLoginInvalido.setUsuario("usuario_inexistente@email.com");
		usuarioLoginInvalido.setSenha("senhaErrada");

		// When
		HttpEntity<UsuarioLogin> requisicaoLogin = new HttpEntity<>(usuarioLoginInvalido);
		ResponseEntity<UsuarioLogin> respostaLogin = testRestTemplate.exchange(
				BASE_URL_USUARIOS + "/logar", HttpMethod.POST, requisicaoLogin, UsuarioLogin.class);

		// Then
		// Espera-se que o status da resposta seja UNAUTHORIZED (401)
		// Ou BAD_REQUEST (400) dependendo da sua implementação para credenciais inválidas
		assertEquals(HttpStatus.UNAUTHORIZED, respostaLogin.getStatusCode());
		// O corpo da resposta pode ser nulo ou conter uma mensagem de erro
		// Se o corpo não é nulo, você pode verificar a mensagem de erro se houver.
	}

	
}
