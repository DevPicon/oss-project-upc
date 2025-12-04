package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.EmpleadoRequest;
import pe.edu.upc.oss.group1.dto.response.EmpleadoResponse;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;
import pe.edu.upc.oss.group1.mapper.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Empleado entity y DTOs.
 */
public class EmpleadoMapper {

    public static Empleado toEntity(EmpleadoRequest request) {
        if (request == null) {
            return null;
        }

        Empleado empleado = new Empleado();
        empleado.setCodigoEmpleado(request.getCodigoEmpleado());
        empleado.setNombre(request.getNombre());
        empleado.setApellidoPaterno(request.getApellidoPaterno());
        empleado.setApellidoMaterno(request.getApellidoMaterno());
        empleado.setEmail(request.getEmail());
        empleado.setTelefono(request.getTelefono());
        empleado.setFechaIngreso(request.getFechaIngreso());

        if (request.getAreaId() != null) {
            CatArea area = new CatArea();
            area.setId(request.getAreaId());
            empleado.setArea(area);
        }

        if (request.getPuestoId() != null) {
            CatPuesto puesto = new CatPuesto();
            puesto.setId(request.getPuestoId());
            empleado.setPuesto(puesto);
        }

        if (request.getSedeId() != null) {
            CatSede sede = new CatSede();
            sede.setId(request.getSedeId());
            empleado.setSede(sede);
        }

        if (request.getEstadoEmpleadoId() != null) {
            CatEstadoEmpleado estado = new CatEstadoEmpleado();
            estado.setId(request.getEstadoEmpleadoId());
            empleado.setEstadoEmpleado(estado);
        }

        return empleado;
    }

    public static EmpleadoResponse toResponse(Empleado entity) {
        if (entity == null) {
            return null;
        }

        return EmpleadoResponse.builder()
                .id(entity.getId())
                .codigoEmpleado(entity.getCodigoEmpleado())
                .nombre(entity.getNombre())
                .apellidoPaterno(entity.getApellidoPaterno())
                .apellidoMaterno(entity.getApellidoMaterno())
                .nombreCompleto(entity.getNombreCompleto())
                .area(CatAreaMapper.toResponse(entity.getArea()))
                .puesto(CatPuestoMapper.toResponse(entity.getPuesto()))
                .sede(CatSedeMapper.toResponse(entity.getSede()))
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .fechaIngreso(entity.getFechaIngreso())
                .estadoEmpleado(CatEstadoEmpleadoMapper.toResponse(entity.getEstadoEmpleado()))
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<EmpleadoResponse> toResponseList(List<Empleado> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(EmpleadoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
