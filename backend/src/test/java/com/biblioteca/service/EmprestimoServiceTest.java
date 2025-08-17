package com.biblioteca.service;
import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.dto.LivroDTO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {
    @Mock
    private EmprestimoRepository emprestimoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LivroRepository livroRepository;
    @InjectMocks
    private EmprestimoService emprestimoService;
    private Usuario usuario;
    private Livro livro;
    private Emprestimo emprestimo;
    private EmprestimoDTO emprestimoDTO;
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setTelefone("11999999999");
        usuario.setDataCadastro(LocalDate.now());
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("O Senhor dos Anéis");
        livro.setAutor("J.R.R. Tolkien");
        livro.setIsbn("978-0-261-10295-4");
        livro.setDataPublicacao(LocalDate.of(1954, 7, 29));
        livro.setCategoria("Fantasia");
        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(15));
        emprestimo.setStatus(Emprestimo.StatusEmprestimo.EMPRESTADO);
        emprestimoDTO = new EmprestimoDTO();
        emprestimoDTO.setId(1L);
        emprestimoDTO.setUsuarioId(1L);
        emprestimoDTO.setLivroId(1L);
        emprestimoDTO.setDataEmprestimo(LocalDate.now());
        emprestimoDTO.setDataDevolucao(LocalDate.now().plusDays(15));
        emprestimoDTO.setStatus("EMPRESTADO");
    }
    @Test
    void findAll_DeveRetornarListaDeEmprestimos() {
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo);
        when(emprestimoRepository.findAll()).thenReturn(emprestimos);
        List<EmprestimoDTO> result = emprestimoService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(emprestimo.getStatus(), result.get(0).getStatus());
        verify(emprestimoRepository, times(1)).findAll();
    }
    @Test
    void findById_QuandoEmprestimoExiste_DeveRetornarEmprestimo() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        Optional<EmprestimoDTO> result = emprestimoService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(emprestimo.getStatus(), result.get().getStatus());
        verify(emprestimoRepository, times(1)).findById(1L);
    }
    @Test
    void findById_QuandoEmprestimoNaoExiste_DeveRetornarVazio() {
        when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<EmprestimoDTO> result = emprestimoService.findById(999L);
        assertFalse(result.isPresent());
        verify(emprestimoRepository, times(1)).findById(999L);
    }
    @Test
    void findByUsuarioId_DeveRetornarEmprestimosDoUsuario() {
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo);
        when(emprestimoRepository.findByUsuarioId(1L)).thenReturn(emprestimos);
        List<EmprestimoDTO> result = emprestimoService.findByUsuarioId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        verify(emprestimoRepository, times(1)).findByUsuarioId(1L);
    }
    @Test
    void findByStatus_DeveRetornarEmprestimosPorStatus() {
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo);
        when(emprestimoRepository.findByStatus(Emprestimo.StatusEmprestimo.EMPRESTADO)).thenReturn(emprestimos);
        List<EmprestimoDTO> result = emprestimoService.findByStatus("EMPRESTADO");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EMPRESTADO", result.get(0).getStatus());
        verify(emprestimoRepository, times(1)).findByStatus(Emprestimo.StatusEmprestimo.EMPRESTADO);
    }
    @Test
    void save_QuandoUsuarioELivroExistem_DeveCriarEmprestimo() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        EmprestimoDTO result = emprestimoService.save(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getUsuarioId());
        assertEquals(1L, result.getLivroId());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).findById(1L);
        verify(emprestimoRepository, times(1)).save(any(Emprestimo.class));
    }
    @Test
    void save_QuandoUsuarioNaoExiste_DeveLancarExcecao() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emprestimoService.save(999L, 1L);
        });
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999L);
        verify(livroRepository, never()).findById(anyLong());
        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }
    @Test
    void save_QuandoLivroNaoExiste_DeveLancarExcecao() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emprestimoService.save(1L, 999L);
        });
        assertEquals("Livro não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).findById(999L);
        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }
    @Test
    void devolver_QuandoEmprestimoExiste_DeveDevolverComSucesso() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        Optional<EmprestimoDTO> result = emprestimoService.devolver(1L);
        assertTrue(result.isPresent());
        assertEquals("DEVOLVIDO", emprestimo.getStatus().toString());
        verify(emprestimoRepository, times(1)).findById(1L);
        verify(emprestimoRepository, times(1)).save(emprestimo);
    }
    @Test
    void devolver_QuandoEmprestimoNaoExiste_DeveRetornarVazio() {
        when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<EmprestimoDTO> result = emprestimoService.devolver(999L);
        assertFalse(result.isPresent());
        verify(emprestimoRepository, times(1)).findById(999L);
        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }
    @Test
    void deleteById_QuandoEmprestimoExiste_DeveDeletarComSucesso() {
        when(emprestimoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(emprestimoRepository).deleteById(1L);
        boolean result = emprestimoService.deleteById(1L);
        assertTrue(result);
        verify(emprestimoRepository, times(1)).existsById(1L);
        verify(emprestimoRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteById_QuandoEmprestimoNaoExiste_DeveRetornarFalse() {
        when(emprestimoRepository.existsById(999L)).thenReturn(false);
        boolean result = emprestimoService.deleteById(999L);
        assertFalse(result);
        verify(emprestimoRepository, times(1)).existsById(999L);
        verify(emprestimoRepository, never()).deleteById(anyLong());
    }
}
