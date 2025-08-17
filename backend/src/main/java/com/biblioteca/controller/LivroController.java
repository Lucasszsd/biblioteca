package com.biblioteca.controller;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
public class LivroController {
    @Autowired
    private LivroService livroService;
    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista com todos os livros cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<LivroDTO>> findAll() {
        List<LivroDTO> livros = livroService.findAll();
        return ResponseEntity.ok(livros);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<LivroDTO> findById(
            @Parameter(description = "ID do livro") @PathVariable Long id) {
        Optional<LivroDTO> livro = livroService.findById(id);
        return livro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar livros por categoria", description = "Retorna uma lista de livros de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<LivroDTO>> findByCategoria(
            @Parameter(description = "Categoria dos livros") @PathVariable String categoria) {
        List<LivroDTO> livros = livroService.findByCategoria(categoria);
        return ResponseEntity.ok(livros);
    }
    @GetMapping("/categorias")
    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias de livros disponíveis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<String>> findAllCategorias() {
        List<String> categorias = livroService.findAllCategorias();
        return ResponseEntity.ok(categorias);
    }
    @PostMapping
    @Operation(summary = "Criar novo livro", description = "Cria um novo livro no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> save(@Valid @RequestBody Livro livro) {
        try {
            LivroDTO savedLivro = livroService.save(livro);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLivro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID do livro") @PathVariable Long id, 
            @Valid @RequestBody Livro livro) {
        try {
            Optional<LivroDTO> updatedLivro = livroService.update(id, livro);
            return updatedLivro.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro", description = "Remove um livro do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID do livro") @PathVariable Long id) {
        boolean deleted = livroService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
