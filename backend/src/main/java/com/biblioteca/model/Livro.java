package com.biblioteca.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String titulo;
    @NotBlank(message = "Autor é obrigatório")
    @Column(nullable = false)
    private String autor;
    @NotBlank(message = "ISBN é obrigatório")
    @Column(nullable = false, unique = true)
    private String isbn;
    @NotNull(message = "Data de publicação é obrigatória")
    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;
    @NotBlank(message = "Categoria é obrigatória")
    @Column(nullable = false)
    private String categoria;
    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprestimo> emprestimos = new ArrayList<>();
    public Livro() {}
    public Livro(String titulo, String autor, String isbn, LocalDate dataPublicacao, String categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.categoria = categoria;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}
