package com.biblioteca.service;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
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
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioService usuarioService;
    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setTelefone("11999999999");
        usuario.setDataCadastro(LocalDate.now());
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@email.com");
        usuarioDTO.setTelefone("11999999999");
        usuarioDTO.setDataCadastro(LocalDate.now());
    }
    @Test
    void findAll_DeveRetornarListaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<UsuarioDTO> result = usuarioService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuario.getNome(), result.get(0).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }
    @Test
    void findById_QuandoUsuarioExiste_DeveRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Optional<UsuarioDTO> result = usuarioService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(usuario.getNome(), result.get().getNome());
        verify(usuarioRepository, times(1)).findById(1L);
    }
    @Test
    void findById_QuandoUsuarioNaoExiste_DeveRetornarVazio() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<UsuarioDTO> result = usuarioService.findById(999L);
        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findById(999L);
    }
    @Test
    void save_QuandoUsuarioValido_DeveSalvarComSucesso() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Maria Santos");
        novoUsuario.setEmail("maria@email.com");
        novoUsuario.setTelefone("11888888888");
        novoUsuario.setDataCadastro(null);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        UsuarioDTO result = usuarioService.save(novoUsuario);
        assertNotNull(result);
        assertEquals(usuario.getNome(), result.getNome());
        assertNotNull(novoUsuario.getDataCadastro());
        verify(usuarioRepository, times(1)).findByEmail(novoUsuario.getEmail());
        verify(usuarioRepository, times(1)).save(novoUsuario);
    }
    @Test
    void save_QuandoEmailJaExiste_DeveLancarExcecao() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("João Silva");
        novoUsuario.setEmail("joao@email.com");
        novoUsuario.setTelefone("11999999999");
        when(usuarioRepository.findByEmail(novoUsuario.getEmail())).thenReturn(Optional.of(usuario));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.save(novoUsuario);
        });
        assertEquals("Email já cadastrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(novoUsuario.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
    @Test
    void update_QuandoUsuarioExiste_DeveAtualizarComSucesso() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("João Silva Atualizado");
        usuarioAtualizado.setEmail("joao.novo@email.com");
        usuarioAtualizado.setTelefone("11999999999");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);
        Optional<UsuarioDTO> result = usuarioService.update(1L, usuarioAtualizado);
        assertTrue(result.isPresent());
        assertEquals(usuarioAtualizado.getNome(), result.get().getNome());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    @Test
    void update_QuandoUsuarioNaoExiste_DeveRetornarVazio() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<UsuarioDTO> result = usuarioService.update(999L, usuario);
        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
    @Test
    void deleteById_QuandoUsuarioExiste_DeveDeletarComSucesso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);
        boolean result = usuarioService.deleteById(1L);
        assertTrue(result);
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteById_QuandoUsuarioNaoExiste_DeveRetornarFalse() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        boolean result = usuarioService.deleteById(999L);
        assertFalse(result);
        verify(usuarioRepository, times(1)).existsById(999L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
