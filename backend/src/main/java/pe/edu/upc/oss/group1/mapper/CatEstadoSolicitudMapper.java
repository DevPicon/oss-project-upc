package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoSolicitudRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoSolicitudResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatEstadoSolicitudMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatEstadoSolicitud toEntity(CatEstadoSolicitudRequest request) {
        CatEstadoSolicitud entity = new CatEstadoSolicitud();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatEstadoSolicitudResponse toResponse(CatEstadoSolicitud entity) {
        return CatEstadoSolicitudResponse.builder()
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
    public static List<CatEstadoSolicitudResponse> toResponseList(List<CatEstadoSolicitud> entities) {
        return entities.stream()
                .map(CatEstadoSolicitudMapper::toResponse)
                .collect(Collectors.toList());
    }
}
