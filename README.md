# 🐾 PetFect – “Ayuda y Encuentra”

<div align="center">

Aplicación Android diseñada para facilitar la gestión de mascotas perdidas y promover la adopción responsable.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-blue?style=for-the-badge&logo=java&logoColor=white)]()
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)]()
[![Material Design](https://img.shields.io/badge/UI-Material%203-0081CB?style=for-the-badge&logo=material-design&logoColor=white)]()
[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-success?style=for-the-badge)]()

</div>

---

## Descripción

PetFect es una aplicación móvil desarrollada para ayudar a encontrar mascotas perdidas y facilitar procesos de adopción de forma organizada y accesible.

La aplicación conecta usuarios mediante publicaciones dinámicas, permitiendo registrar animales, añadir imágenes y contactar directamente con los responsables.

---

## Funcionalidades Actuales

### Autenticación
- Registro con email y contraseña
- Inicio de sesión con Google
- Persistencia de sesión (no se solicita login si el usuario ya ha iniciado sesión)

### Publicación de Mascotas
- Creación de publicaciones para mascotas perdidas o en adopción
- Subida de imagen desde:
  - Cámara
  - Galería
- Campos opcionales editables (chip, información adicional)
- Almacenamiento en Firebase Firestore
- Imágenes almacenadas en Firebase Storage

### Visualización de Mascotas
- Listado dinámico mediante RecyclerView
- Visualización detallada de cada publicación
- Contacto directo vía llamada telefónica

### Perfil de Usuario
- Visualización de datos del usuario
- Cambio de imagen de perfil
- Cierre de sesión seguro

### Interfaz y Experiencia
- Material Design 3
- BottomAppBar con FloatingActionButton
- Navegación clara e intuitiva
- Soporte para modo claro y oscuro

---

## Tecnologías Utilizadas

| Categoría | Tecnología |
|-----------|------------|
| Plataforma | Android |
| Lenguaje | Java |
| Backend | Firebase |
| Base de datos | Firestore |
| Almacenamiento | Firebase Storage |
| Autenticación | Firebase Auth |
| UI | Material Design 3 |
| Librerías | Glide |

---

## Capturas de Pantalla

| Pantalla | Vista |
|----------|-------|
| Splash | ![Splash](https://github.com/user-attachments/assets/fe8cc27f-e294-4c66-8a1d-c7fb9501b668) |
| Login | <img width="250" src="https://github.com/user-attachments/assets/b39e7266-8319-498e-bc84-1868e63a578d" /> |
| Registro | <img width="250" src="https://github.com/user-attachments/assets/d5cd5c2e-c4df-4d54-a6db-8d4d9e50967d" /> |

---

## Estructura del Proyecto
