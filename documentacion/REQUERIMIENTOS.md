# 📋 Sistema de Notas ESFE - Requerimientos

---

## 🔐 RF-01: Login

| ID | Requerimiento |
|----|---------------|
| RF-01.1 | Pantalla de inicio de sesión con email y contraseña |
| RF-01.2 | Checkbox "Mostrar contraseña" para ver u ocultar el texto |
| RF-01.3 | Usuario por defecto: `admin@demo.com` / `admin123` |
| RF-01.4 | Mensaje de error si las credenciales son incorrectas |

---

## 👨‍🎓 RF-02: Gestión de Estudiantes

| Campo | Valores permitidos |
|-------|-------------------|
| Carrera | Software |
| Niveles | 1° Año, 2° Año, 4° Articulado |
| Grupos | 1, 2, 3, 4, 5 |
| Carnet | Único, obligatorio |
| Estado | Activo / Inactivo |

### Funcionalidades

- ✅ Agregar estudiante
- ✅ Modificar estudiante
- ✅ Eliminar estudiante (con confirmación)
- ✅ Ver tabla con: ID, Carnet, Nombre, Apellido, Curso, Estado
- ✅ Búsqueda en tiempo real (filtra mientras escribes)

**Ejemplo de curso en tabla:**  
`Software - 1° Año Gpo 2`  
`Software - 4° Articulado Gpo 3`

---

## 📝 RF-03: Gestión de Notas

### Materias predefinidas por nivel

| Nivel | Materia 1 | Materia 2 |
|-------|-----------|-----------|
| 1° Año | Módulo de Software | Inglés |
| 2° Año | Módulo de Software | Inglés |
| 4° Articulado | Módulo de Software | Inglés |

### Validaciones

- Nota válida: **0 a 10**
- Observación: opcional

---

## 📊 RF-04: Reportes

| Reporte | Descripción |
|---------|-------------|
| Promedio por estudiante | Muestra promedio y si está APROBADO (≥6.0) o REPROBADO |
| Promedio por carrera | Muestra promedio general de Software |

---

## 💾 RF-05: Persistencia

| Archivo | Contenido |
|---------|-----------|
| `estudiantes.dat` | Lista de estudiantes |
| `notas.dat` | Lista de notas registradas |
| `usuarios.dat` | Lista de usuarios del sistema |

---

## 🛠️ RF-06: Requisitos Técnicos

| Requisito | Especificación |
|-----------|----------------|
| Lenguaje | Java 17+ |
| GUI | Swing (GridBagLayout) |
| Arquitectura | MVC (model, view, controller) |
| Documentación | Código comentado en español |
| Punto de entrada | `SwingUtilities.invokeLater` |

---

## ✅ Criterios de Aceptación

- [x] Login funciona con admin@demo.com / admin123
- [x] Se puede ver/ocultar contraseña
- [x] CRUD completo de estudiantes
- [x] Tabla muestra curso correctamente (Software - 1° Año Gpo X)
- [x] Búsqueda en tiempo real funciona
- [x] Se pueden registrar notas (0-10)
- [x] Reporte de promedios funciona
- [x] Los datos persisten al cerrar el programa
- [x] El código compila sin errores