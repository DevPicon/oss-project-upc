package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatTipoDispositivoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatTipoDispositivoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;
import pe.edu.upc.oss.group1.mapper.CatTipoDispositivoMapper;
import pe.edu.upc.oss.group1.service.CatTipoDispositivoService;

import java.util.List;

/**
 * Controlador REST para el catálogo de tipos de dispositivo.
 * Expone endpoints CRUD con validación de DTOs.
 */
@RestController
@RequestMapping("/api/v1/catalogos/tipos-dispositivo")
@RequiredArgsConstructor
@Slf4j
public class CatTipoDispositivoController {

    private final CatTipoDispositivoService catTipoDispositivoService;

    @GetMapping
    public ResponseEntity<List<CatTipoDispositivoResponse>> findAll() {
        log.debug("REST request to get all tipos de dispositivo");
        List<CatTipoDispositivo> tipos = catTipoDispositivoService.findAll();
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponseList(tipos));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatTipoDispositivoResponse>> findAllActive() {
        log.debug("REST request to get active tipos de dispositivo");
        List<CatTipoDispositivo> tipos = catTipoDispositivoService.findAllActive();
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponseList(tipos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatTipoDispositivoResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get tipo de dispositivo by ID: {}", id);
        CatTipoDispositivo tipo = catTipoDispositivoService.findById(id);
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponse(tipo));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatTipoDispositivoResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get tipo de dispositivo by codigo: {}", codigo);
        CatTipoDispositivo tipo = catTipoDispositivoService.findByCodigo(codigo);
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponse(tipo));
    }

    @PostMapping
    public ResponseEntity<CatTipoDispositivoResponse> create(@Valid @RequestBody CatTipoDispositivoRequest request) {
        log.debug("REST request to create tipo de dispositivo: {}", request.getCodigo());
        CatTipoDispositivo tipo = CatTipoDispositivoMapper.toEntity(request);
        CatTipoDispositivo created = catTipoDispositivoService.create(tipo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatTipoDispositivoMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatTipoDispositivoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatTipoDispositivoRequest request) {
        log.debug("REST request to update tipo de dispositivo: {}", id);
        CatTipoDispositivo tipo = CatTipoDispositivoMapper.toEntity(request);
        CatTipoDispositivo updated = catTipoDispositivoService.update(id, tipo);
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete tipo de dispositivo: {}", id);
        catTipoDispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatTipoDispositivoResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search tipos de dispositivo by nombre: {}", nombre);
        List<CatTipoDispositivo> tipos = catTipoDispositivoService.search(nombre);
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponseList(tipos));
    }

    @GetMapping("/con-serie")
    public ResponseEntity<List<CatTipoDispositivoResponse>> findTiposConSerie() {
        log.debug("REST request to get tipos de dispositivo que requieren serie");
        List<CatTipoDispositivo> tipos = catTipoDispositivoService.findTiposConSerie();
        return ResponseEntity.ok(CatTipoDispositivoMapper.toResponseList(tipos));
    }
}
