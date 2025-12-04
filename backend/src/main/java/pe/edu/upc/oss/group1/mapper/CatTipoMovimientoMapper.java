package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatTipoMovimientoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatTipoMovimientoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatTipoMovimientoMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatTipoMovimiento toEntity(CatTipoMovimientoRequest request) {
        CatTipoMovimiento entity = new CatTipoMovimiento();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatTipoMovimientoResponse toResponse(CatTipoMovimiento entity) {
        return CatTipoMovimientoResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatTipoMovimientoResponse> toResponseList(List<CatTipoMovimiento> entities) {
        return entities.stream()
                .map(CatTipoMovimientoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
