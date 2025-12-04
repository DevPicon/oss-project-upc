package pe.edu.upc.oss.group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.upc.oss.group1.dto.request.DispositivoRequest;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.service.DispositivoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for DispositivoController.
 */
@WebMvcTest(DispositivoController.class)
class DispositivoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DispositivoService dispositivoService;

    private Dispositivo dispositivo;

    @BeforeEach
    void setUp() {
        dispositivo = new Dispositivo();
        dispositivo.setId(1);
        dispositivo.setCodigoActivo("DEVICE001");
        dispositivo.setNumeroSerie("SN123456");
        dispositivo.setModelo("Model X");
    }

    @Test
    void findAll_ShouldReturnListOfDispositivos() throws Exception {
        // Arrange
        List<Dispositivo> dispositivos = Arrays.asList(dispositivo);
        when(dispositivoService.findAll()).thenReturn(dispositivos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dispositivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].codigoActivo").value("DEVICE001"));

        verify(dispositivoService, times(1)).findAll();
    }

    @Test
    void findDisponibles_ShouldReturnAvailableDevices() throws Exception {
        // Arrange
        List<Dispositivo> disponibles = Arrays.asList(dispositivo);
        when(dispositivoService.findDisponibles()).thenReturn(disponibles);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dispositivos/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(dispositivoService, times(1)).findDisponibles();
    }

    @Test
    void findById_WhenDispositivoExists_ShouldReturnDispositivo() throws Exception {
        // Arrange
        when(dispositivoService.findByIdWithRelations(1)).thenReturn(dispositivo);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dispositivos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigoActivo").value("DEVICE001"));

        verify(dispositivoService, times(1)).findByIdWithRelations(1);
    }

    @Test
    void findByCodigoActivo_WhenExists_ShouldReturnDispositivo() throws Exception {
        // Arrange
        when(dispositivoService.findByCodigoActivo("DEVICE001")).thenReturn(dispositivo);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dispositivos/codigo/DEVICE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoActivo").value("DEVICE001"));

        verify(dispositivoService, times(1)).findByCodigoActivo("DEVICE001");
    }

    @Test
    void create_WhenValidData_ShouldReturn201() throws Exception {
        // Arrange
        DispositivoRequest request = new DispositivoRequest();
        request.setCodigoActivo("DEVICE002");
        request.setNumeroSerie("SN789012");
        request.setTipoDispositivoId(1);
        request.setMarcaId(1);
        request.setProveedorId(1);
        request.setEstadoDispositivoId(1);

        when(dispositivoService.create(any(Dispositivo.class))).thenReturn(dispositivo);

        // Act & Assert
        mockMvc.perform(post("/api/v1/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoActivo").value("DEVICE001"));

        verify(dispositivoService, times(1)).create(any(Dispositivo.class));
    }

    @Test
    void create_WhenDuplicateCodigoActivo_ShouldReturn409() throws Exception {
        // Arrange
        DispositivoRequest request = new DispositivoRequest();
        request.setCodigoActivo("DEVICE001");
        request.setNumeroSerie("SN123456");
        request.setTipoDispositivoId(1);
        request.setMarcaId(1);
        request.setProveedorId(1);
        request.setEstadoDispositivoId(1);

        when(dispositivoService.create(any(Dispositivo.class)))
                .thenThrow(new DuplicateResourceException("Código de activo duplicado"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(dispositivoService, times(1)).create(any(Dispositivo.class));
    }

    @Test
    void update_WhenValidData_ShouldReturn200() throws Exception {
        // Arrange
        DispositivoRequest request = new DispositivoRequest();
        request.setCodigoActivo("DEVICE001");
        request.setNumeroSerie("SN123456");
        request.setTipoDispositivoId(1);
        request.setMarcaId(1);
        request.setProveedorId(1);
        request.setEstadoDispositivoId(1);

        when(dispositivoService.update(eq(1), any(Dispositivo.class))).thenReturn(dispositivo);

        // Act & Assert
        mockMvc.perform(put("/api/v1/dispositivos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(dispositivoService, times(1)).update(eq(1), any(Dispositivo.class));
    }

    @Test
    void delete_WhenDispositivoExists_ShouldReturn204() throws Exception {
        // Arrange
        doNothing().when(dispositivoService).delete(1);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/dispositivos/1"))
                .andExpect(status().isNoContent());

        verify(dispositivoService, times(1)).delete(1);
    }

    @Test
    void findByTipo_ShouldReturnFilteredDevices() throws Exception {
        // Arrange
        List<Dispositivo> dispositivos = Arrays.asList(dispositivo);
        when(dispositivoService.findByTipo(1)).thenReturn(dispositivos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dispositivos/tipo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(dispositivoService, times(1)).findByTipo(1);
    }
}
