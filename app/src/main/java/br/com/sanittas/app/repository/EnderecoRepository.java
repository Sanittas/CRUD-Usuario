package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositório JPA para a entidade Endereco, permite acessar e manipular dados relacionados a endereços no banco de dados.
 */
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * Consulta JPA personalizada para encontrar endereços por ID de usuário.
     *
     * @param usuario_id ID do usuário
     * @return Lista de endereços associados ao usuário especificado
     */
    @Query(value = "SELECT e FROM Endereco e WHERE e.usuario.id = :usuario_id")
    List<Endereco> findByUsuario_Id(Long usuario_id);
}
