package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatEstadoDispositivoRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoDispositivoResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatEstadoDispositivoMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatEstadoDispositivo toEntity(CatEstadoDispositivoRequest request) {
        CatEstadoDispositivo entity = new CatEstadoDispositivo();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setDisponibleAsignacion(request.getDisponibleAsignacion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatEstadoDispositivoResponse toResponse(CatEstadoDispositivo entity) {
        if (entity == null) {
            return null;
        }
        return CatEstadoDispositivoResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .disponibleAsignacion(entity.getDisponibleAsignacion())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatEstadoDispositivoResponse> toResponseList(List<CatEstadoDispositivo> entities) {
        return entities.stream()
                .map(CatEstadoDispositivoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
