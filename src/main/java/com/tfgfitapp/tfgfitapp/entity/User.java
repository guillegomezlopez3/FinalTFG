package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entidad base del sistema. Representa a cualquier usuario (admin, trainer o client).
 * Implementa UserDetails para integrarse directamente con Spring Security.
 *
 * NOTA: Se implementa UserDetails aquí para simplificar la arquitectura.
 * El email actúa como nombre de usuario (username).
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    public User() {}

    public User(Long id, String name, String email, String password, Role role, Boolean active, String avatar, Boolean emailConfirmed, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.avatar = avatar;
        this.emailConfirmed = emailConfirmed;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    // No usar @Enumerated aquí: el RoleConverter (autoApply=true) hace la conversión
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 255)
    private String avatar;

    @Column(name = "email_confirmed", nullable = false)
    private Boolean emailConfirmed = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relaciones inversas (no se incluyen en la serialización por defecto)
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trainer trainer;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Client client;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Manual Getters/Setters for compatibility when Lombok fails
    /** @return El identificador único del usuario. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El nombre completo del usuario. */
    public String getName() { return name; }
    /** @param name El nuevo nombre a asignar. */
    public void setName(String name) { this.name = name; }
    /** @return El correo electrónico del usuario. */
    public String getEmail() { return email; }
    /** @param email El nuevo email a asignar. */
    public void setEmail(String email) { this.email = email; }
    /** @param password La nueva contraseña cifrada a asignar. */
    public void setPassword(String password) { this.password = password; }
    /** @return El rol asignado al usuario. */
    public Role getRole() { return role; }
    /** @param role El nuevo rol a asignar. */
    public void setRole(Role role) { this.role = role; }
    /** @return true si el usuario está activo, false en caso contrario. */
    public Boolean getActive() { return active; }
    /** @param active El nuevo estado de activación. */
    public void setActive(Boolean active) { this.active = active; }
    /** @return La ruta o URL del avatar del usuario. */
    public String getAvatar() { return avatar; }
    /** @param avatar La nueva ruta del avatar. */
    public void setAvatar(String avatar) { this.avatar = avatar; }
    /** @return true si el email está confirmado, false en caso contrario. */
    public Boolean getEmailConfirmed() { return emailConfirmed; }
    public void setEmailConfirmed(Boolean emailConfirmed) { this.emailConfirmed = emailConfirmed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // ===== UserDetails Methods =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role.name() : "USER")));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.active);
    }
}

