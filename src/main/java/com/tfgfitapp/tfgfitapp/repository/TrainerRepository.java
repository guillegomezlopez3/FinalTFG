package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Trainer> findByUserId(Long userId);
}

