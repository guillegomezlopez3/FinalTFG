package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.ProgressRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.ProgressRecord}.
 * 
 * Acceso a los registros antropométricos históricos de los clientes.
 */
@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {

    /**
     * Obtiene todos los registros de progreso de un cliente ordenados por fecha.
     * 
     * @param clientId ID del cliente.
     * @return Lista de registros ordenados de más reciente a más antiguo.
     */
    List<ProgressRecord> findAllByClientIdOrderByRecordDateDesc(Long clientId);

    Page<ProgressRecord> findAllByClientId(Long clientId, Pageable pageable);
}

