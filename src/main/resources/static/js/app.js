/**
 * Archivo de utilidades JS global para TFGFitApp.
 *
 * Responsabilidades:
 * - Gestión del token JWT en localStorage.
 * - Función fetch autenticada (con header Authorization).
 * - Redirección al login si el token no existe o ha expirado.
 */

const API_BASE = '/api';
const TOKEN_KEY = 'tfgfitapp_token';
const USER_KEY  = 'tfgfitapp_user';

// ── Gestión del token ─────────────────────────────────────────────────────────

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function saveToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function getUser() {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
}

function saveUser(user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
}

async function fetchCurrentUserProfile() {
    const token = getToken();
    if (!token) return null;

    const response = await fetch(API_BASE + '/me', {
        headers: { 'Authorization': 'Bearer ' + token }
    });

    if (response.status === 401) {
        clearSession();
        window.location.href = '/login';
        return null;
    }

    if (response.status === 403) {
        throw new Error('No tienes permisos para ver este perfil');
    }

    if (!response.ok) {
        throw new Error('No se pudo cargar el perfil del usuario');
    }

    const profile = await response.json();
    saveUser(profile);
    return profile;
}

function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
}

function isLoggedIn() {
    return !!getToken();
}

// ── Redirección si no autenticado ─────────────────────────────────────────────

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = '/login';
    }
}

// ── Fetch autenticado ─────────────────────────────────────────────────────────

async function apiFetch(path, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': 'Bearer ' + token } : {}),
        ...(options.headers || {})
    };

    const response = await fetch(API_BASE + path, { ...options, headers });

    if (response.status === 401) {
        clearSession();
        window.location.href = '/login';
        return;
    }

    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || errorData.message || `Error ${response.status}: ${response.statusText}`);
    }

    return response;
}

// ── Login ─────────────────────────────────────────────────────────────────────

async function login(email, password) {
    const res = await fetch(API_BASE + '/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });

    if (!res.ok) {
        const err = await res.json();
        throw new Error(err.error || 'Credenciales incorrectas');
    }

    const data = await res.json();
    saveToken(data.token);

    saveUser({
        id: data.userId,
        name: data.name,
        email: data.email,
        role: data.role
    });

    // Cargar perfil completo
    const profile = await fetchCurrentUserProfile();

    return profile || getUser();
}

// ── Register ──────────────────────────────────────────────────────────────────

async function register(name, email, password) {
    const res = await fetch(API_BASE + '/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
    });

    if (!res.ok) {
        const err = await res.json();
        throw new Error(err.error || err.message || 'Error al registrarse');
    }

    return res.json();
}

// ── Logout ────────────────────────────────────────────────────────────────────

function logout() {
    clearSession();
    window.location.href = '/login';
}

// ── Helpers de UI ─────────────────────────────────────────────────────────────

function showAlert(container, message, type = 'danger') {
    container.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>`;
}

function showSpinner(container) {
    container.innerHTML = `
        <div class="text-center py-4">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
        </div>`;
}

function formatDate(isoDate) {
    if (!isoDate) return '—';
    return new Date(isoDate).toLocaleDateString('es-ES');
}

function formatDateTime(isoDateTime) {
    if (!isoDateTime) return '—';
    return new Date(isoDateTime).toLocaleString('es-ES');
}

/**
 * Genera el HTML de un avatar. Si tiene foto la muestra, si no, usa las iniciales.
 * @param {string} name - Nombre del usuario
 * @param {string} avatarUrl - URL de la imagen (opcional)
 * @param {string} size - Clase de tamaño (opcional)
 */
function getAvatarHtml(name, avatarUrl, size = '') {
    if (avatarUrl && avatarUrl !== '/images/default-avatar.svg') {
        return `<img src="${avatarUrl}" alt="${name}" class="avatar-img ${size}" onerror="this.parentElement.innerHTML=getAvatarInitials('${name}', '${size}')">`;
    }
    return getAvatarInitials(name, size);
}

function getAvatarInitials(name, size = '') {
    const initials = (name || '?').split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
    const colors = ['#FF7A00', '#2563eb', '#059669', '#7c3aed', '#db2777', '#ea580c'];
    // Color determinístico basado en el nombre
    let hash = 0;
    for (let i = 0; i < (name || '').length; i++) hash = (name.charCodeAt(i) + ((hash << 5) - hash));
    const color = colors[Math.abs(hash) % colors.length];
    
    return `<div class="avatar-initials ${size}" style="background-color: ${color}">${initials}</div>`;
}

// ── SweetAlert2 Helpers ───────────────────────────────────────────────────────

function confirmAction(title, text, confirmCallback) {
    if (typeof Swal !== 'undefined') {
        Swal.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#FF7A00',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                confirmCallback();
            }
        });
    } else {
        if (confirm(title + '\n' + text)) {
            confirmCallback();
        }
    }
}

function successToast(message) {
    if (typeof Swal !== 'undefined') {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'success',
            title: message,
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
    } else {
        alert(message);
    }
}
