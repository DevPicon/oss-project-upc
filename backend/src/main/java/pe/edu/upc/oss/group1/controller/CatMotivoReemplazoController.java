package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatMotivoReemplazoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatMotivoReemplazoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;
import pe.edu.upc.oss.group1.mapper.CatMotivoReemplazoMapper;
import pe.edu.upc.oss.group1.service.CatMotivoReemplazoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de motivos de reemplazo.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/motivos-reemplazo")
@RequiredArgsConstructor
@Slf4j
public class CatMotivoReemplazoController {

    private final CatMotivoReemplazoService catMotivoReemplazoService;

    @GetMapping
    public ResponseEntity<List<CatMotivoReemplazoResponse>> findAll() {
        log.debug("REST request to get all motivos de reemplazo");
        List<CatMotivoReemplazo> motivos = catMotivoReemplazoService.findAll();
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponseList(motivos));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatMotivoReemplazoResponse>> findAllActive() {
        log.debug("REST request to get active motivos de reemplazo");
        List<CatMotivoReemplazo> motivos = catMotivoReemplazoService.findAllActive();
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponseList(motivos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatMotivoReemplazoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get motivo de reemplazo by ID: {}", id);
        CatMotivoReemplazo motivo = catMotivoReemplazoService.findById(id);
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponse(motivo));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatMotivoReemplazoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get motivo de reemplazo by codigo: {}", codigo);
        CatMotivoReemplazo motivo = catMotivoReemplazoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponse(motivo));
    }

    @PostMapping
    public ResponseEntity<CatMotivoReemplazoResponse> create(@Valid @RequestBody CatMotivoReemplazoRequest request) {
        log.debug("REST request to create motivo de reemplazo: {}", request.getCodigo());
        CatMotivoReemplazo motivo = CatMotivoReemplazoMapper.toEntity(request);
        CatMotivoReemplazo created = catMotivoReemplazoService.create(motivo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatMotivoReemplazoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatMotivoReemplazoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatMotivoReemplazoRequest request) {
        log.debug("REST request to update motivo de reemplazo: {}", id);
        CatMotivoReemplazo motivo = CatMotivoReemplazoMapper.toEntity(request);
        CatMotivoReemplazo updated = catMotivoReemplazoService.update(id, motivo);
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete motivo de reemplazo: {}", id);
        catMotivoReemplazoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatMotivoReemplazoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search motivos de reemplazo by nombre: {}", nombre);
        List<CatMotivoReemplazo> motivos = catMotivoReemplazoService.search(nombre);
        return ResponseEntity.ok(CatMotivoReemplazoMapper.toResponseList(motivos));
    }
}
