package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.UsuarioRepository;

import java.util.List;

/**
 * Servicio para gestión de usuarios del sistema.
 * Proporciona operaciones CRUD y lógica de negocio para autenticación.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Retorna todos los usuarios del sistema.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        log.debug("Buscando todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Retorna solo los usuarios activos.
     */
    @Transactional(readOnly = true)
    public List<Usuario> findAllActivos() {
        log.debug("Buscando usuarios activos");
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Busca un usuario por su ID.
     */
    @Transactional(readOnly = true)
    public Usuario findById(Integer id) {
        log.debug("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Busca un usuario por su username.
     */
    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        log.debug("Buscando usuario por username: {}", username);
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Busca un usuario por su email.
     */
    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    /**
     * Crea un nuevo usuario en el sistema.
     */
    public Usuario create(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new DuplicateResourceException("Ya existe un usuario con el username: " + usuario.getUsername());
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     */
    public Usuario update(Integer id, Usuario usuario) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario existing = findById(id);

        if (!existing.getUsername().equals(usuario.getUsername()) &&
            usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new DuplicateResourceException("Ya existe un usuario con el username: " + usuario.getUsername());
        }

        if (!existing.getEmail().equals(usuario.getEmail()) &&
            usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        existing.setUsername(usuario.getUsername());
        existing.setEmail(usuario.getEmail());
        existing.setNombreCompleto(usuario.getNombreCompleto());
        existing.setActivo(usuario.getActivo());

        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().isEmpty()) {
            existing.setPasswordHash(usuario.getPasswordHash());
        }

        return usuarioRepository.save(existing);
    }

    /**
     * Desactiva un usuario (soft delete).
     */
    public void deactivate(Integer id) {
        log.info("Desactivando usuario con ID: {}", id);
        Usuario usuario = findById(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Elimina permanentemente un usuario.
     */
    public void delete(Integer id) {
        log.warn("Eliminando permanentemente usuario con ID: {}", id);
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }
}
