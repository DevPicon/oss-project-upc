package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatSedeRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatSedeResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatSedeMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatSede toEntity(CatSedeRequest request) {
        CatSede entity = new CatSede();
        entity.setCodigo(request.getCodigo());
        entity.setNombre(request.getNombre());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(request.getCiudad());
        entity.setPais(request.getPais());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatSedeResponse toResponse(CatSede entity) {
        return CatSedeResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .pais(entity.getPais())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatSedeResponse> toResponseList(List<CatSede> entities) {
        return entities.stream()
                .map(CatSedeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
