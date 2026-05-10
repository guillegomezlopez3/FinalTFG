package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link User}.
 * 
 * Encargado de la gestión de las cuentas de usuario, permitiendo búsquedas por email
 * para procesos de autenticación y verificación de existencia de roles.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email Correo electrónico.
     * @return Un Optional con el usuario si existe.
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByRole(Role role);

    java.util.List<User> findByRole(Role role);
}
