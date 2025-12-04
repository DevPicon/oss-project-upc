package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatMotivoReemplazoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatMotivoReemplazoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatMotivoReemplazoMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatMotivoReemplazo toEntity(CatMotivoReemplazoRequest request) {
        CatMotivoReemplazo entity = new CatMotivoReemplazo();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatMotivoReemplazoResponse toResponse(CatMotivoReemplazo entity) {
        return CatMotivoReemplazoResponse.builder()
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
    public static List<CatMotivoReemplazoResponse> toResponseList(List<CatMotivoReemplazo> entities) {
        return entities.stream()
                .map(CatMotivoReemplazoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
