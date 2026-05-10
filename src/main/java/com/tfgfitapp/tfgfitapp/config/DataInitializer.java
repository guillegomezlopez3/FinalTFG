package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos de arranque.
 * Se ejecuta una sola vez al levantar la aplicación.
 *
 * Esta clase se encarga de:
 * 1. Verificar si existe un administrador en el sistema.
 * 2. Si no existe, crearlo utilizando las credenciales configuradas en
 * application.properties.
 * 3. Notificar mediante logs sobre el estado de la inicialización.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Constructor para inyección de dependencias.
     * 
     * @param userRepository  Repositorio de usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     * @param jdbcTemplate    JdbcTemplate para migraciones manuales.
     */
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Método que se ejecuta al iniciar la aplicación.
     * Comprueba la existencia de un administrador y lo crea si es necesario.
     * 
     * @param args Argumentos de la aplicación.
     */
    @Override
    public void run(ApplicationArguments args) {
        // MIGRACIÓN MANUAL: Permitir nulos en predefined_exercise_id y default en is_read
        try {
            log.info("ℹ️ Ejecutando migración manual: permitiendo nulos en exercise_progress...");
            jdbcTemplate.execute("ALTER TABLE exercise_progress MODIFY predefined_exercise_id BIGINT NULL");
            
            log.info("ℹ️ Ejecutando migración manual: añadiendo default a is_read en mensajes...");
            jdbcTemplate.execute("ALTER TABLE messages MODIFY is_read BOOLEAN NOT NULL DEFAULT FALSE");
            
            log.info("✅ Migraciones completadas.");
        } catch (Exception e) {
            log.warn("⚠️ No se pudo ejecutar alguna migración de tabla: {}", e.getMessage());
        }

        // Verificar si ya existe algún usuario con rol ADMIN
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (adminExists) {
            log.info("✅ Ya existe al menos un usuario ADMIN en la base de datos.");
            log.info("   DataInitializer no creará ningún usuario adicional.");
            return;
        }

        log.info("ℹ️  No se encontró ningún usuario ADMIN. Creando administrador por defecto...");

        try {
            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setEmailConfirmed(true);

            userRepository.save(admin);

            log.info("✅ Usuario ADMIN creado con éxito: {}", adminEmail);
            log.info("   Contraseña configurada en application.properties.");
        } catch (Exception e) {
            log.error("❌ Error al crear el usuario ADMIN inicial: {}", e.getMessage());
        }

        log.info("ℹ️  Para cargar datos de prueba adicionales, ejecuta el script: datos_prueba_SIMPLE.sql");

        // RESET DE DATOS PARA PRUEBAS
        log.info("ℹ️  Reseteando contraseñas, activando cuentas y confirmando emails para todos los usuarios...");
        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            u.setPassword(passwordEncoder.encode("password"));
            u.setActive(true);
            u.setEmailConfirmed(true);
            userRepository.save(u);
        }
        log.info("✅ Se han actualizado y activado {} usuarios.", allUsers.size());
    }
}
