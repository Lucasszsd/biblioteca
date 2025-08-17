package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Emprestimo.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    @Query("SELECT e FROM Emprestimo e WHERE e.livro.id = :livroId AND e.status = 'ATIVO'")
    Optional<Emprestimo> findEmprestimoAtivoByLivroId(@Param("livroId") Long livroId);

    @Query("SELECT DISTINCT e.livro.categoria FROM Emprestimo e WHERE e.usuario.id = :usuarioId")
    List<String> findCategoriasEmprestadasByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.livro.id = :livroId AND e.status = 'ATIVO'")
    long countEmprestimosAtivosByLivroId(@Param("livroId") Long livroId);
}

