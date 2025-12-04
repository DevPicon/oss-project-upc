package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.AsignacionDispositivoRequest;
import pe.edu.upc.oss.group1.dto.response.AsignacionDispositivoResponse;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.mapper.CatEstadoAsignacionMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre AsignacionDispositivo entity y DTOs.
 */
public class AsignacionDispositivoMapper {

    public static AsignacionDispositivo toEntity(AsignacionDispositivoRequest request) {
        if (request == null) {
            return null;
        }

        AsignacionDispositivo asignacion = new AsignacionDispositivo();
        asignacion.setFechaAsignacion(request.getFechaAsignacion());
        asignacion.setObservacionesAsignacion(request.getObservacionesAsignacion());

        if (request.getDispositivoId() != null) {
            Dispositivo dispositivo = new Dispositivo();
            dispositivo.setId(request.getDispositivoId());
            asignacion.setDispositivo(dispositivo);
        }

        if (request.getEmpleadoId() != null) {
            Empleado empleado = new Empleado();
            empleado.setId(request.getEmpleadoId());
            asignacion.setEmpleado(empleado);
        }

        if (request.getUsuarioAsignaId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(request.getUsuarioAsignaId());
            asignacion.setUsuarioAsigna(usuario);
        }

        return asignacion;
    }

    public static AsignacionDispositivoResponse toResponse(AsignacionDispositivo entity) {
        if (entity == null) {
            return null;
        }

        return AsignacionDispositivoResponse.builder()
                .id(entity.getId())
                .dispositivo(DispositivoMapper.toResponse(entity.getDispositivo()))
                .empleado(EmpleadoMapper.toResponse(entity.getEmpleado()))
                .fechaAsignacion(entity.getFechaAsignacion())
                .fechaDevolucion(entity.getFechaDevolucion())
                .estadoAsignacion(CatEstadoAsignacionMapper.toResponse(entity.getEstadoAsignacion()))
                .observacionesAsignacion(entity.getObservacionesAsignacion())
                .observacionesDevolucion(entity.getObservacionesDevolucion())
                .usuarioAsigna(UsuarioMapper.toResponse(entity.getUsuarioAsigna()))
                .usuarioRecibe(UsuarioMapper.toResponse(entity.getUsuarioRecibe()))
                .diasAsignado(entity.getDiasAsignado())
                .activa(entity.isActiva())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<AsignacionDispositivoResponse> toResponseList(List<AsignacionDispositivo> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(AsignacionDispositivoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
