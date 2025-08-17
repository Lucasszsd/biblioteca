package com.biblioteca.service;
import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Transactional
public class EmprestimoService {
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private LivroService livroService;
    public List<EmprestimoDTO> findAll() {
        return emprestimoRepository.findAll().stream()
                .map(EmprestimoDTO::new)
                .collect(Collectors.toList());
    }
    public Optional<EmprestimoDTO> findById(Long id) {
        return emprestimoRepository.findById(id)
                .map(EmprestimoDTO::new);
    }
    public EmprestimoDTO save(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioService.findUsuarioEntityById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Livro livro = livroService.findLivroEntityById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        if (emprestimoRepository.countEmprestimosAtivosByLivroId(livroId) > 0) {
            throw new IllegalArgumentException("Livro já está emprestado");
        }
        Emprestimo emprestimo = new Emprestimo(usuario, livro);
        Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);
        return new EmprestimoDTO(savedEmprestimo);
    }
    public Optional<EmprestimoDTO> devolver(Long id) {
        return emprestimoRepository.findById(id)
                .map(emprestimo -> {
                    if (emprestimo.getStatus() == Emprestimo.StatusEmprestimo.DEVOLVIDO) {
                        throw new IllegalArgumentException("Empréstimo já foi devolvido");
                    }
                    emprestimo.devolver();
                    Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);
                    return new EmprestimoDTO(savedEmprestimo);
                });
    }
    public List<EmprestimoDTO> findByUsuarioId(Long usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId).stream()
                .map(EmprestimoDTO::new)
                .collect(Collectors.toList());
    }
    public List<EmprestimoDTO> findByStatus(String status) {
        Emprestimo.StatusEmprestimo statusEnum = Emprestimo.StatusEmprestimo.valueOf(status.toUpperCase());
        return emprestimoRepository.findByStatus(statusEnum).stream()
                .map(EmprestimoDTO::new)
                .collect(Collectors.toList());
    }
    public List<LivroDTO> getRecomendacoes(Long usuarioId) {
        List<String> categoriasEmprestadas = emprestimoRepository.findCategoriasEmprestadasByUsuario(usuarioId);
        if (categoriasEmprestadas.isEmpty()) {
            return livroService.findAll();
        }
        List<LivroDTO> recomendacoes = categoriasEmprestadas.stream()
                .flatMap(categoria -> livroService.findLivrosByCategoriaNotEmprestadosByUsuario(categoria, usuarioId).stream())
                .collect(Collectors.toList());
        return recomendacoes;
    }
    public boolean deleteById(Long id) {
        if (emprestimoRepository.existsById(id)) {
            emprestimoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
