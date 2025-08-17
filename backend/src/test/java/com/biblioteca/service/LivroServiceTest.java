package com.biblioteca.service;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
    @Mock
    private LivroRepository livroRepository;
    @InjectMocks
    private LivroService livroService;
    private Livro livro;
    private LivroDTO livroDTO;
    @BeforeEach
    void setUp() {
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("O Senhor dos Anéis");
        livro.setAutor("J.R.R. Tolkien");
        livro.setIsbn("978-0-261-10295-4");
        livro.setDataPublicacao(java.time.LocalDate.of(1954, 7, 29));
        livro.setCategoria("Fantasia");
        livroDTO = new LivroDTO();
        livroDTO.setId(1L);
        livroDTO.setTitulo("O Senhor dos Anéis");
        livroDTO.setAutor("J.R.R. Tolkien");
        livroDTO.setIsbn("978-0-261-10295-4");
        livroDTO.setDataPublicacao(java.time.LocalDate.of(1954, 7, 29));
        livroDTO.setCategoria("Fantasia");
    }
    @Test
    void findAll_DeveRetornarListaDeLivros() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findAll()).thenReturn(livros);
        List<LivroDTO> result = livroService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(livro.getTitulo(), result.get(0).getTitulo());
        verify(livroRepository, times(1)).findAll();
    }
    @Test
    void findById_QuandoLivroExiste_DeveRetornarLivro() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        Optional<LivroDTO> result = livroService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(livro.getTitulo(), result.get().getTitulo());
        verify(livroRepository, times(1)).findById(1L);
    }
    @Test
    void findById_QuandoLivroNaoExiste_DeveRetornarVazio() {
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<LivroDTO> result = livroService.findById(999L);
        assertFalse(result.isPresent());
        verify(livroRepository, times(1)).findById(999L);
    }
    @Test
    void findByCategoria_DeveRetornarLivrosDaCategoria() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findByCategoria("Fantasia")).thenReturn(livros);
        List<LivroDTO> result = livroService.findByCategoria("Fantasia");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fantasia", result.get(0).getCategoria());
        verify(livroRepository, times(1)).findByCategoria("Fantasia");
    }
    @Test
    void findAllCategorias_DeveRetornarTodasAsCategorias() {
        List<String> categorias = Arrays.asList("Fantasia", "Ficção", "Romance");
        when(livroRepository.findAllCategorias()).thenReturn(categorias);
        List<String> result = livroService.findAllCategorias();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Fantasia"));
        verify(livroRepository, times(1)).findAllCategorias();
    }
    @Test
    void save_QuandoLivroValido_DeveSalvarComSucesso() {
        Livro novoLivro = new Livro();
        novoLivro.setTitulo("O Hobbit");
        novoLivro.setAutor("J.R.R. Tolkien");
        novoLivro.setIsbn("978-0-261-10295-5");
        novoLivro.setDataPublicacao(java.time.LocalDate.of(1937, 9, 21));
        novoLivro.setCategoria("Fantasia");
        when(livroRepository.save(any(Livro.class))).thenReturn(novoLivro);
        LivroDTO result = livroService.save(novoLivro);
        assertNotNull(result);
        assertEquals(novoLivro.getTitulo(), result.getTitulo());
        verify(livroRepository, times(1)).save(novoLivro);
    }
    @Test
    void save_QuandoLivroInvalido_DeveLancarExcecao() {
        Livro livroInvalido = new Livro();
        livroInvalido.setTitulo("");
        livroInvalido.setAutor("Autor");
        livroInvalido.setIsbn("123");
        livroInvalido.setDataPublicacao(java.time.LocalDate.now());
        livroInvalido.setCategoria("Categoria");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            livroService.save(livroInvalido);
        });
        assertNotNull(exception.getMessage());
        verify(livroRepository, never()).save(any(Livro.class));
    }
    @Test
    void update_QuandoLivroExiste_DeveAtualizarComSucesso() {
        Livro livroAtualizado = new Livro();
        livroAtualizado.setTitulo("O Senhor dos Anéis - Edição Especial");
        livroAtualizado.setAutor("J.R.R. Tolkien");
        livroAtualizado.setIsbn("978-0-261-10295-4");
        livroAtualizado.setDataPublicacao(java.time.LocalDate.of(1954, 7, 29));
        livroAtualizado.setCategoria("Fantasia");
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroRepository.save(any(Livro.class))).thenReturn(livroAtualizado);
        Optional<LivroDTO> result = livroService.update(1L, livroAtualizado);
        assertTrue(result.isPresent());
        assertEquals(livroAtualizado.getTitulo(), result.get().getTitulo());
        verify(livroRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).save(any(Livro.class));
    }
    @Test
    void update_QuandoLivroNaoExiste_DeveRetornarVazio() {
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<LivroDTO> result = livroService.update(999L, livro);
        assertFalse(result.isPresent());
        verify(livroRepository, times(1)).findById(999L);
        verify(livroRepository, never()).save(any(Livro.class));
    }
    @Test
    void deleteById_QuandoLivroExiste_DeveDeletarComSucesso() {
        when(livroRepository.existsById(1L)).thenReturn(true);
        doNothing().when(livroRepository).deleteById(1L);
        boolean result = livroService.deleteById(1L);
        assertTrue(result);
        verify(livroRepository, times(1)).existsById(1L);
        verify(livroRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteById_QuandoLivroNaoExiste_DeveRetornarFalse() {
        when(livroRepository.existsById(999L)).thenReturn(false);
        boolean result = livroService.deleteById(999L);
        assertFalse(result);
        verify(livroRepository, times(1)).existsById(999L);
        verify(livroRepository, never()).deleteById(anyLong());
    }
}
