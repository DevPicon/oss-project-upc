package pe.edu.upc.oss.group1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.AsignacionDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoAsignacionRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoDispositivoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AsignacionDispositivoService.
 */
@ExtendWith(MockitoExtension.class)
class AsignacionDispositivoServiceTest {

    @Mock
    private AsignacionDispositivoRepository asignacionRepository;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private DispositivoService dispositivoService;

    @Mock
    private HistorialDispositivoService historialService;

    @Mock
    private CatEstadoAsignacionRepository estadoAsignacionRepository;

    @Mock
    private CatEstadoDispositivoRepository estadoDispositivoRepository;

    @InjectMocks
    private AsignacionDispositivoService asignacionService;

    private Empleado empleado;
    private Dispositivo dispositivo;
    private AsignacionDispositivo asignacion;
    private CatEstadoAsignacion estadoActiva;
    private CatEstadoDispositivo estadoAsignado;

    @BeforeEach
    void setUp() {
        // Setup empleado
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setCodigoEmpleado("EMP001");

        // Setup dispositivo
        dispositivo = new Dispositivo();
        dispositivo.setId(1);
        dispositivo.setCodigoActivo("DEVICE001");

        // Setup estados
        estadoActiva = new CatEstadoAsignacion();
        estadoActiva.setId(1);
        estadoActiva.setCodigo("ACTIVA");

        estadoAsignado = new CatEstadoDispositivo();
        estadoAsignado.setId(1);
        estadoAsignado.setCodigo("ASIGNADO");

        // Setup asignacion
        asignacion = new AsignacionDispositivo();
        asignacion.setDispositivo(dispositivo);
        asignacion.setEmpleado(empleado);
    }

    @Test
    void crear_WhenValidData_ShouldCreateAsignacion() {
        // Arrange
        when(empleadoService.findById(1)).thenReturn(empleado);
        when(dispositivoService.findById(1)).thenReturn(dispositivo);
        when(asignacionRepository.findAsignacionActivaByDispositivo(1)).thenReturn(Optional.empty());
        when(estadoAsignacionRepository.findByCodigo("ACTIVA")).thenReturn(Optional.of(estadoActiva));
        when(estadoDispositivoRepository.findByCodigo("ASIGNADO")).thenReturn(Optional.of(estadoAsignado));
        when(asignacionRepository.save(any(AsignacionDispositivo.class))).thenReturn(asignacion);

        // Act
        AsignacionDispositivo result = asignacionService.crear(asignacion);

        // Assert
        assertNotNull(result);
        verify(asignacionRepository, times(1)).save(any(AsignacionDispositivo.class));
        verify(historialService, times(1)).registrarAsignacion(any(), any(), any());
    }

    @Test
    void crear_WhenDispositivoAlreadyAssigned_ShouldThrowException() {
        // Arrange
        AsignacionDispositivo existingAsignacion = new AsignacionDispositivo();
        when(empleadoService.findById(1)).thenReturn(empleado);
        when(dispositivoService.findById(1)).thenReturn(dispositivo);
        when(asignacionRepository.findAsignacionActivaByDispositivo(1))
                .thenReturn(Optional.of(existingAsignacion));

        // Act & Assert
        assertThrows(BusinessValidationException.class, () -> asignacionService.crear(asignacion));
        verify(asignacionRepository, never()).save(any());
    }

    @Test
    void findById_WhenAsignacionNotFound_ShouldThrowException() {
        // Arrange
        when(asignacionRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> asignacionService.findById(999));
    }

    @Test
    void registrarDevolucion_WhenAsignacionNotActive_ShouldThrowException() {
        // Arrange
        CatEstadoAsignacion estadoDevuelta = new CatEstadoAsignacion();
        estadoDevuelta.setCodigo("DEVUELTA");
        asignacion.setEstadoAsignacion(estadoDevuelta);

        when(asignacionRepository.findByIdWithRelations(1)).thenReturn(Optional.of(asignacion));

        // Act & Assert
        assertThrows(BusinessValidationException.class,
                () -> asignacionService.registrarDevolucion(1, "Observaciones", null));
        verify(asignacionRepository, never()).save(any());
    }
}
