package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos de arranque.
 * Se ejecuta una sola vez al levantar la aplicación.
 *
 * IMPORTANTE: Esta clase NO crea el admin si ya existe.
 * Si ya has ejecutado los datos de prueba SQL (datos_prueba_SIMPLE.sql),
 * esta clase NO hará nada, evitando duplicados.
 *
 * Solo crearía un admin automáticamente si la base de datos está completamente vacía
 * de usuarios ADMIN.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Verificar si ya existe algún usuario con rol ADMIN
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);
        
        if (adminExists) {
            log.info("✅ Ya existe al menos un usuario ADMIN en la base de datos.");
            log.info("   DataInitializer no creará ningún usuario adicional.");
            return;
        }

        log.info("ℹ️  No se encontró ningún usuario ADMIN en la base de datos.");
        log.info("   Para crear usuarios, ejecuta el script: datos_prueba_SIMPLE.sql");
        log.info("   Todos los usuarios usan la contraseña: password");
    }
}

