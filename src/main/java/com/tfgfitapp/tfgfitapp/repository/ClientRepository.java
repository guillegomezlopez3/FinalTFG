package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Client}.
 * 
 * Gestiona el acceso a datos de los perfiles de clientes, incluyendo la búsqueda por
 * usuario asociado, filtrado por entrenador y niveles de experiencia.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Busca un cliente por el identificador de su usuario base.
     * 
     * @param userId ID del usuario.
     * @return Un Optional con el cliente si existe.
     */
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Optional<Client> findByUserId(Long userId);

    /**
     * Recupera todos los clientes asignados a un entrenador específico.
     * 
     * @param trainerId ID del entrenador.
     * @return Lista de clientes asociados.
     */
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    List<Client> findAllByTrainerId(Long trainerId);

    /**
     * Recupera una página de clientes asignados a un entrenador específico.
     * 
     * @param trainerId ID del entrenador.
     * @param pageable Configuración de paginación.
     * @return Página de clientes encontrados.
     */
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Page<Client> findAllByTrainerId(Long trainerId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Page<Client> findAllByTrainerIdOrderByCreatedAtDesc(Long trainerId, Pageable pageable);

    /** Filtra los clientes de un entrenador por nivel de experiencia. */
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Page<Client> findAllByTrainerIdAndLevel(Long trainerId, ClientLevel level, Pageable pageable);

    /** Filtra por nivel sin restricción de entrenador (uso ADMIN). */
    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Page<Client> findAllByLevel(ClientLevel level, Pageable pageable);

    long countByTrainerId(Long trainerId);

    long countByActive(Boolean active);
}

