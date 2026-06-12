# 🤖 Prompt Optimizado para IA de Desarrollo

**Autor:** Bayron  
**Fecha:** 11/06/2025  
**IA objetivo:** Antigravity / GitHub Copilot / Claude Code / Cursor

---

## 📌 Contexto

> Necesito una aplicación de escritorio en Java Swing para gestionar notas de estudiantes de la carrera de **Software (Articulado)** en ESFE.

---

## 🎯 Prompt

```text
Eres un experto desarrollador Java Swing. Genera el código completo de un sistema de notas para ESFE con los siguientes requisitos:

## Requisitos funcionales

### Login
- Pantalla con campos: Email, Contraseña
- Checkbox "Mostrar contraseña" (alterna entre texto plano y puntos)
- Usuario por defecto: admin@demo.com / admin123 (rol ADMIN)

### Gestión de Estudiantes
- Carrera: Software
- Niveles: "1°", "2°", "4° Articulado"
- Grupos: 1 al 5
- Campos: Carnet (único), Nombre, Apellido, Nivel, Grupo, Estado (Activo/Inactivo)
- CRUD completo (Crear, Leer, Actualizar, Eliminar)
- Tabla con columnas: ID, Carnet, Nombre, Apellido, Curso (ej: "Software - 1° Año Gpo 2"), Estado
- Barra de búsqueda en tiempo real

### Gestión de Notas
- Materias predefinidas por nivel:
  - 1° Año: Módulo de Software, Inglés
  - 2° Año: Módulo de Software, Inglés
  - 4° Articulado: Módulo de Software, Inglés
- Registrar nota (0-10) + observación opcional
- Tabla de notas registradas

### Reportes
- Promedio por estudiante (aprobado >= 6.0)
- Promedio por carrera

### Persistencia
- estudiantes.dat, notas.dat, usuarios.dat

### Requisitos técnicos
- Java 17+, Swing, GridBagLayout
- Paquetes: model, view, controller
- Código comentado en español
- SwingUtilities.invokeLater

Genera las clases:
Main.java, LoginForm.java, MainForm.java, Estudiante.java, 
EstudianteController.java, EstudianteDialog.java, Nota.java, 
NotaController.java, NotaDialog.java, User.java, UserController.java, 
ChangePasswordDialog.java