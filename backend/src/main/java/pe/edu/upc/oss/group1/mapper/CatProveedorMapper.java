package pe.edu.upc.oss.group1.mapper;

import pe.edu.upc.oss.group1.dto.catalogo.request.CatProveedorRequest;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatProveedorResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entity, request DTO y response DTO.
 * Utiliza patrón de mapeo manual sin librerías externas.
 */
public class CatProveedorMapper {

    /**
     * Convierte un Request DTO a Entity.
     */
    public static CatProveedor toEntity(CatProveedorRequest request) {
        CatProveedor entity = new CatProveedor();
        entity.setCodigo(request.getCodigo());
        entity.setRazonSocial(request.getRazonSocial());
        entity.setRuc(request.getRuc());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
        entity.setDireccion(request.getDireccion());
        entity.setActivo(request.getActivo());
        return entity;
    }

    /**
     * Convierte un Entity a Response DTO.
     */
    public static CatProveedorResponse toResponse(CatProveedor entity) {
        if (entity == null) {
            return null;
        }
        return CatProveedorResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .razonSocial(entity.getRazonSocial())
                .ruc(entity.getRuc())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    /**
     * Convierte una lista de entities a lista de response DTOs.
     */
    public static List<CatProveedorResponse> toResponseList(List<CatProveedor> entities) {
        return entities.stream()
                .map(CatProveedorMapper::toResponse)
                .collect(Collectors.toList());
    }
}
