package com.biblioteca.dto;

import com.biblioteca.model.Emprestimo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class EmprestimoDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long livroId;
    private String livroTitulo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEmprestimo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucao;

    private String status;

    public EmprestimoDTO() {}

    public EmprestimoDTO(Emprestimo emprestimo) {
        this.id = emprestimo.getId();
        this.usuarioId = emprestimo.getUsuario().getId();
        this.usuarioNome = emprestimo.getUsuario().getNome();
        this.livroId = emprestimo.getLivro().getId();
        this.livroTitulo = emprestimo.getLivro().getTitulo();
        this.dataEmprestimo = emprestimo.getDataEmprestimo();
        this.dataDevolucao = emprestimo.getDataDevolucao();
        this.status = emprestimo.getStatus().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public String getLivroTitulo() {
        return livroTitulo;
    }

    public void setLivroTitulo(String livroTitulo) {
        this.livroTitulo = livroTitulo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

