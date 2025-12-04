package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatCondicionDevolucionRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatCondicionDevolucionResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;
import pe.edu.upc.oss.group1.mapper.CatCondicionDevolucionMapper;
import pe.edu.upc.oss.group1.service.CatCondicionDevolucionService;

import java.util.List;

/**
 * Controlador REST para el catálogo de condiciones de devolución.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/condiciones-devolucion")
@RequiredArgsConstructor
@Slf4j
public class CatCondicionDevolucionController {

    private final CatCondicionDevolucionService catCondicionDevolucionService;

    @GetMapping
    public ResponseEntity<List<CatCondicionDevolucionResponse>> findAll() {
        log.debug("REST request to get all condiciones de devolucion");
        List<CatCondicionDevolucion> condiciones = catCondicionDevolucionService.findAll();
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponseList(condiciones));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CatCondicionDevolucionResponse>> findAllActive() {
        log.debug("REST request to get active condiciones de devolucion");
        List<CatCondicionDevolucion> condiciones = catCondicionDevolucionService.findAllActive();
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponseList(condiciones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatCondicionDevolucionResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get condicion de devolucion by ID: {}", id);
        CatCondicionDevolucion condicion = catCondicionDevolucionService.findById(id);
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponse(condicion));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatCondicionDevolucionResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get condicion de devolucion by codigo: {}", codigo);
        CatCondicionDevolucion condicion = catCondicionDevolucionService.findByCodigo(codigo);
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponse(condicion));
    }

    @PostMapping
    public ResponseEntity<CatCondicionDevolucionResponse> create(@Valid @RequestBody CatCondicionDevolucionRequest request) {
        log.debug("REST request to create condicion de devolucion: {}", request.getCodigo());
        CatCondicionDevolucion condicion = CatCondicionDevolucionMapper.toEntity(request);
        CatCondicionDevolucion created = catCondicionDevolucionService.create(condicion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatCondicionDevolucionMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatCondicionDevolucionResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatCondicionDevolucionRequest request) {
        log.debug("REST request to update condicion de devolucion: {}", id);
        CatCondicionDevolucion condicion = CatCondicionDevolucionMapper.toEntity(request);
        CatCondicionDevolucion updated = catCondicionDevolucionService.update(id, condicion);
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete condicion de devolucion: {}", id);
        catCondicionDevolucionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatCondicionDevolucionResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search condiciones de devolucion by nombre: {}", nombre);
        List<CatCondicionDevolucion> condiciones = catCondicionDevolucionService.search(nombre);
        return ResponseEntity.ok(CatCondicionDevolucionMapper.toResponseList(condiciones));
    }
}
