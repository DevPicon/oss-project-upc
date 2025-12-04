package pe.edu.upc.oss.group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upc.oss.group1.dto.request.UsuarioRequest;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.service.UsuarioService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UsuarioController.
 */
@WebMvcTest(UsuarioController.class)
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setActivo(true);
    }

    @Test
    void findAll_ShouldReturnListOfUsuarios() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.findAll()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("testuser"));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void findById_WhenUsuarioExists_ShouldReturnUsuario() throws Exception {
        // Arrange
        when(usuarioService.findById(1)).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(usuarioService, times(1)).findById(1);
    }

    @Test
    void findById_WhenUsuarioNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(usuarioService.findById(999)).thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).findById(999);
    }

    @Test
    void create_WhenValidData_ShouldReturn201() throws Exception {
        // Arrange
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setEmail("new@example.com");
        request.setNombreCompleto("New User");
        request.setActivo(true);

        when(usuarioService.create(any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(usuarioService, times(1)).create(any(Usuario.class));
    }

    @Test
    void create_WhenInvalidData_ShouldReturn400() throws Exception {
        // Arrange - Missing required fields
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername(""); // Invalid: empty

        // Act & Assert
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).create(any());
    }

    @Test
    void update_WhenValidData_ShouldReturn200() throws Exception {
        // Arrange
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername("updateduser");
        request.setPassword("password123");
        request.setEmail("updated@example.com");
        request.setNombreCompleto("Updated User");
        request.setActivo(true);

        when(usuarioService.update(eq(1), any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(usuarioService, times(1)).update(eq(1), any(Usuario.class));
    }

    @Test
    void delete_WhenUsuarioExists_ShouldReturn204() throws Exception {
        // Arrange
        doNothing().when(usuarioService).delete(1);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).delete(1);
    }
}
