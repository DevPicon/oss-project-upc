package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatPuestoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatPuestoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Maneja la relación con áreas.
 */
public class CatPuestoMapper {

    /**
     * Convierte un Request DTO a Entity.
     * Nota: La relación con área se establece en el servicio.
     */
    public static CatPuesto toEntity(CatPuestoRequest request) {
        CatPuesto entity = new CatPuesto();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     * Incluye información del área si existe.
     */
    public static CatPuestoResponse toResponse(CatPuesto entity) {
        return CatPuestoResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .areaId(entity.getArea() != null ? entity.getArea().getId() : null)
                .areaNombre(entity.getArea() != null ? entity.getArea().getNombre() : null)
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatPuestoResponse> toResponseList(List<CatPuesto> entities) {
        return entities.stream()
                .map(CatPuestoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
