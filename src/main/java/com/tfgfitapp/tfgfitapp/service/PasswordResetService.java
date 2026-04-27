package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.entity.PasswordResetToken;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.repository.PasswordResetTokenRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Token caduca en 1 hora
    private final Long EXPIRATION_MS = 3600000L;

    @Transactional
    public String createPasswordResetTokenForUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // No revelamos si el correo existe o no por seguridad, devolvemos un string vacío
            return "";
        }

        User user = userOptional.get();

        // Borrar el token antiguo si existe para no acumular basura
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(Instant.now().plusMillis(EXPIRATION_MS));

        tokenRepository.save(resetToken);

        return token; // En un escenario real, esto se enviaría por email
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            tokenRepository.delete(resetToken);
            throw new IllegalArgumentException("El token ha expirado");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Eliminamos el token tras usarlo
        tokenRepository.delete(resetToken);
    }
}
