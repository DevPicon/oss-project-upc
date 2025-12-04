package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatAreaRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatAreaResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Maneja la relación jerárquica con áreas superiores.
 */
public class CatAreaMapper {

    /**
     * Convierte un Request DTO a Entity.
     * Nota: La relación con área superior se establece en el servicio.
     */
    public static CatArea toEntity(CatAreaRequest request) {
        CatArea entity = new CatArea();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     * Incluye información del área superior si existe.
     */
    public static CatAreaResponse toResponse(CatArea entity) {
        return CatAreaResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .areaSuperiorId(entity.getAreaSuperior() != null ? entity.getAreaSuperior().getId() : null)
                .areaSuperiorNombre(entity.getAreaSuperior() != null ? entity.getAreaSuperior().getNombre() : null)
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatAreaResponse> toResponseList(List<CatArea> entities) {
        return entities.stream()
                .map(CatAreaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
