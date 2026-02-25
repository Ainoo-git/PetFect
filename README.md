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
- Imágenes almacenadas en Supabase Storage

### Visualización de Mascotas
- Listado dinámico mediante RecyclerView
- Actualización en tiempo real con SnapshotListener
- Visualización detallada de cada publicación
- Contacto directo vía llamada telefónica

### Perfil de Usuario
- Visualización de datos del usuario
- Cambio de imagen de perfil
- Subida de imagen a Supabase
- Cierre de sesión seguro

### Interfaz y Experiencia
- Material Design 3
- BottomAppBar con FloatingActionButton central 
- Navegación mediante Fragments
- Soporte para modo claro y oscuro

---

## Tecnologías Utilizadas

| Categoría | Tecnología |
|-----------|------------|
| Plataforma | Android |
| Lenguaje | Java |
| Backend | Firebase Auth|
| Base de datos | Firestore |
| Almacenamiento | Firebase Storage |
| Autenticación | Firebase Auth |
| UI | Material Design 3 |
| Librerías | Glide, OkHttp |

---

## Capturas de Pantalla

## Pantallas Principales

| Splash | Login | Registro |
|--------|--------|----------|
| ![IMG-20260225-WA0037](https://github.com/user-attachments/assets/a0b5ac40-3f4d-42e9-9817-664bba802920)
 | ![IMG-20260225-WA0039](https://github.com/user-attachments/assets/c7ffad0d-9086-4f3f-9aa1-6daf80e0468e)
| ![IMG-20260225-WA0038](https://github.com/user-attachments/assets/6f379b6c-03ba-417b-8a5e-8ed9b4be4fb6)
 |

---

### Navegación Principal

| Home | Detalle | Publicar |
|------|---------|----------|
| <img width="250" src="AQUI_TU_IMAGEN_HOME" /> | <img width="250" src="AQUI_TU_IMAGEN_DETALLE" /> | <img width="250" src="AQUI_TU_IMAGEN_PUBLICACION" /> |

<br>

| Perfil | Mapa |
|--------|------|
| <img width="250" src="AQUI_TU_IMAGEN_PERFIL" /> | <img width="250" src="AQUI_TU_IMAGEN_MAPA" />

---

## Estructura del Proyecto
```
PetFect/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/aipasa/
│   │       │   ├── auth/
│   │       │   ├── fragment/
│   │       │   ├── main/
│   │       │   ├── firebase/
│   │       │   └── model/
│   │       │
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   ├── drawable/
│   │       │   ├── values/
│   │       │   ├── menu/
│   │       │   └── anim/
│   │       │
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

---

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Ainoo-git/PetFect.git
   ```

2. **Abrir el proyecto en Android Studio**

3. **Sincronizar Gradle**

4. **Configurar claves necesarias**
   - Añadir las API Keys correspondientes en el archivo `local.properties` si procede.

5. **Ejecutar la aplicación**
   - En emulador Android  
   - O en dispositivo físico  

---

## Próximas Mejoras

- Sistema completo de geolocalización
- Notificaciones avanzadas en tiempo real
- Filtros de búsqueda personalizados
- Panel de administración para refugios

---

## Licencia

Este proyecto está bajo la licencia  
**Creative Commons BY-SA 4.0**
