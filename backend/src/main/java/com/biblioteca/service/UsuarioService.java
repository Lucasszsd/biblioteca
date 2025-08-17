package com.biblioteca.service;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }
    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioDTO::new);
    }
    public UsuarioDTO save(Usuario usuario) {
        if (usuario.getDataCadastro() == null) {
            usuario.setDataCadastro(LocalDate.now());
        }
        validateUsuario(usuario);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new UsuarioDTO(savedUsuario);
    }
    public Optional<UsuarioDTO> update(Long id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existingUsuario -> {
                    validateUsuario(usuario);
                    existingUsuario.setNome(usuario.getNome());
                    existingUsuario.setEmail(usuario.getEmail());
                    existingUsuario.setTelefone(usuario.getTelefone());
                    Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
                    return new UsuarioDTO(updatedUsuario);
                });
    }
    public boolean deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    private void validateUsuario(Usuario usuario) {
        if (usuario.getId() == null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (usuario.getDataCadastro() != null && usuario.getDataCadastro().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de cadastro não pode ser maior que a atual");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (usuario.getEmail() == null || !usuario.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    public Optional<Usuario> findUsuarioEntityById(Long id) {
        return usuarioRepository.findById(id);
    }
}
