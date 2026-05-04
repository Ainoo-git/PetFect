# 🐾 PetFect – “Ayuda y Encuentra”

<div align="center">

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-blue?style=for-the-badge&logo=java&logoColor=white)]()
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)]()
[![Material Design](https://img.shields.io/badge/UI-Material%203-0081CB?style=for-the-badge&logo=material-design&logoColor=white)]()
[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-success?style=for-the-badge)]()

</div>

---

## Contenido

- [Sobre el Proyecto](#sobre-el-proyecto)
- [Demo](#-demo)
- [Características](#características-principales)
- [Arquitectura](#arquitectura-tecnológica)
- [Capturas](#vista-previa)
- [Instalación](#instalación)
- [Roadmap](#roadmap)
- [Licencia](#licencia)

---

## Sobre el Proyecto

**PetFect** es una aplicación Android orientada a la localización de mascotas perdidas y a la promoción de la adopción responsable mediante una plataforma centralizada de publicaciones.

La aplicación permite a los usuarios registrar mascotas perdidas o en adopción, adjuntar imágenes, consultar publicaciones actualizadas en tiempo real y contactar directamente con los responsables.

### Objetivos principales

- Facilitar la localización de mascotas desaparecidas
- Impulsar procesos de adopción responsable
- Centralizar publicaciones en una plataforma accesible
- Mejorar la comunicación entre usuarios
- Ofrecer una experiencia rápida, intuitiva y eficiente
  
El sistema permite crear publicaciones detalladas, adjuntar imágenes, consultar mascotas disponibles y contactar directamente con los responsables.

---

## 🎥 Demo

[Ver vídeo en YouTube](https://youtu.be/lzR7Zqn5gJ4)

---

## Características Principales

### Autenticación
- Registro mediante email y contraseña
- Inicio de sesión con Google
- Persistencia automática de sesión

### Gestión de Publicaciones
- Alta de mascotas perdidas o en adopción
- Subida de imágenes desde cámara o galería
- Información editable y ampliable
- Almacenamiento en Firestore

### Visualización Dinámica
- Feed actualizado en tiempo real
- RecyclerView optimizado
- Vista detallada por publicación
- Contacto telefónico directo

### Perfil de Usuario
- Gestión de datos personales
- Cambio de imagen de perfil
- Cierre de sesión seguro

### Experiencia de Usuario
- Material Design 3
- Navegación mediante Fragments
- BottomAppBar personalizada
- Soporte para modo claro y oscuro

---

## Arquitectura Tecnológica

| Componente | Tecnología |
|-----------|------------|
| Plataforma | Android Native |
| Lenguaje | Java |
| Arquitectura | Fragments + Adapter Pattern |
| Backend | Firebase |
| Base de datos | Cloud Firestore |
| Almacenamiento multimedia | Supabase Storage |
| Autenticación | Firebase Authentication |
| UI | Material Design 3 |
| Librerías | Glide · OkHttp |

---

## Vista Previa

### Pantallas Principales

| Splash | Login | Registro |
|--------|-------|----------|
| <img src="https://github.com/user-attachments/assets/a0b5ac40-3f4d-42e9-9817-664bba802920" width="250"> | <img src="https://github.com/user-attachments/assets/c7ffad0d-9086-4f3f-9aa1-6daf80e0468e" width="250"> | <img src="https://github.com/user-attachments/assets/6f379b6c-03ba-417b-8a5e-8ed9b4be4fb6" width="250"> |

---

###  Navegación Principal

| Home | Formulario |
|------|---------|
| <img src="https://github.com/user-attachments/assets/44698d40-babe-41ad-b78e-fae9bda97bca" width="250"> | <img src="https://github.com/user-attachments/assets/567757cf-5cad-49b1-9e82-543b55291a9e" width="250"> |

| Búsqueda | Mapa |
|----------|------|
| <img src="https://github.com/user-attachments/assets/22d42854-aea9-4495-977b-eee1c01fb3f7" width="250"> | <img src="https://github.com/user-attachments/assets/db71bf33-b332-49d8-9c59-5658a3bf1a65" width="250"> |

| Modo Oscuro | Perfil |
|-------------|--------|
| <img src="https://github.com/user-attachments/assets/192ca139-fbd8-4d4e-a90c-6743a2956c73" width="250"> | <img src="https://github.com/user-attachments/assets/db71bf33-b332-49d8-9c59-5658a3bf1a65" width="250"> |


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

## Roadmap

- [ ] Geolocalización avanzada
- [ ] Sistema de favoritos
- [ ] Notificaciones push
- [ ] Filtros inteligentes
- [ ] Panel para refugios
- [ ] Historial de publicaciones

---

## Objetivo Académico

Proyecto desarrollado como solución tecnológica enfocada al bienestar animal, aplicando conocimientos de:

- Desarrollo Android nativo
- Integración de servicios cloud
- Diseño UI/UX
- Gestión de datos en tiempo real

---

## Licencia

Distribuido bajo licencia **MIT**.
