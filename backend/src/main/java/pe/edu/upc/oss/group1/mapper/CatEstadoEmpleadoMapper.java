package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoEmpleadoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoEmpleadoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatEstadoEmpleadoMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatEstadoEmpleado toEntity(CatEstadoEmpleadoRequest request) {
        CatEstadoEmpleado entity = new CatEstadoEmpleado();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatEstadoEmpleadoResponse toResponse(CatEstadoEmpleado entity) {
        return CatEstadoEmpleadoResponse.builder()
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
    public static List<CatEstadoEmpleadoResponse> toResponseList(List<CatEstadoEmpleado> entities) {
        return entities.stream()
                .map(CatEstadoEmpleadoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
