package com.tfgfitapp.tfgfitapp.enumeration;

/**
 * Definición de los roles de usuario dentro del ecosistema de la aplicación.
 * 
 * Determina los permisos de acceso y las funcionalidades disponibles para cada
 * perfil.
 */
public enum Role {
    /**
     * Rol de administrador con acceso total al sistema.
     */
    ADMIN,
    /**
     * Rol de entrenador con permisos de gestión de usuarios y rutinas.
     */
    TRAINER,
    /**
     * Rol de cliente con acceso a sus rutinas y progreso personal.
     */
    CLIENT
}
