package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Usuario, permite acessar e manipular dados relacionados a usuários no banco de dados.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Consulta JPA para encontrar um usuário pelo endereço de e-mail.
     *
     * @param email Endereço de e-mail do usuário
     * @return Uma instância de Optional contendo o usuário correspondente ao e-mail fornecido, se existir.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se um usuário existe com o endereço de e-mail fornecido.
     *
     * @param email Endereço de e-mail a ser verificado
     * @return true se um usuário existir com o e-mail fornecido, false caso contrário
     */
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
