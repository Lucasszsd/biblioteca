package com.biblioteca.controller;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;
    @Autowired
    private ObjectMapper objectMapper;
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
    void findAll_DeveRetornarListaDeUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO);
        when(usuarioService.findAll()).thenReturn(usuarios);
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
        verify(usuarioService, times(1)).findAll();
    }
    @Test
    void findById_QuandoUsuarioExiste_DeveRetornarUsuario() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuarioDTO));
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
        verify(usuarioService, times(1)).findById(1L);
    }
    @Test
    void findById_QuandoUsuarioNaoExiste_DeveRetornar404() throws Exception {
        when(usuarioService.findById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/usuarios/999"))
                .andExpect(status().isNotFound());
        verify(usuarioService, times(1)).findById(999L);
    }
    @Test
    void save_QuandoUsuarioValido_DeveCriarUsuario() throws Exception {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Maria Santos");
        novoUsuario.setEmail("maria@email.com");
        novoUsuario.setTelefone("11888888888");
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioDTO);
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }
    @Test
    void save_QuandoEmailJaExiste_DeveRetornar400() throws Exception {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNome("João Silva");
        usuarioInvalido.setEmail("joao@email.com");
        usuarioInvalido.setTelefone("11999999999");
        when(usuarioService.save(any(Usuario.class)))
                .thenThrow(new IllegalArgumentException("Email já cadastrado"));
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email já cadastrado"));
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }
    @Test
    void update_QuandoUsuarioExiste_DeveAtualizarUsuario() throws Exception {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("João Silva Atualizado");
        usuarioAtualizado.setEmail("joao.novo@email.com");
        usuarioAtualizado.setTelefone("11999999999");
        UsuarioDTO usuarioAtualizadoDTO = new UsuarioDTO();
        usuarioAtualizadoDTO.setId(1L);
        usuarioAtualizadoDTO.setNome("João Silva Atualizado");
        usuarioAtualizadoDTO.setEmail("joao.novo@email.com");
        usuarioAtualizadoDTO.setTelefone("11999999999");
        usuarioAtualizadoDTO.setDataCadastro(LocalDate.now());
        when(usuarioService.update(1L, usuarioAtualizado)).thenReturn(Optional.of(usuarioAtualizadoDTO));
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"));
        verify(usuarioService, times(1)).update(1L, usuarioAtualizado);
    }
    @Test
    void update_QuandoUsuarioNaoExiste_DeveRetornar404() throws Exception {
        when(usuarioService.update(999L, any(Usuario.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound());
        verify(usuarioService, times(1)).update(999L, any(Usuario.class));
    }
    @Test
    void deleteById_QuandoUsuarioExiste_DeveDeletarUsuario() throws Exception {
        when(usuarioService.deleteById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
        verify(usuarioService, times(1)).deleteById(1L);
    }
    @Test
    void deleteById_QuandoUsuarioNaoExiste_DeveRetornar404() throws Exception {
        when(usuarioService.deleteById(999L)).thenReturn(false);
        mockMvc.perform(delete("/api/usuarios/999"))
                .andExpect(status().isNotFound());
        verify(usuarioService, times(1)).deleteById(999L);
    }
}
