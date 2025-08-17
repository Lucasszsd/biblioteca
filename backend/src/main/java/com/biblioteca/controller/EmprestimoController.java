package com.biblioteca.controller;

import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos de livros")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @Operation(
            summary = "Listar todos os empréstimos",
            description = "Retorna uma lista com todos os empréstimos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de empréstimos retornada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> findAll() {
        List<EmprestimoDTO> emprestimos = emprestimoService.findAll();
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(
            summary = "Buscar empréstimo por ID",
            description = "Retorna um empréstimo específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Empréstimo encontrado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Empréstimo não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> findById(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Long id
    ) {
        Optional<EmprestimoDTO> emprestimo = emprestimoService.findById(id);
        return emprestimo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar empréstimos por usuário",
            description = "Retorna todos os empréstimos de um usuário específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de empréstimos do usuário retornada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> findByUsuarioId(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long usuarioId
    ) {
        List<EmprestimoDTO> emprestimos = emprestimoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(
            summary = "Buscar empréstimos por status",
            description = "Retorna todos os empréstimos com o status especificado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de empréstimos com o status especificado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            )
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmprestimoDTO>> findByStatus(
            @Parameter(
                    description = "Status do empréstimo (ex: ATIVO, DEVOLVIDO, ATRASADO)",
                    required = true,
                    example = "ATIVO"
            )
            @PathVariable String status
    ) {
        List<EmprestimoDTO> emprestimos = emprestimoService.findByStatus(status);
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(
            summary = "Criar novo empréstimo",
            description = "Cria um novo empréstimo para um usuário e livro específicos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Empréstimo criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<?> save(
            @Parameter(description = "ID do usuário que fará o empréstimo", required = true)
            @RequestParam Long usuarioId,
            @Parameter(description = "ID do livro a ser emprestado", required = true)
            @RequestParam Long livroId
    ) {
        try {
            EmprestimoDTO savedEmprestimo = emprestimoService.save(usuarioId, livroId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmprestimo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Devolver livro emprestado",
            description = "Marca um empréstimo como devolvido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro devolvido com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmprestimoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Empréstimo não encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao processar devolução",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(
            @Parameter(description = "ID do empréstimo a ser devolvido", required = true)
            @PathVariable Long id
    ) {
        try {
            Optional<EmprestimoDTO> emprestimo = emprestimoService.devolver(id);
            return emprestimo.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Obter recomendações de livros",
            description = "Retorna uma lista de livros recomendados baseada no histórico de empréstimos do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de recomendações retornada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LivroDTO.class)
                    )
            )
    })
    @GetMapping("/usuario/{usuarioId}/recomendacoes")
    public ResponseEntity<List<LivroDTO>> getRecomendacoes(
            @Parameter(description = "ID do usuário para obter recomendações", required = true)
            @PathVariable Long usuarioId
    ) {
        List<LivroDTO> recomendacoes = emprestimoService.getRecomendacoes(usuarioId);
        return ResponseEntity.ok(recomendacoes);
    }

    @Operation(
            summary = "Deletar empréstimo",
            description = "Remove um empréstimo do sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Empréstimo deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Empréstimo não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID do empréstimo a ser deletado", required = true)
            @PathVariable Long id
    ) {
        boolean deleted = emprestimoService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}