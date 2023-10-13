package br.com.sanittas.app.repository;

import br.com.sanittas.app.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query(value = "SELECT e from Endereco e where e.usuario.id = :usuario_id")
    List<Endereco> findByUsuario_Id(Long usuario_id);
}
