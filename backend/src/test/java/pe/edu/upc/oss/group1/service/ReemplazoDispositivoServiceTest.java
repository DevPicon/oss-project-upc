package pe.edu.upc.oss.group1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.oss.group1.entity.*;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.AsignacionDispositivoRepository;
import pe.edu.upc.oss.group1.repository.ReemplazoDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoAsignacionRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoReemplazoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReemplazoDispositivoServiceTest {

    @Mock
    private ReemplazoDispositivoRepository reemplazoRepository;
    @Mock
    private AsignacionDispositivoRepository asignacionRepository;
    @Mock
    private DispositivoService dispositivoService;
    @Mock
    private HistorialDispositivoService historialService;
    @Mock
    private CatEstadoReemplazoRepository estadoReemplazoRepository;
    @Mock
    private CatEstadoDispositivoRepository estadoDispositivoRepository;
    @Mock
    private CatEstadoAsignacionRepository estadoAsignacionRepository;

    @InjectMocks
    private ReemplazoDispositivoService reemplazoService;

    private ReemplazoDispositivo reemplazo;
    private AsignacionDispositivo asignacionOriginal;
    private Dispositivo dispositivoOriginal;
    private Dispositivo dispositivoReemplazo;
    private Empleado empleado;
    private Usuario usuario;
    private CatEstadoAsignacion estadoAsignacionActiva;
    private CatEstadoReemplazo estadoReemplazoPendiente;

    @BeforeEach
    void setUp() {
        // Entities setup
        empleado = new Empleado();
        empleado.setId(1);
        empleado.setNombre("Juan");
        empleado.setApellidoPaterno("Perez");

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("admin");

        dispositivoOriginal = new Dispositivo();
        dispositivoOriginal.setId(1);
        dispositivoOriginal.setCodigoActivo("OLD001");

        dispositivoReemplazo = new Dispositivo();
        dispositivoReemplazo.setId(2);
        dispositivoReemplazo.setCodigoActivo("NEW001");

        estadoAsignacionActiva = new CatEstadoAsignacion();
        estadoAsignacionActiva.setId(1);
        estadoAsignacionActiva.setCodigo("ACTIVA");

        asignacionOriginal = new AsignacionDispositivo();
        asignacionOriginal.setId(1);
        asignacionOriginal.setDispositivo(dispositivoOriginal);
        asignacionOriginal.setEmpleado(empleado);
        asignacionOriginal.setEstadoAsignacion(estadoAsignacionActiva);

        estadoReemplazoPendiente = new CatEstadoReemplazo();
        estadoReemplazoPendiente.setId(1);
        estadoReemplazoPendiente.setCodigo("PENDIENTE");

        CatMotivoReemplazo motivo = new CatMotivoReemplazo();
        motivo.setId(1);
        motivo.setNombre("Falla técnica");

        reemplazo = new ReemplazoDispositivo();
        reemplazo.setId(1);
        reemplazo.setAsignacionOriginal(asignacionOriginal);
        reemplazo.setDispositivoReemplazo(dispositivoReemplazo);
        reemplazo.setUsuarioRegistra(usuario);
        reemplazo.setEmpleado(empleado);
        reemplazo.setMotivoReemplazo(motivo);
        reemplazo.setEstadoReemplazo(estadoReemplazoPendiente);
        reemplazo.setDispositivoOriginal(dispositivoOriginal);
    }

    @Test
    void crear_WhenValid_ShouldCreateReemplazo() {
        when(asignacionRepository.findByIdWithRelations(1)).thenReturn(Optional.of(asignacionOriginal));
        when(dispositivoService.findById(2)).thenReturn(dispositivoReemplazo);
        // Mock device availability
        dispositivoReemplazo.setEstadoDispositivo(new CatEstadoDispositivo());
        dispositivoReemplazo.getEstadoDispositivo().setDisponibleAsignacion(true);
        
        when(estadoReemplazoRepository.findByCodigo("PENDIENTE")).thenReturn(Optional.of(estadoReemplazoPendiente));
        when(reemplazoRepository.save(any(ReemplazoDispositivo.class))).thenReturn(reemplazo);

        ReemplazoDispositivo result = reemplazoService.crear(reemplazo);

        assertNotNull(result);
        assertEquals("PENDIENTE", result.getEstadoReemplazo().getCodigo());
        verify(reemplazoRepository).save(reemplazo);
    }

    @Test
    void crear_WhenAsignacionNotActive_ShouldThrowException() {
        asignacionOriginal.setEstadoAsignacion(new CatEstadoAsignacion()); // Not ACTIVA
        when(asignacionRepository.findByIdWithRelations(1)).thenReturn(Optional.of(asignacionOriginal));

        assertThrows(BusinessValidationException.class, () -> reemplazoService.crear(reemplazo));
    }

    @Test
    void crear_WhenDispositivoReemplazoNotAvailable_ShouldThrowException() {
        when(asignacionRepository.findByIdWithRelations(1)).thenReturn(Optional.of(asignacionOriginal));
        when(dispositivoService.findById(2)).thenReturn(dispositivoReemplazo);
        
        dispositivoReemplazo.setEstadoDispositivo(new CatEstadoDispositivo());
        dispositivoReemplazo.getEstadoDispositivo().setDisponibleAsignacion(false);

        assertThrows(BusinessValidationException.class, () -> reemplazoService.crear(reemplazo));
    }

    @Test
    void crear_WhenSameDispositivo_ShouldThrowException() {
        when(asignacionRepository.findByIdWithRelations(1)).thenReturn(Optional.of(asignacionOriginal));
        
        // Set replacement device to be the same as original (ID 1)
        reemplazo.getDispositivoReemplazo().setId(1);
        when(dispositivoService.findById(1)).thenReturn(dispositivoOriginal);
        
        // Ensure the original device is "available" for the check, even though logic should fail on ID equality
        dispositivoOriginal.setEstadoDispositivo(new CatEstadoDispositivo());
        dispositivoOriginal.getEstadoDispositivo().setDisponibleAsignacion(true);

        assertThrows(BusinessValidationException.class, () -> reemplazoService.crear(reemplazo));
    }

    @Test
    void ejecutarReemplazo_WhenValid_ShouldProcessReplacement() {
        // Setup mocks for execution
        CatEstadoAsignacion estadoDevuelta = new CatEstadoAsignacion();
        estadoDevuelta.setCodigo("DEVUELTA");
        
        CatEstadoDispositivo estadoDisponible = new CatEstadoDispositivo();
        estadoDisponible.setCodigo("DISPONIBLE");
        
        CatEstadoDispositivo estadoAsignado = new CatEstadoDispositivo();
        estadoAsignado.setCodigo("ASIGNADO");
        
        CatEstadoReemplazo estadoCompletado = new CatEstadoReemplazo();
        estadoCompletado.setCodigo("COMPLETADO");

        when(reemplazoRepository.findByIdWithRelations(1)).thenReturn(Optional.of(reemplazo));
        when(estadoAsignacionRepository.findByCodigo("DEVUELTA")).thenReturn(Optional.of(estadoDevuelta));
        when(estadoAsignacionRepository.findByCodigo("ACTIVA")).thenReturn(Optional.of(estadoAsignacionActiva));
        when(estadoDispositivoRepository.findByCodigo("DISPONIBLE")).thenReturn(Optional.of(estadoDisponible));
        when(estadoDispositivoRepository.findByCodigo("ASIGNADO")).thenReturn(Optional.of(estadoAsignado));
        when(estadoReemplazoRepository.findByCodigo("COMPLETADO")).thenReturn(Optional.of(estadoCompletado));
        when(reemplazoRepository.save(any(ReemplazoDispositivo.class))).thenReturn(reemplazo);

        ReemplazoDispositivo result = reemplazoService.ejecutarReemplazo(1);

        assertNotNull(result);
        verify(asignacionRepository, times(2)).save(any(AsignacionDispositivo.class)); // 1 update, 1 create
        verify(historialService).registrarReemplazo(any(), any(), any(), any(), any());
        verify(historialService).registrarAsignacion(any(), any(), any());
    }

    @Test
    void ejecutarReemplazo_WhenNotPendiente_ShouldThrowException() {
        reemplazo.setEstadoReemplazo(new CatEstadoReemplazo()); // Not PENDIENTE
        when(reemplazoRepository.findByIdWithRelations(1)).thenReturn(Optional.of(reemplazo));

        assertThrows(BusinessValidationException.class, () -> reemplazoService.ejecutarReemplazo(1));
    }

    @Test
    void cancelar_WhenValid_ShouldCancel() {
        CatEstadoReemplazo estadoCancelado = new CatEstadoReemplazo();
        estadoCancelado.setCodigo("CANCELADO");

        when(reemplazoRepository.findById(1)).thenReturn(Optional.of(reemplazo));
        when(estadoReemplazoRepository.findByCodigo("CANCELADO")).thenReturn(Optional.of(estadoCancelado));
        
        reemplazoService.cancelar(1, "Ya no es necesario");

        verify(reemplazoRepository).save(reemplazo);
        assertEquals("CANCELADO", reemplazo.getEstadoReemplazo().getCodigo());
    }

    @Test
    void cancelar_WhenNotPendiente_ShouldThrowException() {
        reemplazo.setEstadoReemplazo(new CatEstadoReemplazo()); // Not PENDIENTE
        when(reemplazoRepository.findById(1)).thenReturn(Optional.of(reemplazo));

        assertThrows(BusinessValidationException.class, () -> reemplazoService.cancelar(1, "Motivo"));
    }
}
