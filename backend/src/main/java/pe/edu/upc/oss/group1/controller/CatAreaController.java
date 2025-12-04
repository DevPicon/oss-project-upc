package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatAreaRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatAreaResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.mapper.CatAreaMapper;
import pe.edu.upc.oss.group1.service.CatAreaService;

import java.util.List;

/**
 * Controlador REST para el catálogo de áreas.
 * Incluye endpoints para la jerarquía de áreas.
 */
@RestController
@RequestMapping("/api/v1/catalogos/areas")
@RequiredArgsConstructor
@Slf4j
public class CatAreaController {

    private final CatAreaService catAreaService;

    @GetMapping
    public ResponseEntity<List<CatAreaResponse>> findAll() {
        log.debug("REST request to get all areas");
        List<CatArea> areas = catAreaService.findAll();
        return ResponseEntity.ok(CatAreaMapper.toResponseList(areas));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CatAreaResponse>> findAllActive() {
        log.debug("REST request to get active areas");
        List<CatArea> areas = catAreaService.findAllActive();
        return ResponseEntity.ok(CatAreaMapper.toResponseList(areas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatAreaResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get area by ID: {}", id);
        CatArea area = catAreaService.findById(id);
        return ResponseEntity.ok(CatAreaMapper.toResponse(area));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatAreaResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get area by codigo: {}", codigo);
        CatArea area = catAreaService.findByCodigo(codigo);
        return ResponseEntity.ok(CatAreaMapper.toResponse(area));
    }

    @PostMapping
    public ResponseEntity<CatAreaResponse> create(@Valid @RequestBody CatAreaRequest request) {
        log.debug("REST request to create area: {}", request.getCodigo());
        CatArea area = CatAreaMapper.toEntity(request);

        // Establecer área superior si se proporcionó
        if (request.getAreaSuperiorId() != null) {
            CatArea areaSuperior = catAreaService.findById(request.getAreaSuperiorId());
            area.setAreaSuperior(areaSuperior);
        }

        CatArea created = catAreaService.create(area);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatAreaMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatAreaResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatAreaRequest request) {
        log.debug("REST request to update area: {}", id);
        CatArea area = CatAreaMapper.toEntity(request);

        // Establecer área superior si se proporcionó
        if (request.getAreaSuperiorId() != null) {
            CatArea areaSuperior = catAreaService.findById(request.getAreaSuperiorId());
            area.setAreaSuperior(areaSuperior);
        }

        CatArea updated = catAreaService.update(id, area);
        return ResponseEntity.ok(CatAreaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete area: {}", id);
        catAreaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatAreaResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search areas by nombre: {}", nombre);
        List<CatArea> areas = catAreaService.search(nombre);
        return ResponseEntity.ok(CatAreaMapper.toResponseList(areas));
    }

    @GetMapping("/raiz")
    public ResponseEntity<List<CatAreaResponse>> findAreasRaiz() {
        log.debug("REST request to get areas raiz");
        List<CatArea> areas = catAreaService.findAreasRaiz();
        return ResponseEntity.ok(CatAreaMapper.toResponseList(areas));
    }

    @GetMapping("/{areaSuperiorId}/subareas")
    public ResponseEntity<List<CatAreaResponse>> findSubAreas(@PathVariable Integer areaSuperiorId) {
        log.debug("REST request to get subareas of area: {}", areaSuperiorId);
        List<CatArea> areas = catAreaService.findSubAreas(areaSuperiorId);
        return ResponseEntity.ok(CatAreaMapper.toResponseList(areas));
    }
}
