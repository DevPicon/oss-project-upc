package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.ReemplazoDispositivoRequest;
import pe.edu.upc.oss.group1.dto.response.ReemplazoDispositivoResponse;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.ReemplazoDispositivo;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;
import pe.edu.upc.oss.group1.mapper.CatEstadoReemplazoMapper;
import pe.edu.upc.oss.group1.mapper.CatMotivoReemplazoMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre ReemplazoDispositivo entity y DTOs.
 */
public class ReemplazoDispositivoMapper {

    public static ReemplazoDispositivo toEntity(ReemplazoDispositivoRequest request) {
        if (request == null) {
            return null;
        }

        ReemplazoDispositivo reemplazo = new ReemplazoDispositivo();
        reemplazo.setFechaReemplazo(request.getFechaReemplazo());
        reemplazo.setDescripcionMotivo(request.getDescripcionMotivo());

        if (request.getAsignacionOriginalId() != null) {
            AsignacionDispositivo asignacion = new AsignacionDispositivo();
            asignacion.setId(request.getAsignacionOriginalId());
            reemplazo.setAsignacionOriginal(asignacion);
        }

        if (request.getDispositivoReemplazoId() != null) {
            Dispositivo dispositivo = new Dispositivo();
            dispositivo.setId(request.getDispositivoReemplazoId());
            reemplazo.setDispositivoReemplazo(dispositivo);
        }

        if (request.getMotivoReemplazoId() != null) {
            CatMotivoReemplazo motivo = new CatMotivoReemplazo();
            motivo.setId(request.getMotivoReemplazoId());
            reemplazo.setMotivoReemplazo(motivo);
        }

        if (request.getUsuarioRegistraId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(request.getUsuarioRegistraId());
            reemplazo.setUsuarioRegistra(usuario);
        }

        return reemplazo;
    }

    public static ReemplazoDispositivoResponse toResponse(ReemplazoDispositivo entity) {
        if (entity == null) {
            return null;
        }

        return ReemplazoDispositivoResponse.builder()
                .id(entity.getId())
                .asignacionOriginal(AsignacionDispositivoMapper.toResponse(entity.getAsignacionOriginal()))
                .dispositivoOriginal(DispositivoMapper.toResponse(entity.getDispositivoOriginal()))
                .dispositivoReemplazo(DispositivoMapper.toResponse(entity.getDispositivoReemplazo()))
                .empleado(EmpleadoMapper.toResponse(entity.getEmpleado()))
                .motivoReemplazo(CatMotivoReemplazoMapper.toResponse(entity.getMotivoReemplazo()))
                .fechaReemplazo(entity.getFechaReemplazo())
                .descripcionMotivo(entity.getDescripcionMotivo())
                .estadoReemplazo(CatEstadoReemplazoMapper.toResponse(entity.getEstadoReemplazo()))
                .usuarioRegistra(UsuarioMapper.toResponse(entity.getUsuarioRegistra()))
                .pendiente(entity.isPendiente())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<ReemplazoDispositivoResponse> toResponseList(List<ReemplazoDispositivo> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(ReemplazoDispositivoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
