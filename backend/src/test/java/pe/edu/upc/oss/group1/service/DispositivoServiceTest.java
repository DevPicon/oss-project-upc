package pe.edu.upc.oss.group1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.DispositivoRepository;

import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DispositivoService.
 */
@ExtendWith(MockitoExtension.class)
class DispositivoServiceTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @InjectMocks
    private DispositivoService dispositivoService;

    private Dispositivo dispositivo;

    @BeforeEach
    void setUp() {
        dispositivo = new Dispositivo();
        dispositivo.setId(1);
        dispositivo.setCodigoActivo("DEVICE001");
        dispositivo.setNumeroSerie("SN123456");
        
        CatTipoDispositivo tipo = new CatTipoDispositivo();
        tipo.setId(1);
        tipo.setNombre("Laptop");
        dispositivo.setTipoDispositivo(tipo);

        CatMarca marca = new CatMarca();
        marca.setId(1);
        marca.setNombre("Dell");
        dispositivo.setMarca(marca);

        CatEstadoDispositivo estado = new CatEstadoDispositivo();
        estado.setId(1);
        estado.setNombre("Disponible");
        dispositivo.setEstadoDispositivo(estado);
    }

    @Test
    void findAll_ShouldReturnAllDispositivos() {
        // Arrange
        List<Dispositivo> dispositivos = Arrays.asList(dispositivo, new Dispositivo());
        when(dispositivoRepository.findAll()).thenReturn(dispositivos);

        // Act
        List<Dispositivo> result = dispositivoService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(dispositivoRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenDispositivoExists_ShouldReturnDispositivo() {
        // Arrange
        when(dispositivoRepository.findById(1)).thenReturn(Optional.of(dispositivo));

        // Act
        Dispositivo result = dispositivoService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals("DEVICE001", result.getCodigoActivo());
        verify(dispositivoRepository, times(1)).findById(1);
    }

    @Test
    void findById_WhenDispositivoNotFound_ShouldThrowException() {
        // Arrange
        when(dispositivoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> dispositivoService.findById(999));
    }

    @Test
    void create_WhenValidData_ShouldCreateDispositivo() {
        // Arrange
        when(dispositivoRepository.existsByCodigoActivo("DEVICE001")).thenReturn(false);
        when(dispositivoRepository.save(any(Dispositivo.class))).thenReturn(dispositivo);

        // Act
        Dispositivo result = dispositivoService.create(dispositivo);

        // Assert
        assertNotNull(result);
        verify(dispositivoRepository, times(1)).save(any(Dispositivo.class));
    }

    @Test
    void create_WhenCodigoActivoDuplicate_ShouldThrowException() {
        // Arrange
        when(dispositivoRepository.existsByCodigoActivo("DEVICE001")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> dispositivoService.create(dispositivo));
        verify(dispositivoRepository, never()).save(any());
    }

    @Test
    void findDisponibles_ShouldReturnOnlyAvailableDevices() {
        // Arrange
        List<Dispositivo> disponibles = Arrays.asList(dispositivo);
        when(dispositivoRepository.findDispositivosDisponibles()).thenReturn(disponibles);

        // Act
        List<Dispositivo> result = dispositivoService.findDisponibles();

        // Assert
        assertEquals(1, result.size());
        verify(dispositivoRepository, times(1)).findDispositivosDisponibles();
    }

    @Test
    void findByCodigoActivo_WhenExists_ShouldReturnDispositivo() {
        // Arrange
        when(dispositivoRepository.findByCodigoActivo("DEVICE001")).thenReturn(Optional.of(dispositivo));

        // Act
        Dispositivo result = dispositivoService.findByCodigoActivo("DEVICE001");

        // Assert
        assertNotNull(result);
        assertEquals("DEVICE001", result.getCodigoActivo());
    }

    @Test
    void update_WhenValidData_ShouldUpdateDispositivo() {
        // Arrange
        Dispositivo updatedData = new Dispositivo();
        updatedData.setCodigoActivo("DEVICE001");
        updatedData.setModelo("New Model");
        
        // Set mandatory fields
        CatTipoDispositivo tipo = new CatTipoDispositivo();
        tipo.setId(1);
        updatedData.setTipoDispositivo(tipo);
        
        CatMarca marca = new CatMarca();
        marca.setId(1);
        updatedData.setMarca(marca);
        
        CatEstadoDispositivo estado = new CatEstadoDispositivo();
        estado.setId(1);
        updatedData.setEstadoDispositivo(estado);

        when(dispositivoRepository.findById(1)).thenReturn(Optional.of(dispositivo));
        when(dispositivoRepository.save(any(Dispositivo.class))).thenReturn(dispositivo);

        // Act
        Dispositivo result = dispositivoService.update(1, updatedData);

        // Assert
        assertNotNull(result);
        verify(dispositivoRepository, times(1)).save(any(Dispositivo.class));
    }
}
