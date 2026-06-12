# 📋 Plan de Implementación - Sistema de Notas ESFE

**Autor:** Bayron  
**Fecha:** 11/06/2025  
**Versión:** 1.0  
**Metodología:** Desarrollo incremental por capas (MVC)

---

## 🎯 Objetivo del Plan

Guiar el desarrollo del Sistema de Notas ESFE desde la configuración inicial hasta la entrega final, asegurando que cada fase sea funcional y probada antes de continuar.

---

## 📅 Fases de Implementación

### Fase 0: Configuración del Entorno (Día 1 - 30 min)

| Actividad | Responsable | Tiempo |
|-----------|-------------|--------|
| Crear proyecto en IntelliJ | Bayron | 5 min |
| Crear estructura de paquetes (model, view, controller) | Bayron | 5 min |
| Configurar GitHub (main, develop, feature/bayron) | Bayron | 10 min |
| Verificar JDK 17+ instalado | Bayron | 5 min |
| Probar que el proyecto compila | Bayron | 5 min |

**✅ Criterio de éxito:** El proyecto se abre en IntelliJ, compila sin errores y está en GitHub.

---

### Fase 1: Modelo de Datos (Día 1 - 1 hora)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear clase User (con rol, active, password) | `model/User.java` | 15 min |
| Crear clase Estudiante (carnet, nombre, apellido, nivel, grupo, activo) | `model/Estudiante.java` | 15 min |
| Crear clase Nota (estudianteId, materiaId, valor, fecha, observación) | `model/Nota.java` | 15 min |
| Crear clase Materia (nombre, carrera, nivel, grupo) | `model/Materia.java` | 10 min |
| Probar constructores y getters/setters | - | 5 min |

**✅ Criterio de éxito:** Las clases se crean sin errores de compilación.

---

### Fase 2: Controladores y Persistencia (Día 2 - 2 horas)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear UserController (CRUD + autenticación + archivo .dat) | `controller/UserController.java` | 30 min |
| Crear EstudianteController (CRUD + búsqueda + archivo .dat) | `controller/EstudianteController.java` | 30 min |
| Crear NotaController (CRUD + promedios + archivo .dat) | `controller/NotaController.java` | 30 min |
| Probar cada controlador con métodos main temporales | - | 30 min |

**✅ Criterio de éxito:** Los controladores guardan/cargan datos desde archivos .dat.

---

### Fase 3: Vistas - Login y Pantalla Principal (Día 3 - 2 horas)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear LoginForm (email, password, checkbox mostrar password) | `view/LoginForm.java` | 30 min |
| Conectar LoginForm con UserController.authenticate() | - | 15 min |
| Crear MainForm (JFrame con JMenuBar y JTabbedPane) | `view/MainForm.java` | 30 min |
| Implementar cierre de sesión en el menú | - | 15 min |
| Probar flujo login → main → logout | - | 30 min |

**✅ Criterio de éxito:** Se puede iniciar sesión con admin@demo.com / admin123 y ver la ventana principal.

---

### Fase 4: Vistas - Gestión de Estudiantes (Día 4 - 3 horas)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear tabla de estudiantes con JTable | `MainForm.java` (pestaña estudiantes) | 30 min |
| Crear EstudianteDialog (formulario para crear/editar) | `view/EstudianteDialog.java` | 45 min |
| Implementar botones Nuevo, Modificar, Eliminar, Actualizar | `MainForm.java` | 30 min |
| Implementar barra de búsqueda en tiempo real | `MainForm.java` | 30 min |
| Implementar estadísticas (total, activos, por nivel) | `MainForm.java` (updateStats) | 30 min |
| Probar CRUD completo y persistencia | - | 15 min |

**✅ Criterio de éxito:** Se puede agregar, modificar, eliminar y buscar estudiantes. La tabla muestra el curso formateado como "Software - 1° Año Gpo 2".

---

### Fase 5: Vistas - Gestión de Notas (Día 5 - 2 horas)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear tabla de notas registradas | `MainForm.java` (pestaña notas) | 20 min |
| Crear NotaDialog (formulario para registrar nota) | `view/NotaDialog.java` | 40 min |
| Materias predefinidas para cada nivel | `NotaDialog.java` | 20 min |
| Implementar registro de notas (validación 0-10) | `NotaDialog.java` | 20 min |
| Probar registro de notas y visualización en tabla | - | 20 min |

**✅ Criterio de éxito:** Se puede registrar una nota para un estudiante (0-10) y aparece en la tabla.

---

### Fase 6: Reportes (Día 6 - 1.5 horas)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Implementar reporte de promedios por estudiante | `MainForm.java` (showPromediosReport) | 30 min |
| Implementar reporte de promedios por carrera | `MainForm.java` (showPromediosPorCarreraReport) | 30 min |
| Probar reportes con datos de ejemplo | - | 30 min |

**✅ Criterio de éxito:** Los reportes muestran promedios correctos y estado APROBADO/REPROBADO.

---

### Fase 7: Cambio de Contraseña y Perfil (Día 6 - 1 hora)

| Actividad | Clase | Tiempo |
|-----------|-------|--------|
| Crear ChangePasswordDialog | `view/ChangePasswordDialog.java` | 30 min |
| Implementar validación de contraseña actual | - | 15 min |
| Conectar con UserController.changePassword() | - | 15 min |

**✅ Criterio de éxito:** El usuario puede cambiar su contraseña y luego iniciar sesión con la nueva.

---

### Fase 8: Pruebas y Documentación (Día 7 - 2 horas)

| Actividad | Tiempo |
|-----------|--------|
| Probar todo el flujo completo (login → agregar estudiante → registrar nota → reportes → logout) | 30 min |
| Agregar comentarios JavaDoc a todas las clases | 30 min |
| Crear archivos de documentación (requerimientos, prompt, plan) | 30 min |
| Subir todo a GitHub con commits y pull requests | 30 min |

**✅ Criterio de éxito:** El programa funciona sin errores, el código está comentado y GitHub tiene el historial.

---

## 📊 Resumen de Tiempos

| Fase | Días | Horas estimadas |
|------|------|-----------------|
| Fase 0: Configuración | Día 1 | 0.5 h |
| Fase 1: Modelo de Datos | Día 1 | 1 h |
| Fase 2: Controladores | Día 2 | 2 h |
| Fase 3: Login y Main | Día 3 | 2 h |
| Fase 4: Gestión de Estudiantes | Día 4 | 3 h |
| Fase 5: Gestión de Notas | Día 5 | 2 h |
| Fase 6: Reportes | Día 6 | 1.5 h |
| Fase 7: Cambio de contraseña | Día 6 | 1 h |
| Fase 8: Pruebas y documentación | Día 7 | 2 h |
| **TOTAL** | **7 días** | **15 horas** |

---

## 🔀 Estrategia de Ramas en GitHub
