package pe.edu.upc.oss.group1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.UsuarioRepository;

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

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("jperez");
        usuario.setEmail("jperez@example.com");
        usuario.setNombreCompleto("Juan Perez");
        usuario.setActivo(true);
        usuario.setPasswordHash("hashed_password");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<Usuario> result = usuarioService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void findAllActivos_ShouldReturnActiveUsersOnly() {
        when(usuarioRepository.findByActivoTrue()).thenReturn(Arrays.asList(usuario));

        List<Usuario> result = usuarioService.findAllActivos();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(usuarioRepository).findByActivoTrue();
    }

    @Test
    void findById_WhenExists_ShouldReturnUser() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findById(99));
    }

    @Test
    void findByUsername_WhenExists_ShouldReturnUser() {
        when(usuarioRepository.findByUsername("jperez")).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByUsername("jperez");

        assertNotNull(result);
        assertEquals("jperez", result.getUsername());
    }

    @Test
    void findByUsername_WhenNotExists_ShouldThrowException() {
        when(usuarioRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findByUsername("unknown"));
    }

    @Test
    void findByEmail_WhenExists_ShouldReturnUser() {
        when(usuarioRepository.findByEmail("jperez@example.com")).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByEmail("jperez@example.com");

        assertNotNull(result);
        assertEquals("jperez@example.com", result.getEmail());
    }

    @Test
    void findByEmail_WhenNotExists_ShouldThrowException() {
        when(usuarioRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findByEmail("unknown@example.com"));
    }

    @Test
    void create_WhenValid_ShouldCreateUser() {
        when(usuarioRepository.existsByUsername(any())).thenReturn(false);
        when(usuarioRepository.existsByEmail(any())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.create(usuario);

        assertNotNull(result);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void create_WhenUsernameExists_ShouldThrowException() {
        when(usuarioRepository.existsByUsername(usuario.getUsername())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.create(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_WhenEmailExists_ShouldThrowException() {
        when(usuarioRepository.existsByUsername(usuario.getUsername())).thenReturn(false);
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.create(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void update_WhenValid_ShouldUpdateUser() {
        Usuario updatedData = new Usuario();
        updatedData.setUsername("jperez_updated");
        updatedData.setEmail("jperez@example.com"); // Same email
        updatedData.setNombreCompleto("Juan Perez Updated");
        updatedData.setPasswordHash("new_hash");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByUsername("jperez_updated")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario result = usuarioService.update(1, updatedData);

        assertEquals("jperez_updated", result.getUsername());
        assertEquals("new_hash", result.getPasswordHash());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void update_WhenUsernameDuplicate_ShouldThrowException() {
        Usuario updatedData = new Usuario();
        updatedData.setUsername("existing_user");
        updatedData.setEmail("jperez@example.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByUsername("existing_user")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.update(1, updatedData));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void update_WhenEmailDuplicate_ShouldThrowException() {
        Usuario updatedData = new Usuario();
        updatedData.setUsername("jperez"); // Same username
        updatedData.setEmail("existing@example.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.update(1, updatedData));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deactivate_ShouldSetActivoFalse() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.deactivate(1);

        assertFalse(usuario.getActivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        usuarioService.delete(1);

        verify(usuarioRepository).delete(usuario);
    }
}
