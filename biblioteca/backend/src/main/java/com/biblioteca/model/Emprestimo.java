package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Usuário é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Livro é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @NotNull(message = "Data de empréstimo é obrigatória")
    @PastOrPresent(message = "Data de empréstimo não pode ser futura")
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status;

    public enum StatusEmprestimo {
        EMPRESTADO, DEVOLVIDO
    }

    public Emprestimo() {}

    public Emprestimo(Usuario usuario, Livro livro) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.status = StatusEmprestimo.EMPRESTADO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public void devolver() {
        this.status = StatusEmprestimo.DEVOLVIDO;
        this.dataDevolucao = LocalDate.now();
    }
}
