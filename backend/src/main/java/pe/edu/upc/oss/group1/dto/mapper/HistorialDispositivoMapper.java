package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.response.HistorialDispositivoResponse;
import pe.edu.upc.oss.group1.entity.HistorialDispositivo;
import pe.edu.upc.oss.group1.mapper.CatTipoMovimientoMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre HistorialDispositivo entity y DTOs.
 * No se incluye Request DTO porque el historial solo se crea internamente.
 */
public class HistorialDispositivoMapper {

    public static HistorialDispositivoResponse toResponse(HistorialDispositivo entity) {
        if (entity == null) {
            return null;
        }

        return HistorialDispositivoResponse.builder()
                .id(entity.getId())
                .dispositivo(DispositivoMapper.toResponse(entity.getDispositivo()))
                .tipoMovimiento(CatTipoMovimientoMapper.toResponse(entity.getTipoMovimiento()))
                .usuario(UsuarioMapper.toResponse(entity.getUsuario()))
                .fechaMovimiento(entity.getFechaMovimiento())
                .descripcion(entity.getDescripcion())
                .datosAnteriores(entity.getDatosAnteriores())
                .datosNuevos(entity.getDatosNuevos())
                .build();
    }

    public static List<HistorialDispositivoResponse> toResponseList(List<HistorialDispositivo> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(HistorialDispositivoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
