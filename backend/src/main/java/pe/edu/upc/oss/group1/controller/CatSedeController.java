package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatSedeRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatSedeResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;
import pe.edu.upc.oss.group1.mapper.CatSedeMapper;
import pe.edu.upc.oss.group1.service.CatSedeService;

import java.util.List;

/**
 * Controlador REST para el catálogo de sedes.
 * Incluye endpoints para consultar por ubicación geográfica.
 */
@RestController
@RequestMapping("/api/v1/catalogos/sedes")
@RequiredArgsConstructor
@Slf4j
public class CatSedeController {

    private final CatSedeService catSedeService;

    @GetMapping
    public ResponseEntity<List<CatSedeResponse>> findAll() {
        log.debug("REST request to get all sedes");
        List<CatSede> sedes = catSedeService.findAll();
        return ResponseEntity.ok(CatSedeMapper.toResponseList(sedes));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CatSedeResponse>> findAllActive() {
        log.debug("REST request to get active sedes");
        List<CatSede> sedes = catSedeService.findAllActive();
        return ResponseEntity.ok(CatSedeMapper.toResponseList(sedes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatSedeResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get sede by ID: {}", id);
        CatSede sede = catSedeService.findById(id);
        return ResponseEntity.ok(CatSedeMapper.toResponse(sede));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatSedeResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get sede by codigo: {}", codigo);
        CatSede sede = catSedeService.findByCodigo(codigo);
        return ResponseEntity.ok(CatSedeMapper.toResponse(sede));
    }

    @PostMapping
    public ResponseEntity<CatSedeResponse> create(@Valid @RequestBody CatSedeRequest request) {
        log.debug("REST request to create sede: {}", request.getCodigo());
        CatSede sede = CatSedeMapper.toEntity(request);
        CatSede created = catSedeService.create(sede);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatSedeMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatSedeResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatSedeRequest request) {
        log.debug("REST request to update sede: {}", id);
        CatSede sede = CatSedeMapper.toEntity(request);
        CatSede updated = catSedeService.update(id, sede);
        return ResponseEntity.ok(CatSedeMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete sede: {}", id);
        catSedeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatSedeResponse>> search(@RequestParam String nombre) {
        log.debug("REST request to search sedes by nombre: {}", nombre);
        List<CatSede> sedes = catSedeService.search(nombre);
        return ResponseEntity.ok(CatSedeMapper.toResponseList(sedes));
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<CatSedeResponse>> findByCiudad(@PathVariable String ciudad) {
        log.debug("REST request to get sedes by ciudad: {}", ciudad);
        List<CatSede> sedes = catSedeService.findByCiudad(ciudad);
        return ResponseEntity.ok(CatSedeMapper.toResponseList(sedes));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<CatSedeResponse>> findByPais(@PathVariable String pais) {
        log.debug("REST request to get sedes by pais: {}", pais);
        List<CatSede> sedes = catSedeService.findByPais(pais);
        return ResponseEntity.ok(CatSedeMapper.toResponseList(sedes));
    }
}
