package pe.edu.upc.oss.group1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.catalogo.request.CatProveedorRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatProveedorResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;
import pe.edu.upc.oss.group1.mapper.CatProveedorMapper;
import pe.edu.upc.oss.group1.service.CatProveedorService;

import java.util.List;

/**
 * Controlador REST para el catálogo de proveedores.
 * Incluye endpoints para búsqueda por RUC y razón social.
 */
@RestController
@RequestMapping("/api/v1/catalogos/proveedores")
@RequiredArgsConstructor
@Slf4j
public class CatProveedorController {

    private final CatProveedorService catProveedorService;

    @GetMapping
    public ResponseEntity<List<CatProveedorResponse>> findAll() {
        log.debug("REST request to get all proveedores");
        List<CatProveedor> proveedores = catProveedorService.findAll();
        return ResponseEntity.ok(CatProveedorMapper.toResponseList(proveedores));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CatProveedorResponse>> findAllActive() {
        log.debug("REST request to get active proveedores");
        List<CatProveedor> proveedores = catProveedorService.findAllActive();
        return ResponseEntity.ok(CatProveedorMapper.toResponseList(proveedores));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatProveedorResponse> findById(@PathVariable Integer id) {
        log.debug("REST request to get proveedor by ID: {}", id);
        CatProveedor proveedor = catProveedorService.findById(id);
        return ResponseEntity.ok(CatProveedorMapper.toResponse(proveedor));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CatProveedorResponse> findByCodigo(@PathVariable String codigo) {
        log.debug("REST request to get proveedor by codigo: {}", codigo);
        CatProveedor proveedor = catProveedorService.findByCodigo(codigo);
        return ResponseEntity.ok(CatProveedorMapper.toResponse(proveedor));
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<CatProveedorResponse> findByRuc(@PathVariable String ruc) {
        log.debug("REST request to get proveedor by RUC: {}", ruc);
        CatProveedor proveedor = catProveedorService.findByRuc(ruc);
        return ResponseEntity.ok(CatProveedorMapper.toResponse(proveedor));
    }

    @PostMapping
    public ResponseEntity<CatProveedorResponse> create(@Valid @RequestBody CatProveedorRequest request) {
        log.debug("REST request to create proveedor: {}", request.getCodigo());
        CatProveedor proveedor = CatProveedorMapper.toEntity(request);
        CatProveedor created = catProveedorService.create(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CatProveedorMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatProveedorResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatProveedorRequest request) {
        log.debug("REST request to update proveedor: {}", id);
        CatProveedor proveedor = CatProveedorMapper.toEntity(request);
        CatProveedor updated = catProveedorService.update(id, proveedor);
        return ResponseEntity.ok(CatProveedorMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("REST request to delete proveedor: {}", id);
        catProveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CatProveedorResponse>> search(@RequestParam String razonSocial) {
        log.debug("REST request to search proveedores by razonSocial: {}", razonSocial);
        List<CatProveedor> proveedores = catProveedorService.search(razonSocial);
        return ResponseEntity.ok(CatProveedorMapper.toResponseList(proveedores));
    }
}
