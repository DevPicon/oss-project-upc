package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoAsignacionRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoAsignacionResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatEstadoAsignacionMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatEstadoAsignacion toEntity(CatEstadoAsignacionRequest request) {
        CatEstadoAsignacion entity = new CatEstadoAsignacion();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatEstadoAsignacionResponse toResponse(CatEstadoAsignacion entity) {
        return CatEstadoAsignacionResponse.builder()
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
    public static List<CatEstadoAsignacionResponse> toResponseList(List<CatEstadoAsignacion> entities) {
        return entities.stream()
                .map(CatEstadoAsignacionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
