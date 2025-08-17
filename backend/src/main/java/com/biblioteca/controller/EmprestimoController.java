package com.biblioteca.controller;

import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> findAll() {
        List<EmprestimoDTO> emprestimos = emprestimoService.findAll();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> findById(@PathVariable Long id) {
        Optional<EmprestimoDTO> emprestimo = emprestimoService.findById(id);
        return emprestimo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<EmprestimoDTO> emprestimos = emprestimoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmprestimoDTO>> findByStatus(@PathVariable String status) {
        List<EmprestimoDTO> emprestimos = emprestimoService.findByStatus(status);
        return ResponseEntity.ok(emprestimos);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam Long usuarioId, @RequestParam Long livroId) {
        try {
            EmprestimoDTO savedEmprestimo = emprestimoService.save(usuarioId, livroId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmprestimo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            Optional<EmprestimoDTO> emprestimo = emprestimoService.devolver(id);
            return emprestimo.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}/recomendacoes")
    public ResponseEntity<List<LivroDTO>> getRecomendacoes(@PathVariable Long usuarioId) {
        List<LivroDTO> recomendacoes = emprestimoService.getRecomendacoes(usuarioId);
        return ResponseEntity.ok(recomendacoes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = emprestimoService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

