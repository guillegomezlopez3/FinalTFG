package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.entity.EmailConfirmationToken;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.repository.EmailConfirmationTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio para la gestión de envíos de correo electrónico.
 * 
 * Maneja la generación de tokens de confirmación de cuenta, envío de emails
 * de bienvenida con credenciales y verificación de direcciones de correo.
 * Soporta un modo de consola para desarrollo sin servidor SMTP.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailConfirmationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @Value("${spring.mail.username:noreply@tfgfitapp.com}")
    private String fromEmail;

    public EmailService(EmailConfirmationTokenRepository tokenRepository,
                        JavaMailSender mailSender) {
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
    }

    /**
     * Envía un email de bienvenida a un nuevo cliente con sus credenciales y enlace de confirmación.
     * 
     * @param clientUser Usuario del cliente recién creado.
     * @param rawPassword Contraseña temporal en texto plano.
     */
    @Transactional
    public void sendClientWelcomeEmail(User clientUser, String rawPassword) {
        // 1. Generar token de confirmación (válido 48h)
        String tokenValue = UUID.randomUUID().toString();
        EmailConfirmationToken token = new EmailConfirmationToken(
                tokenValue,
                clientUser,
                LocalDateTime.now().plusHours(48)
        );
        tokenRepository.save(token);

        // 2. Construir contenido del email
        String confirmUrl = baseUrl + "/api/auth/confirm?token=" + tokenValue;
        String subject = "¡Bienvenido a TFGFitApp! Confirma tu cuenta";
        String htmlContent = buildWelcomeEmailHtml(clientUser.getName(), clientUser.getEmail(), rawPassword, confirmUrl);

        // 3. Enviar o logear
        if (emailEnabled) {
            sendHtmlEmail(clientUser.getEmail(), subject, htmlContent);
        } else {
            log.info("╔══════════════════════════════════════════════════════════════");
            log.info("║ 📧 EMAIL DE BIENVENIDA (modo consola - app.email.enabled=false)");
            log.info("║ Para: {}", clientUser.getEmail());
            log.info("║ Nombre: {}", clientUser.getName());
            log.info("║ Usuario: {}", clientUser.getEmail());
            log.info("║ Contraseña: {}", rawPassword);
            log.info("║ Enlace de confirmación: {}", confirmUrl);
            log.info("╚══════════════════════════════════════════════════════════════");
        }
    }

    /**
     * Verifica el token de confirmación recibido por email.
     * 
     * @param tokenValue Valor del token UUID.
     * @return true si el token es válido y no ha expirado; false en caso contrario.
     */
    @Transactional
    public boolean confirmEmail(String tokenValue) {
        EmailConfirmationToken token = tokenRepository.findByToken(tokenValue)
                .orElse(null);

        if (token == null) {
            log.warn("Token de confirmación no encontrado: {}", tokenValue);
            return false;
        }

        if (token.getConfirmedAt() != null) {
            log.info("Email ya confirmado previamente para usuario: {}", token.getUser().getEmail());
            return true; // Ya estaba confirmado
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token de confirmación expirado para usuario: {}", token.getUser().getEmail());
            return false;
        }

        // Marcar token como usado
        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);

        // Activar cuenta del usuario
        User user = token.getUser();
        user.setEmailConfirmed(true);
        user.setActive(true);

        return true;
    }

    // ===== HELPERS =====

    /**
     * Envía un email HTML con reintentos automáticos (hasta 2 intentos extra) para
     * tolerar fallos de conexión transitorios en entornos cloud.
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromEmail, "TFGFitApp");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                mailSender.send(message);
                log.info("✅ Email enviado correctamente a: {} (intento {})", to, attempt);
                return;
            } catch (MailException e) {
                log.warn("⚠️ Error al enviar email a {} (intento {}/{}): {}", to, attempt, maxAttempts, e.getMessage());
                if (attempt == maxAttempts) {
                    log.error("❌ No se pudo enviar el email a {} tras {} intentos. "
                            + "Verifica que: 1) App Password de Gmail es correcto, "
                            + "2) spring.mail.properties.mail.smtp.starttls.required=true está configurado, "
                            + "3) el servidor tiene salida al puerto 587.", to, maxAttempts);
                } else {
                    try { Thread.sleep(2000L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }
            } catch (Exception e) {
                log.error("❌ Error inesperado al enviar email a {}: {}", to, e.getMessage());
                return;
            }
        }
    }

    private String buildWelcomeEmailHtml(String name, String email, String password, String confirmUrl) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family:'Segoe UI',Arial,sans-serif;background:#f4f5f7;margin:0;padding:0;">
                <div style="max-width:560px;margin:40px auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.08);">
                    <div style="background:linear-gradient(135deg,#FF7A00,#FF5722);padding:32px 24px;text-align:center;">
                        <h1 style="color:#fff;margin:0;font-size:24px;">🏋️ TFGFitApp</h1>
                        <p style="color:rgba(255,255,255,.85);margin:8px 0 0;font-size:14px;">Tu entrenamiento, tu progreso</p>
                    </div>
                    <div style="padding:32px 24px;">
                        <h2 style="color:#1a1a2e;margin:0 0 16px;">¡Hola, %s!</h2>
                        <p style="color:#555;line-height:1.6;">Tu entrenador te ha dado de alta en TFGFitApp. Aquí tienes tus credenciales de acceso:</p>
                        <div style="background:#f8f9fa;border-radius:8px;padding:20px;margin:20px 0;border-left:4px solid #FF7A00;">
                            <p style="margin:0 0 8px;"><strong>📧 Usuario:</strong> %s</p>
                            <p style="margin:0;"><strong>🔑 Contraseña:</strong> %s</p>
                        </div>
                        <p style="color:#555;line-height:1.6;">Para activar tu cuenta, haz clic en el siguiente botón:</p>
                        <div style="text-align:center;margin:24px 0;">
                            <a href="%s" style="display:inline-block;background:#FF7A00;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-weight:700;font-size:15px;">
                                ✅ Confirmar mi cuenta
                            </a>
                        </div>
                        <p style="color:#999;font-size:12px;text-align:center;">Este enlace expira en 48 horas. Si no solicitaste esta cuenta, ignora este mensaje.</p>
                    </div>
                    <div style="background:#f8f9fa;padding:16px 24px;text-align:center;">
                        <p style="color:#999;font-size:12px;margin:0;">© 2026 TFGFitApp · Trabajo de Fin de Grado</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name, email, password, confirmUrl);
    }

    /**
     * Envía un email de bienvenida y activación a un nuevo entrenador.
     * 
     * @param trainerUser Usuario del entrenador recién registrado.
     */
    @Transactional
    public void sendTrainerWelcomeEmail(User trainerUser) {
        // 1. Generar token de confirmación (válido 48h)
        String tokenValue = java.util.UUID.randomUUID().toString();
        EmailConfirmationToken token = new EmailConfirmationToken(
                tokenValue,
                trainerUser,
                LocalDateTime.now().plusHours(48)
        );
        tokenRepository.save(token);

        // 2. Construir contenido del email
        String confirmUrl = baseUrl + "/api/auth/confirm?token=" + tokenValue;
        String subject = "¡Bienvenido a TFGFitApp! Activa tu cuenta de Entrenador";
        String htmlContent = buildTrainerWelcomeEmailHtml(trainerUser.getName(), confirmUrl);

        // 3. Enviar o logear
        if (emailEnabled) {
            sendHtmlEmail(trainerUser.getEmail(), subject, htmlContent);
        } else {
            log.info("╔══════════════════════════════════════════════════════════════");
            log.info("║ 📧 EMAIL DE ACTIVACIÓN DE ENTRENADOR (modo consola - app.email.enabled=false)");
            log.info("║ Para: {}", trainerUser.getEmail());
            log.info("║ Nombre: {}", trainerUser.getName());
            log.info("║ Enlace de confirmación: {}", confirmUrl);
            log.info("╚══════════════════════════════════════════════════════════════");
        }
    }

    private String buildTrainerWelcomeEmailHtml(String name, String confirmUrl) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family:'Segoe UI',Arial,sans-serif;background:#f4f5f7;margin:0;padding:0;">
                <div style="max-width:560px;margin:40px auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.08);">
                    <div style="background:linear-gradient(135deg,#FF7A00,#FF5722);padding:32px 24px;text-align:center;">
                        <h1 style="color:#fff;margin:0;font-size:24px;">🏋️ TFGFitApp</h1>
                        <p style="color:rgba(255,255,255,.85);margin:8px 0 0;font-size:14px;">Plataforma de Entrenadores Profesionales</p>
                    </div>
                    <div style="padding:32px 24px;">
                        <h2 style="color:#1a1a2e;margin:0 0 16px;">¡Hola, %s!</h2>
                        <p style="color:#555;line-height:1.6;">Gracias por registrarte en TFGFitApp para tus 10 días de prueba gratuita. Para completar tu registro y poder acceder a tu cuenta, por favor confirma tu dirección de correo electrónico haciendo clic en el siguiente botón:</p>
                        <div style="text-align:center;margin:28px 0;">
                            <a href="%s" style="display:inline-block;background:#FF7A00;color:#fff;padding:14px 32px;border-radius:8px;text-decoration:none;font-weight:700;font-size:15px;box-shadow:0 4px 12px rgba(255,122,0,0.2);">
                                ✅ Activar mi cuenta de Entrenador
                            </a>
                        </div>
                        <p style="color:#555;line-height:1.6;">Una vez activada la cuenta, podrás iniciar sesión con las credenciales que creaste durante el registro.</p>
                        <p style="color:#999;font-size:12px;text-align:center;">Este enlace expira en 48 horas. Si no realizaste este registro, puedes ignorar este correo de forma segura.</p>
                    </div>
                    <div style="background:#f8f9fa;padding:16px 24px;text-align:center;">
                        <p style="color:#999;font-size:12px;margin:0;">© 2026 TFGFitApp · Trabajo de Fin de Grado</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(name, confirmUrl);
    }
}
