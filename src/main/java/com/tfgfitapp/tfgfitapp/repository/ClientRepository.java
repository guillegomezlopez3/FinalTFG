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

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    Optional<Client> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "trainer", "trainer.user"})
    List<Client> findAllByTrainerId(Long trainerId);

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

