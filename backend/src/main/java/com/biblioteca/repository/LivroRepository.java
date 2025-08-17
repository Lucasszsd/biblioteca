package com.biblioteca.repository;
import com.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    List<Livro> findByCategoria(String categoria);
    @Query("SELECT l FROM Livro l WHERE l.categoria = :categoria AND l.id NOT IN " +
            "(SELECT DISTINCT e.livro.id FROM Emprestimo e WHERE e.usuario.id = :usuarioId)")
    List<Livro> findLivrosByCategoriaNotEmprestadosByUsuario(
            @Param("categoria") String categoria,
            @Param("usuarioId") Long usuarioId);
    @Query("SELECT DISTINCT l.categoria FROM Livro l")
    List<String> findAllCategorias();
}
