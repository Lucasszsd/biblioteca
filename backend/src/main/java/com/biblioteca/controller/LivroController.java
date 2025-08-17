package com.biblioteca.controller;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<List<LivroDTO>> findAll() {
        List<LivroDTO> livros = livroService.findAll();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> findById(@PathVariable Long id) {
        Optional<LivroDTO> livro = livroService.findById(id);
        return livro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LivroDTO>> findByCategoria(@PathVariable String categoria) {
        List<LivroDTO> livros = livroService.findByCategoria(categoria);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> findAllCategorias() {
        List<String> categorias = livroService.findAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Livro livro) {
        try {
            LivroDTO savedLivro = livroService.save(livro);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLivro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Livro livro) {
        try {
            Optional<LivroDTO> updatedLivro = livroService.update(id, livro);
            return updatedLivro.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = livroService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

