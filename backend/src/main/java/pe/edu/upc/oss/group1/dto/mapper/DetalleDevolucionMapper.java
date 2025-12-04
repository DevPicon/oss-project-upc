package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.DetalleDevolucionRequest;
import pe.edu.upc.oss.group1.dto.response.DetalleDevolucionResponse;
import pe.edu.upc.oss.group1.entity.DetalleDevolucion;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;
import pe.edu.upc.oss.group1.mapper.CatCondicionDevolucionMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre DetalleDevolucion entity y DTOs.
 */
public class DetalleDevolucionMapper {

    public static DetalleDevolucion toEntity(DetalleDevolucionRequest request) {
        if (request == null) {
            return null;
        }

        DetalleDevolucion detalle = new DetalleDevolucion();
        detalle.setObservaciones(request.getObservaciones());

        if (request.getSolicitudDevolucionId() != null) {
            SolicitudDevolucion solicitud = new SolicitudDevolucion();
            solicitud.setId(request.getSolicitudDevolucionId());
            detalle.setSolicitudDevolucion(solicitud);
        }

        if (request.getDispositivoId() != null) {
            Dispositivo dispositivo = new Dispositivo();
            dispositivo.setId(request.getDispositivoId());
            detalle.setDispositivo(dispositivo);
        }

        if (request.getCondicionDevolucionId() != null) {
            CatCondicionDevolucion condicion = new CatCondicionDevolucion();
            condicion.setId(request.getCondicionDevolucionId());
            detalle.setCondicionDevolucion(condicion);
        }

        return detalle;
    }

    public static DetalleDevolucionResponse toResponse(DetalleDevolucion entity) {
        if (entity == null) {
            return null;
        }

        return DetalleDevolucionResponse.builder()
                .id(entity.getId())
                .solicitudDevolucion(SolicitudDevolucionMapper.toResponse(entity.getSolicitudDevolucion()))
                .dispositivo(DispositivoMapper.toResponse(entity.getDispositivo()))
                .asignacion(AsignacionDispositivoMapper.toResponse(entity.getAsignacion()))
                .condicionDevolucion(CatCondicionDevolucionMapper.toResponse(entity.getCondicionDevolucion()))
                .observaciones(entity.getObservaciones())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<DetalleDevolucionResponse> toResponseList(List<DetalleDevolucion> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(DetalleDevolucionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
