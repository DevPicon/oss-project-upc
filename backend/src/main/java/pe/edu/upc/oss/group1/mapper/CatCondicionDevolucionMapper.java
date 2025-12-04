package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatCondicionDevolucionRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatCondicionDevolucionResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatCondicionDevolucionMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatCondicionDevolucion toEntity(CatCondicionDevolucionRequest request) {
        CatCondicionDevolucion entity = new CatCondicionDevolucion();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatCondicionDevolucionResponse toResponse(CatCondicionDevolucion entity) {
        return CatCondicionDevolucionResponse.builder()
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
    public static List<CatCondicionDevolucionResponse> toResponseList(List<CatCondicionDevolucion> entities) {
        return entities.stream()
                .map(CatCondicionDevolucionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
