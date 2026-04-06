package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.ProgressRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {

    List<ProgressRecord> findAllByClientIdOrderByRecordDateDesc(Long clientId);

    Page<ProgressRecord> findAllByClientId(Long clientId, Pageable pageable);
}

