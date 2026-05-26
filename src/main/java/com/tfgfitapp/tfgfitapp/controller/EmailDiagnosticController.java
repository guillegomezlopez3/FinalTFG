package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;

/**
 * Controlador de diagnóstico del sistema de emails.
 *
 * Permite a los administradores verificar que la configuración SMTP está funcionando
 * correctamente en el entorno de producción cloud, sin necesidad de crear cuentas reales.
 *
 * Todos los endpoints están protegidos con rol ADMIN.
 */
@RestController
@RequestMapping("/api/admin/email")
public class EmailDiagnosticController {

    private static final Logger log = LoggerFactory.getLogger(EmailDiagnosticController.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@tfgfitapp.com}")
    private String fromEmail;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String mailHost;

    @Value("${spring.mail.port:587}")
    private int mailPort;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailDiagnosticController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un email de prueba a la dirección indicada.
     * Solo accesible para ADMIN.
     *
     * @param payload Mapa con la clave "email" del destinatario de prueba.
     * @return Resultado del intento de envío.
     */
    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendTestEmail(@RequestBody Map<String, String> payload) {
        String to = payload.get("email");
        if (to == null || to.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "El campo 'email' es requerido"
            ));
        }

        if (!emailEnabled) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "El sistema de email está desactivado (app.email.enabled=false). Actívalo en application.properties.",
                "config", getConfig()
            ));
        }

        long startMs = System.currentTimeMillis();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, "TFGFitApp");
            helper.setTo(to);
            helper.setSubject("✅ Test de Email — TFGFitApp");
            helper.setText(buildTestEmailHtml(), true);
            mailSender.send(message);

            long elapsed = System.currentTimeMillis() - startMs;
            log.info("✅ Email de prueba enviado a {} en {}ms", to, elapsed);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Email enviado correctamente a " + to,
                "elapsedMs", elapsed,
                "config", getConfig()
            ));
        } catch (MailException | MessagingException | java.io.UnsupportedEncodingException e) {
            long elapsed = System.currentTimeMillis() - startMs;
            log.error("❌ Error enviando email de prueba a {}: {}", to, e.getMessage());

            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Error SMTP: " + e.getMessage(),
                "elapsedMs", elapsed,
                "config", getConfig(),
                "troubleshooting", Map.of(
                    "paso1", "Verifica que el App Password de Gmail es correcto (16 chars sin espacios)",
                    "paso2", "Asegúrate de que la cuenta " + fromEmail + " tiene verificación en 2 pasos activa",
                    "paso3", "Comprueba que el servidor cloud permite salida al puerto 587 (STARTTLS)",
                    "paso4", "Si el puerto 587 está bloqueado, intenta el puerto 465 con SSL"
                )
            ));
        }
    }

    /**
     * Devuelve la configuración SMTP actual (sin contraseña).
     */
    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEmailConfig() {
        return ResponseEntity.ok(Map.of(
            "emailEnabled", emailEnabled,
            "config", getConfig()
        ));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Map<String, Object> getConfig() {
        return Map.of(
            "host", mailHost,
            "port", mailPort,
            "from", fromEmail,
            "emailEnabled", emailEnabled,
            "baseUrl", baseUrl
        );
    }

    private String buildTestEmailHtml() {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family:'Segoe UI',Arial,sans-serif;background:#f4f5f7;margin:0;padding:0;">
                <div style="max-width:560px;margin:40px auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.08);">
                    <div style="background:linear-gradient(135deg,#FF7A00,#FF5722);padding:32px 24px;text-align:center;">
                        <h1 style="color:#fff;margin:0;font-size:24px;">🏋️ TFGFitApp</h1>
                        <p style="color:rgba(255,255,255,.85);margin:8px 0 0;font-size:14px;">Sistema de Email — Diagnóstico</p>
                    </div>
                    <div style="padding:32px 24px;">
                        <h2 style="color:#10b981;margin:0 0 16px;">✅ ¡Email funcionando correctamente!</h2>
                        <p style="color:#555;line-height:1.6;">
                            Si estás leyendo esto, el sistema de email de TFGFitApp está configurado y funcionando
                            correctamente desde el servidor cloud.
                        </p>
                        <div style="background:#f0fdf4;border-radius:8px;padding:16px;margin:20px 0;border-left:4px solid #10b981;">
                            <p style="margin:0;color:#065f46;font-size:14px;">
                                ✓ Conexión SMTP establecida<br>
                                ✓ Autenticación correcta<br>
                                ✓ STARTTLS habilitado<br>
                                ✓ Email entregado en bandeja de entrada
                            </p>
                        </div>
                        <p style="color:#999;font-size:12px;text-align:center;">Este es un email de prueba generado desde el panel de administración.</p>
                    </div>
                    <div style="background:#f8f9fa;padding:16px 24px;text-align:center;">
                        <p style="color:#999;font-size:12px;margin:0;">© 2026 TFGFitApp · Trabajo de Fin de Grado</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
