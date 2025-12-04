package pe.edu.upc.oss.group1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.EmpleadoRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado empleado;
    private CatArea area;
    private CatPuesto puesto;
    private CatSede sede;
    private CatEstadoEmpleado estado;

    @BeforeEach
    void setUp() {
        area = new CatArea();
        area.setId(1);
        area.setNombre("IT");

        puesto = new CatPuesto();
        puesto.setId(1);
        puesto.setNombre("Developer");

        sede = new CatSede();
        sede.setId(1);
        sede.setNombre("Main Office");

        estado = new CatEstadoEmpleado();
        estado.setId(1);
        estado.setCodigo("ACTIVO");

        empleado = new Empleado();
        empleado.setId(1);
        empleado.setCodigoEmpleado("EMP001");
        empleado.setNombre("John");
        empleado.setApellidoPaterno("Doe");
        empleado.setEmail("john.doe@example.com");
        empleado.setFechaIngreso(LocalDate.now().minusYears(1));
        
        empleado.setArea(area);
        empleado.setPuesto(puesto);
        empleado.setSede(sede);
        empleado.setEstadoEmpleado(estado);
    }

    @Test
    void findAll_ShouldReturnAllEmployees() {
        when(empleadoRepository.findAll()).thenReturn(Arrays.asList(empleado));

        List<Empleado> result = empleadoService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findById_WhenExists_ShouldReturnEmployee() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        Empleado result = empleadoService.findById(1);

        assertNotNull(result);
        assertEquals("EMP001", result.getCodigoEmpleado());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(empleadoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empleadoService.findById(99));
    }

    @Test
    void create_WhenValid_ShouldCreateEmployee() {
        when(empleadoRepository.existsByCodigoEmpleado(any())).thenReturn(false);
        when(empleadoRepository.existsByEmail(any())).thenReturn(false);
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado result = empleadoService.create(empleado);

        assertNotNull(result);
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void create_WhenCodeDuplicate_ShouldThrowException() {
        when(empleadoRepository.existsByCodigoEmpleado("EMP001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> empleadoService.create(empleado));
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void create_WhenEmailDuplicate_ShouldThrowException() {
        when(empleadoRepository.existsByCodigoEmpleado("EMP001")).thenReturn(false);
        when(empleadoRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> empleadoService.create(empleado));
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void create_WhenMissingMandatoryFields_ShouldThrowException() {
        empleado.setArea(null); // Missing Area
        
        // Mocks for duplication checks are not strictly needed if validation fails first depending on order,
        // but good practice to mock successful checks if we want to reach validation logic.
        // In this service implementation, duplicate checks run BEFORE validation.
        when(empleadoRepository.existsByCodigoEmpleado(any())).thenReturn(false);
        when(empleadoRepository.existsByEmail(any())).thenReturn(false);

        assertThrows(BusinessValidationException.class, () -> empleadoService.create(empleado));
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void create_WhenInvalidDates_ShouldThrowException() {
        empleado.setFechaTermino(empleado.getFechaIngreso().minusDays(1)); // Termino before Ingreso

        when(empleadoRepository.existsByCodigoEmpleado(any())).thenReturn(false);
        when(empleadoRepository.existsByEmail(any())).thenReturn(false);

        assertThrows(BusinessValidationException.class, () -> empleadoService.create(empleado));
    }

    @Test
    void update_WhenValid_ShouldUpdateEmployee() {
        Empleado updateData = new Empleado();
        updateData.setCodigoEmpleado("EMP001");
        updateData.setEmail("john.doe@example.com");
        updateData.setNombre("John Updated");
        updateData.setArea(area);
        updateData.setPuesto(puesto);
        updateData.setSede(sede);
        updateData.setEstadoEmpleado(estado);
        updateData.setFechaIngreso(LocalDate.now());

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));
        // exists checks shouldn't trigger because code/email haven't changed
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado result = empleadoService.update(1, updateData);

        assertNotNull(result);
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void update_WhenChangingToDuplicateCode_ShouldThrowException() {
        Empleado updateData = new Empleado();
        updateData.setCodigoEmpleado("EMP002"); // Changing code
        updateData.setEmail("john.doe@example.com");

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.existsByCodigoEmpleado("EMP002")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> empleadoService.update(1, updateData));
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        empleadoService.delete(1);

        verify(empleadoRepository).delete(empleado);
    }
}
