# 🚀 MASTER PLAN: Estrategia, Desarrollo y Psicología Visual para TFGFitApp

Como Jefe de Producto y Lead Developer de TFGFitApp, este es el plan maestro para convertir nuestra aplicación de un "buen proyecto" a un **producto de grado empresarial (SaaS), altamente adictivo y visualmente impecable** para entrenadores y clientes.

El diseño se basará en **Carga Cognitiva Baja** (interfaces limpias), **Refuerzo Positivo** (motivación) y **Eficiencia Operativa** (código a prueba de balas).

---

## FASE 1: 🛡️ Cimientos Irrompibles (Estabilización y Seguridad)
*Regla de oro: No podemos construir un rascacielos sobre arena. Antes de añadir la pintura, la estructura debe ser perfecta.*

- [ ] **1.1 Auditoría de Relaciones JPA y Rendimiento:**
  - Asegurar `FetchType.LAZY` en todas las relaciones `@OneToMany` y `@ManyToOne` para evitar el problema N+1.
  - Implementar DTOs estrictos en las respuestas para asegurar que nunca serialicemos bucles infinitos u objetos de base de datos directamente al front.
- [ ] **1.2 Manejo de Errores Silencioso y Elegante:**
  - Refinar el `GlobalExceptionHandler` para que mapee errores complejos de base de datos a mensajes "humanos" (ej. en lugar de *ConstraintViolationException*, mostrar *"Este correo ya está en uso"*).
- [ ] **1.3 Refuerzo de Seguridad y Sesión:**
  - Implementar sistema de **Refresh Tokens** para que los usuarios no pierdan su sesión bruscamente a mitad de un entrenamiento.
  - Recuperación segura de contraseña (flujo "¿Olvidaste tu contraseña?" con token temporal).

---

## FASE 2: 🧠 Psicología Visual y UI/UX (Retención de Usuarios)
*El diseño no es solo cómo se ve, es cómo funciona. Aplicaremos psicología de color y microinteracciones.*

- [ ] **2.1 Paleta de Colores y Tipografía (Branding & Trust):**
  - **Clientes:** Tonos que transmitan energía y salud (Acentos en verde esmeralda o azul eléctrico), con un fondo limpio (blanco roto o un **Dark Mode** sofisticado para usar en el gimnasio).
  - **Entrenadores:** Diseño más denso y analítico. Alto contraste para facilitar la lectura de datos de múltiples clientes rápidamente.
- [ ] **2.2 Tableros (Dashboards) Motivacionales (Gamificación):**
  - **Para el Cliente:** Al entrar, mostrar un mensaje dinámico según la hora (*"¡Buenos días, es hora de brillar!"*). Mostrar una barra de progreso que le recuerde lo cerca que está de su objetivo.
  - **Para el Entrenador:** Panel de "Alertas" (ej: *“María no ha registrado su peso en 2 semanas”* o *“Carlos termina su rutina en 3 días”*). Esto potencia el servicio del entrenador.
- [ ] **2.3 Microinteracciones y Estado Fluido:**
  - Añadir skeletons (pantallas de carga grises con siluetas) en lugar de *spinners* clásicos mientras los datos cargan mediante JS. Reduce la percepción de lentitud.
  - Alertas tipo "Toast" en las esquinas inferiores para éxitos/errores (evitar las alertas invasivas en medio de la pantalla).

---

## FASE 3: 🌟 Funcionalidades "Killer" (Propuesta de Valor Única)
*Lo que hará que los entrenadores paguen por esto y los clientes lo amen.*

- [ ] **3.1 Visualización de Progreso (Chart.js):**
  - Gráficos interactivos de líneas para la evolución del peso y la grasa corporal. Ver la curva bajar (o subir, si es hipertrofia) genera un pico de dopamina en el cliente.
- [ ] **3.2 Fotos de Evolución (Visual Feedback):**
  - Permitir al cliente subir una foto de progreso vinculada a su `ProgressRecord`. Una imagen vale más que mil datos. Requiere manejo de subida de archivos (Multipart).
- [ ] **3.3 Motor de Exportación (PDFs Premium):**
  - Botón "Exportar a PDF" para dietas y entrenamientos. El PDF debe incluir el branding de la app y un diseño limpio y profesional para que el cliente pueda imprimirlo o llevarlo en el móvil offline.
- [ ] **3.4 Clonado y Plantillas (Productividad del Entrenador):**
  - Permitir a los entrenadores "Clonar" un workout o dieta exitosa de un cliente a otro de perfil similar. Esto reduce el trabajo del entrenador de horas a minutos.

---

## FASE 4: 🛠️ Operativa Dinámica (Fricción Cero)
*Sin recargar la página, una experiencia casi nativa (Single Page Feel).*

- [ ] **4.1 Drag and Drop (Arrastrar y Soltar):**
  - En la vista de creación de rutinas (`WorkoutPlan`), permitir reorganizar los días (`WorkoutDay`) o los ejercicios (`Exercise`) simplemente arrastrándolos con el ratón.
- [ ] **4.2 Buscadores de Filtrado Rápido (Live Search):**
  - Para los entrenadores: Una barra de búsqueda que filtre clientes al vuelo ("a medida que se teclea") en la lista de gestión de clientes.  
- [ ] **4.3 Avatares y Perfiles Personalizados:**
  - Subida de imagen de perfil para todos los roles (sustituyendo las iniciales genéricas). Aporta calidez y sentido de pertenencia a la aplicación.

---

## FASE 5: 🧪 Calidad, QA y Despliegue Real (Producción)
*Asegurar que nuestro producto funciona perfectamente antes de llegar a las manos del cliente.*

- [ ] **5.1 Testing Estratégico:**
  - Pruebas E2E (End-to-End) en los flujos principales (Login -> Ver Dieta -> Ver Rutina) para garantizar que si cambiamos código, no se rompa la vista.
- [ ] **5.2 Limpieza de Deuda Técnica:**
  - Eliminar código comentado antiguo, estandarizar el idioma de todo el código fuente (inglés para clases/variables, español para respuestas/UI de cliente).
- [ ] **5.3 Responsive Design Extremo (Mobile-First):**
  - El 90% de los clientes verán su rutina en el gimnasio desde el móvil. Asegurarse de que las tablas HTML de ejercicios pasen a formato tarjeta en resoluciones menores de 768px.

---
**Firmado:** *Tu Director de Producto (GitHub Copilot)*
