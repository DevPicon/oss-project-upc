package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de proveedores.
 * Incluye métodos para buscar por RUC y razón social.
 */
@Repository
public interface CatProveedorRepository extends JpaRepository<CatProveedor, Integer> {

    /**
     * Busca un proveedor por su código único.
     * @param codigo código del proveedor
     * @return Optional con el proveedor encontrado o vacío
     */
    Optional<CatProveedor> findByCodigo(String codigo);

    /**
     * Verifica si existe un proveedor con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los proveedores activos.
     * @return lista de proveedores con activo = true
     */
    List<CatProveedor> findByActivoTrue();

    /**
     * Busca proveedor por RUC.
     * @param ruc RUC del proveedor
     * @return Optional con el proveedor encontrado o vacío
     */
    Optional<CatProveedor> findByRuc(String ruc);

    /**
     * Verifica si existe un proveedor con el RUC dado.
     * @param ruc RUC a verificar
     * @return true si existe, false si no
     */
    boolean existsByRuc(String ruc);

    /**
     * Busca proveedores por razón social (búsqueda parcial, insensible a mayúsculas).
     * @param razonSocial texto a buscar en la razón social
     * @return lista de proveedores que coinciden
     */
    List<CatProveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
}
