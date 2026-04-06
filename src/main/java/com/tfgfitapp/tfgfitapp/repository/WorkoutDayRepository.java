package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {

    List<WorkoutDay> findAllByWorkoutPlanId(Long workoutPlanId);
}

