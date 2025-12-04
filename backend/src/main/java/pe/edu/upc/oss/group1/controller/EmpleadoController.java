package pe.edu.upc.oss.group1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.EmpleadoMapper;
import pe.edu.upc.oss.group1.dto.request.EmpleadoRequest;
import pe.edu.upc.oss.group1.dto.response.EmpleadoResponse;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.service.EmpleadoService;

import java.util.List;

/**
 * Controller REST para gestión de empleados.
 */
@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Empleados", description = "Gestión de empleados de la organización")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    @Operation(summary = "Listar todos los empleados")
    public ResponseEntity<List<EmpleadoResponse>> findAll() {
        log.info("GET /api/v1/empleados - Obteniendo todos los empleados");
        List<Empleado> empleados = empleadoService.findAll();
        List<EmpleadoResponse> response = EmpleadoMapper.toResponseList(empleados);
        log.info("Se encontraron {} empleados", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar empleados activos")
    public ResponseEntity<List<EmpleadoResponse>> findAllActive() {
        log.info("GET /api/v1/empleados/activos - Obteniendo empleados activos");
        List<Empleado> empleados = empleadoService.findAllActivos();
        List<EmpleadoResponse> response = EmpleadoMapper.toResponseList(empleados);
        log.info("Se encontraron {} empleados activos", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID")
    public ResponseEntity<EmpleadoResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/empleados/{} - Obteniendo empleado por ID", id);
        Empleado empleado = empleadoService.findById(id);
        EmpleadoResponse response = EmpleadoMapper.toResponse(empleado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener empleado por código")
    public ResponseEntity<EmpleadoResponse> findByCodigoEmpleado(@PathVariable String codigo) {
        log.info("GET /api/v1/empleados/codigo/{} - Obteniendo empleado por código", codigo);
        Empleado empleado = empleadoService.findByCodigoEmpleado(codigo);
        EmpleadoResponse response = EmpleadoMapper.toResponse(empleado);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo empleado")
    public ResponseEntity<EmpleadoResponse> create(@Valid @RequestBody EmpleadoRequest request) {
        log.info("POST /api/v1/empleados - Creando empleado: {}", request.getCodigoEmpleado());
        Empleado empleado = EmpleadoMapper.toEntity(request);
        Empleado created = empleadoService.create(empleado);
        EmpleadoResponse response = EmpleadoMapper.toResponse(created);
        log.info("Empleado creado exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado")
    public ResponseEntity<EmpleadoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody EmpleadoRequest request) {
        log.info("PUT /api/v1/empleados/{} - Actualizando empleado", id);
        Empleado empleado = EmpleadoMapper.toEntity(request);
        Empleado updated = empleadoService.update(id, empleado);
        EmpleadoResponse response = EmpleadoMapper.toResponse(updated);
        log.info("Empleado actualizado exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar empleado")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/empleados/{} - Desactivando empleado", id);
        empleadoService.delete(id);
        log.info("Empleado desactivado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
