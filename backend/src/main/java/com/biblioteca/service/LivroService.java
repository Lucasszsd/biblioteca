package com.biblioteca.service;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<LivroDTO> findAll() {
        return livroRepository.findAll().stream()
                .map(LivroDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<LivroDTO> findById(Long id) {
        return livroRepository.findById(id)
                .map(LivroDTO::new);
    }

    public LivroDTO save(Livro livro) {
        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado");
        }

        Livro savedLivro = livroRepository.save(livro);
        return new LivroDTO(savedLivro);
    }

    public Optional<LivroDTO> update(Long id, Livro livro) {
        return livroRepository.findById(id)
                .map(existingLivro -> {
                    if (!existingLivro.getIsbn().equals(livro.getIsbn()) &&
                            livroRepository.existsByIsbn(livro.getIsbn())) {
                        throw new IllegalArgumentException("ISBN já cadastrado");
                    }

                    existingLivro.setTitulo(livro.getTitulo());
                    existingLivro.setAutor(livro.getAutor());
                    existingLivro.setIsbn(livro.getIsbn());
                    existingLivro.setDataPublicacao(livro.getDataPublicacao());
                    existingLivro.setCategoria(livro.getCategoria());

                    Livro updatedLivro = livroRepository.save(existingLivro);
                    return new LivroDTO(updatedLivro);
                });
    }

    public boolean deleteById(Long id) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<LivroDTO> findByCategoria(String categoria) {
        return livroRepository.findByCategoria(categoria).stream()
                .map(LivroDTO::new)
                .collect(Collectors.toList());
    }

    public List<String> findAllCategorias() {
        return livroRepository.findAllCategorias();
    }

    public Optional<Livro> findLivroEntityById(Long id) {
        return livroRepository.findById(id);
    }

    public List<LivroDTO> findLivrosByCategoriaNotEmprestadosByUsuario(String categoria, Long usuarioId) {
        return livroRepository.findLivrosByCategoriaNotEmprestadosByUsuario(categoria, usuarioId).stream()
                .map(LivroDTO::new)
                .collect(Collectors.toList());
    }
}

