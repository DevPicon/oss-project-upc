package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.CatMarcaRequest;
import pe.edu.upc.oss.group1.dto.response.CatMarcaResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre CatMarca entity y DTOs.
 *
 * Se usa mapeo manual por simplicidad, pero podrías usar MapStruct
 * para mapeo automático en proyectos más grandes.
 *
 * ¿Por qué mappers manuales?
 * - Control total sobre el mapeo
 * - Fácil de entender y mantener
 * - No requiere configuración adicional
 * - Ideal para proyectos pequeños/medianos
 *
 * ¿Cuándo usar MapStruct?
 * - Proyectos grandes con muchos DTOs
 * - Mapeos complejos con transformaciones
 * - Necesitas performance extrema
 */
public class CatMarcaMapper {

    /**
     * Convierte Request DTO a Entity.
     * Se usa al crear o actualizar una marca.
     *
     * @param request DTO con datos del cliente
     * @return Entity para persistir en BD
     */
    public static CatMarca toEntity(CatMarcaRequest request) {
        if (request == null) {
            return null;
        }

        CatMarca marca = new CatMarca();
        marca.setCodigo(request.getCodigo());
        marca.setNombre(request.getNombre());
        marca.setActivo(request.getActivo());
        return marca;
    }

    /**
     * Convierte Entity a Response DTO.
     * Se usa al devolver una marca al cliente.
     *
     * @param entity Entity desde la BD
     * @return DTO para enviar al cliente
     */
    public static CatMarcaResponse toResponse(CatMarca entity) {
        if (entity == null) {
            return null;
        }

        return CatMarcaResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte lista de Entities a lista de Response DTOs.
     * Se usa al devolver múltiples marcas (findAll, search, etc).
     *
     * @param entities Lista de entities
     * @return Lista de DTOs
     */
    public static List<CatMarcaResponse> toResponseList(List<CatMarca> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(CatMarcaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
