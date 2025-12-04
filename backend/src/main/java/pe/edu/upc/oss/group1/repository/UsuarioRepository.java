package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona operaciones CRUD y consultas personalizadas para usuarios del sistema.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su username único.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su email único.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Retorna todos los usuarios activos.
     */
    List<Usuario> findByActivoTrue();

    /**
     * Verifica si existe un usuario con el username dado.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existsByEmail(String email);
}
