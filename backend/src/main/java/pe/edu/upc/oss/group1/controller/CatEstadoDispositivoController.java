package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoDispositivoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoDispositivoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.mapper.CatEstadoDispositivoMapper;
import pe.edu.upc.oss.group1.service.CatEstadoDispositivoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de estados de dispositivo.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/estados-dispositivo")
@RequiredArgsConstructor
@Slf4j
public class CatEstadoDispositivoController {

    private final CatEstadoDispositivoService catEstadoDispositivoService;

    @GetMapping
    public ResponseEntity<List<CatEstadoDispositivoResponse>> findAll() {
        log.debug("REST request to get all estados de dispositivo");
        List<CatEstadoDispositivo> estados = catEstadoDispositivoService.findAll();
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponseList(estados));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatEstadoDispositivoResponse>> findAllActive() {
        log.debug("REST request to get active estados de dispositivo");
        List<CatEstadoDispositivo> estados = catEstadoDispositivoService.findAllActive();
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponseList(estados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatEstadoDispositivoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get estado de dispositivo by ID: {}", id);
        CatEstadoDispositivo estado = catEstadoDispositivoService.findById(id);
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponse(estado));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatEstadoDispositivoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get estado de dispositivo by codigo: {}", codigo);
        CatEstadoDispositivo estado = catEstadoDispositivoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponse(estado));
    }

    @PostMapping
    public ResponseEntity<CatEstadoDispositivoResponse> create(@Valid @RequestBody CatEstadoDispositivoRequest request) {
        log.debug("REST request to create estado de dispositivo: {}", request.getCodigo());
        CatEstadoDispositivo estado = CatEstadoDispositivoMapper.toEntity(request);
        CatEstadoDispositivo created = catEstadoDispositivoService.create(estado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatEstadoDispositivoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatEstadoDispositivoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatEstadoDispositivoRequest request) {
        log.debug("REST request to update estado de dispositivo: {}", id);
        CatEstadoDispositivo estado = CatEstadoDispositivoMapper.toEntity(request);
        CatEstadoDispositivo updated = catEstadoDispositivoService.update(id, estado);
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete estado de dispositivo: {}", id);
        catEstadoDispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatEstadoDispositivoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search estados de dispositivo by nombre: {}", nombre);
        List<CatEstadoDispositivo> estados = catEstadoDispositivoService.search(nombre);
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponseList(estados));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<CatEstadoDispositivoResponse>> findEstadosDisponibles() {
        log.debug("REST request to get estados disponibles para asignación");
        List<CatEstadoDispositivo> estados = catEstadoDispositivoService.findEstadosDisponibles();
        return ResponseEntity.ok(CatEstadoDispositivoMapper.toResponseList(estados));
    }
}
