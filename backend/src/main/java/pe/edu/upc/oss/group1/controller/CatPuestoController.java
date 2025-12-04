package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatPuestoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatPuestoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;
import pe.edu.upc.oss.group1.mapper.CatPuestoMapper;
import pe.edu.upc.oss.group1.service.CatAreaService;
import pe.edu.upc.oss.group1.service.CatPuestoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de puestos.
 * Incluye endpoints para consultar por área.
 */
@RestController
@RequestMapping("/api/v1/catalogos/puestos")
@RequiredArgsConstructor
@Slf4j
public class CatPuestoController {

    private final CatPuestoService catPuestoService;
    private final CatAreaService catAreaService;

    @GetMapping
    public ResponseEntity<List<CatPuestoResponse>> findAll() {
        log.debug("REST request to get all puestos");
        List<CatPuesto> puestos = catPuestoService.findAll();
        return ResponseEntity.ok(CatPuestoMapper.toResponseList(puestos));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatPuestoResponse>> findAllActive() {
        log.debug("REST request to get active puestos");
        List<CatPuesto> puestos = catPuestoService.findAllActive();
        return ResponseEntity.ok(CatPuestoMapper.toResponseList(puestos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatPuestoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get puesto by ID: {}", id);
        CatPuesto puesto = catPuestoService.findById(id);
        return ResponseEntity.ok(CatPuestoMapper.toResponse(puesto));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatPuestoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get puesto by codigo: {}", codigo);
        CatPuesto puesto = catPuestoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatPuestoMapper.toResponse(puesto));
    }

    @PostMapping
    public ResponseEntity<CatPuestoResponse> create(@Valid @RequestBody CatPuestoRequest request) {
        log.debug("REST request to create puesto: {}", request.getCodigo());
        CatPuesto puesto = CatPuestoMapper.toEntity(request);

        // Establecer área si se proporcionó
        if (request.getAreaId() != null) {
            CatArea area = catAreaService.findById(request.getAreaId());
            puesto.setArea(area);
        }

        CatPuesto created = catPuestoService.create(puesto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatPuestoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatPuestoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatPuestoRequest request) {
        log.debug("REST request to update puesto: {}", id);
        CatPuesto puesto = CatPuestoMapper.toEntity(request);

        // Establecer área si se proporcionó
        if (request.getAreaId() != null) {
            CatArea area = catAreaService.findById(request.getAreaId());
            puesto.setArea(area);
        }

        CatPuesto updated = catPuestoService.update(id, puesto);
        return ResponseEntity.ok(CatPuestoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete puesto: {}", id);
        catPuestoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatPuestoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search puestos by nombre: {}", nombre);
        List<CatPuesto> puestos = catPuestoService.search(nombre);
        return ResponseEntity.ok(CatPuestoMapper.toResponseList(puestos));
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<CatPuestoResponse>> findByArea(@PathVariable Integer areaId) {
        log.debug("REST request to get puestos by area: {}", areaId);
        List<CatPuesto> puestos = catPuestoService.findByArea(areaId);
        return ResponseEntity.ok(CatPuestoMapper.toResponseList(puestos));
    }

    @GetMapping("/area/{areaId}/activos")
    public ResponseEntity<List<CatPuestoResponse>> findActiveByArea(@PathVariable Integer areaId) {
        log.debug("REST request to get active puestos by area: {}", areaId);
        List<CatPuesto> puestos = catPuestoService.findActiveByArea(areaId);
        return ResponseEntity.ok(CatPuestoMapper.toResponseList(puestos));
    }
}
