package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    List<Diet> findAllByClientId(Long clientId);

    List<Diet> findAllByTrainerId(Long trainerId);

    Optional<Diet> findByIdAndTrainerId(Long id, Long trainerId);

    Optional<Diet> findByIdAndClientId(Long id, Long clientId);

    boolean existsByIdAndTrainerId(Long id, Long trainerId);
    
    long countByActive(boolean active);
}
