package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.UsuarioRequest;
import pe.edu.upc.oss.group1.dto.response.UsuarioResponse;
import pe.edu.upc.oss.group1.entity.Usuario;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Usuario entity y DTOs.
 */
public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request) {
        if (request == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setActivo(request.getActivo());
        return usuario;
    }

    public static UsuarioResponse toResponse(Usuario entity) {
        if (entity == null) {
            return null;
        }

        return UsuarioResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .nombreCompleto(entity.getNombreCompleto())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<UsuarioResponse> toResponseList(List<Usuario> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }
}
