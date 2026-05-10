package com.tfgfitapp.tfgfitapp.security;

import com.tfgfitapp.tfgfitapp.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación de UserDetailsService para Spring Security.
 * Carga el usuario desde la base de datos usando el email como identificador único.
 *
 * NOTA: La entidad User ya implementa UserDetails directamente,
 * por lo que no hace falta crear un wrapper adicional.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    /**
     * Recupera los detalles de un usuario a partir de su correo electrónico.
     * 
     * @param email Correo electrónico del usuario.
     * @return Detalles del usuario encontrado.
     * @throws UsernameNotFoundException Si no existe ningún usuario con ese email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));
    }
}

