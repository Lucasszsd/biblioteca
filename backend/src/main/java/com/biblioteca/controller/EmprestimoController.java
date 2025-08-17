package com.biblioteca.controller;
import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
@Tag(name = "Empréstimos", description = "Endpoints para empréstimos")
public class EmprestimoController {
    @Autowired
    private EmprestimoService emprestimoService;
    @GetMapping
    @Operation(summary = "Listar todos empréstimos", description = "Retorna uma lista com todos os empréstimos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
    @Operation(summary = "Criar novo empréstimo", description = "Cria um novo empréstimo para um usuário e livro específicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados invalidos ou livro não disponível")
    })
    public ResponseEntity<?> save(
            @Parameter(description = "ID do usuário") @RequestParam Long usuarioId, 
            @Parameter(description = "ID do livro") @RequestParam Long livroId) {
        try {
            EmprestimoDTO savedEmprestimo = emprestimoService.save(usuarioId, livroId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmprestimo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}/devolver")
    @Operation(summary = "Devolver livro", description = "Marca um empréstimo como devolvido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro devolvido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro na devolução")
    })
    public ResponseEntity<?> devolver(
            @Parameter(description = "ID do empréstimo") @PathVariable Long id) {
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
