package pe.edu.upc.oss.group1.dto.mapper;

import pe.edu.upc.oss.group1.dto.request.DispositivoRequest;
import pe.edu.upc.oss.group1.dto.response.DispositivoResponse;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;
import pe.edu.upc.oss.group1.mapper.CatEstadoDispositivoMapper;
import pe.edu.upc.oss.group1.mapper.CatProveedorMapper;
import pe.edu.upc.oss.group1.mapper.CatTipoDispositivoMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Dispositivo entity y DTOs.
 */
public class DispositivoMapper {

    public static Dispositivo toEntity(DispositivoRequest request) {
        if (request == null) {
            return null;
        }

        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setCodigoActivo(request.getCodigoActivo());
        dispositivo.setNumeroSerie(request.getNumeroSerie());
        dispositivo.setModelo(request.getModelo());
        dispositivo.setEspecificaciones(request.getEspecificaciones());
        dispositivo.setFechaAdquisicion(request.getFechaAdquisicion());
        dispositivo.setValorAdquisicion(request.getValorAdquisicion());
        dispositivo.setObservaciones(request.getObservaciones());

        if (request.getTipoDispositivoId() != null) {
            CatTipoDispositivo tipo = new CatTipoDispositivo();
            tipo.setId(request.getTipoDispositivoId());
            dispositivo.setTipoDispositivo(tipo);
        }

        if (request.getMarcaId() != null) {
            CatMarca marca = new CatMarca();
            marca.setId(request.getMarcaId());
            dispositivo.setMarca(marca);
        }

        if (request.getProveedorId() != null) {
            CatProveedor proveedor = new CatProveedor();
            proveedor.setId(request.getProveedorId());
            dispositivo.setProveedor(proveedor);
        }

        if (request.getEstadoDispositivoId() != null) {
            CatEstadoDispositivo estado = new CatEstadoDispositivo();
            estado.setId(request.getEstadoDispositivoId());
            dispositivo.setEstadoDispositivo(estado);
        }

        return dispositivo;
    }

    public static DispositivoResponse toResponse(Dispositivo entity) {
        if (entity == null) {
            return null;
        }

        return DispositivoResponse.builder()
                .id(entity.getId())
                .codigoActivo(entity.getCodigoActivo())
                .numeroSerie(entity.getNumeroSerie())
                .tipoDispositivo(CatTipoDispositivoMapper.toResponse(entity.getTipoDispositivo()))
                .marca(CatMarcaMapper.toResponse(entity.getMarca()))
                .modelo(entity.getModelo())
                .especificaciones(entity.getEspecificaciones())
                .fechaAdquisicion(entity.getFechaAdquisicion())
                .valorAdquisicion(entity.getValorAdquisicion())
                .proveedor(CatProveedorMapper.toResponse(entity.getProveedor()))
                .observaciones(entity.getObservaciones())
                .estadoDispositivo(CatEstadoDispositivoMapper.toResponse(entity.getEstadoDispositivo()))
                .antiguedadEnAnios(entity.getAntiguedadEnAnios())
                .disponibleParaAsignacion(entity.isDisponibleParaAsignacion())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static List<DispositivoResponse> toResponseList(List<Dispositivo> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(DispositivoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
