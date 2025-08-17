package com.biblioteca.repository;
import com.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.emprestimos e WHERE e.livro.categoria = :categoria")
    List<Usuario> findUsuariosByCategoriaLivro(@Param("categoria") String categoria);
}
