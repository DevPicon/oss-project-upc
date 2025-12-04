package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.SolicitudDevolucionRequest;
import pe.edu.upc.oss.group1.dto.response.SolicitudDevolucionResponse;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.mapper.CatEstadoSolicitudMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre SolicitudDevolucion entity y DTOs.
 */
public class SolicitudDevolucionMapper {

    public static SolicitudDevolucion toEntity(SolicitudDevolucionRequest request) {
        if (request == null) {
            return null;
        }

        SolicitudDevolucion solicitud = new SolicitudDevolucion();
        solicitud.setFechaTerminoEmpleado(request.getFechaTerminoEmpleado());
        solicitud.setFechaDevolucionProgramada(request.getFechaDevolucionProgramada());
        solicitud.setObservaciones(request.getObservaciones());

        if (request.getEmpleadoId() != null) {
            Empleado empleado = new Empleado();
            empleado.setId(request.getEmpleadoId());
            solicitud.setEmpleado(empleado);
        }

        if (request.getUsuarioSolicitaId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(request.getUsuarioSolicitaId());
            solicitud.setUsuarioSolicita(usuario);
        }

        return solicitud;
    }

    public static SolicitudDevolucionResponse toResponse(SolicitudDevolucion entity) {
        if (entity == null) {
            return null;
        }

        return SolicitudDevolucionResponse.builder()
                .id(entity.getId())
                .empleado(EmpleadoMapper.toResponse(entity.getEmpleado()))
                .fechaSolicitud(entity.getFechaSolicitud())
                .fechaTerminoEmpleado(entity.getFechaTerminoEmpleado())
                .fechaDevolucionProgramada(entity.getFechaDevolucionProgramada())
                .fechaDevolucionReal(entity.getFechaDevolucionReal())
                .estadoSolicitud(CatEstadoSolicitudMapper.toResponse(entity.getEstadoSolicitud()))
                .observaciones(entity.getObservaciones())
                .usuarioSolicita(UsuarioMapper.toResponse(entity.getUsuarioSolicita()))
                .usuarioRecibe(UsuarioMapper.toResponse(entity.getUsuarioRecibe()))
                .pendiente(entity.isPendiente())
                .completada(entity.isCompletada())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<SolicitudDevolucionResponse> toResponseList(List<SolicitudDevolucion> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(SolicitudDevolucionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
