package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoEmpleadoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoEmpleadoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;
import pe.edu.upc.oss.group1.mapper.CatEstadoEmpleadoMapper;
import pe.edu.upc.oss.group1.service.CatEstadoEmpleadoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de estados de empleado.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/estados-empleado")
@RequiredArgsConstructor
@Slf4j
public class CatEstadoEmpleadoController {

    private final CatEstadoEmpleadoService catEstadoEmpleadoService;

    @GetMapping
    public ResponseEntity<List<CatEstadoEmpleadoResponse>> findAll() {
        log.debug("REST request to get all estados de empleado");
        List<CatEstadoEmpleado> estados = catEstadoEmpleadoService.findAll();
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponseList(estados));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatEstadoEmpleadoResponse>> findAllActive() {
        log.debug("REST request to get active estados de empleado");
        List<CatEstadoEmpleado> estados = catEstadoEmpleadoService.findAllActive();
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponseList(estados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatEstadoEmpleadoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get estado de empleado by ID: {}", id);
        CatEstadoEmpleado estado = catEstadoEmpleadoService.findById(id);
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponse(estado));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatEstadoEmpleadoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get estado de empleado by codigo: {}", codigo);
        CatEstadoEmpleado estado = catEstadoEmpleadoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponse(estado));
    }

    @PostMapping
    public ResponseEntity<CatEstadoEmpleadoResponse> create(@Valid @RequestBody CatEstadoEmpleadoRequest request) {
        log.debug("REST request to create estado de empleado: {}", request.getCodigo());
        CatEstadoEmpleado estado = CatEstadoEmpleadoMapper.toEntity(request);
        CatEstadoEmpleado created = catEstadoEmpleadoService.create(estado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatEstadoEmpleadoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatEstadoEmpleadoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatEstadoEmpleadoRequest request) {
        log.debug("REST request to update estado de empleado: {}", id);
        CatEstadoEmpleado estado = CatEstadoEmpleadoMapper.toEntity(request);
        CatEstadoEmpleado updated = catEstadoEmpleadoService.update(id, estado);
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete estado de empleado: {}", id);
        catEstadoEmpleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatEstadoEmpleadoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search estados de empleado by nombre: {}", nombre);
        List<CatEstadoEmpleado> estados = catEstadoEmpleadoService.search(nombre);
        return ResponseEntity.ok(CatEstadoEmpleadoMapper.toResponseList(estados));
    }
}
