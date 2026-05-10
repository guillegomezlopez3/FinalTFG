package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Message;
import com.tfgfitapp.tfgfitapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Message}.
 * 
 * Gestiona la persistencia de mensajes del chat y la recuperación de
 * historiales de conversación entre participantes.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Recupera todos los mensajes intercambiados entre dos usuarios específicos.
     * 
     * @param user1 Primer participante.
     * @param user2 Segundo participante.
     * @return Lista de mensajes ordenados por fecha de envío.
     */
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("user1") User user1, @Param("user2") User user2);
}
