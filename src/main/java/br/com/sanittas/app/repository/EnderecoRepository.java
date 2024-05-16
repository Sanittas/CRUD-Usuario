package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.EnderecoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositório JPA para a entidade Endereco, permite acessar e manipular dados relacionados a endereços no banco de dados.
 */
public interface EnderecoRepository extends JpaRepository<EnderecoUsuario, Integer> {

}
