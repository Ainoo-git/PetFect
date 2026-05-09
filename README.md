# 🐾 PetFect – “Ayuda y Encuentra”

<div align="center">

Aplicación Android para la localización de mascotas perdidas y la promoción de la adopción responsable.

[![Android](https://img.shields.io/badge/Platform-Android-0F5052?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)

[![Java](https://img.shields.io/badge/Language-Java-3ACAAE?style=for-the-badge&logo=java&logoColor=white)](#)

[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFC3C3?style=for-the-badge&logo=firebase&logoColor=black)](#)

[![Supabase](https://img.shields.io/badge/Database-Supabase-C3FFFC?style=for-the-badge&logo=supabase&logoColor=black)](https://supabase.com/)

[![Material Design](https://img.shields.io/badge/UI-Material%203-FF8C8C?style=for-the-badge&logo=material-design&logoColor=white)](#)

[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-3ACAAE?style=for-the-badge&logoColor=white)](#)

</div>

---

## Índice

- [Sobre el Proyecto](#-sobre-el-proyecto)
- [Vídeo Promocional](#-vídeo-promocional)
- [Características Principales](#-características-principales)
- [Arquitectura Tecnológica](#-arquitectura-tecnológica)
- [Vista Previa](#-vista-previa)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Roadmap](#-roadmap)
- [Objetivo Académico](#-objetivo-académico)
- [Licencia](#-licencia)

---

## 🐾 Sobre el Proyecto

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

## Vídeo Promocional

<div align="center">

[▶ Ver vídeo promocional de PetFect](https://youtu.be/lzR7Zqn5gJ4?si=AUesscdH2lUIxQCj)

</div>

---

## Características Principales

### Autenticación
- Registro mediante email y contraseña
- Inicio de sesión con Google
- Persistencia automática de sesión
- Aceptación obligatoria de términos y privacidad

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

| Modo Oscuro |
|-------------|
| <img src="https://github.com/user-attachments/assets/192ca139-fbd8-4d4e-a90c-6743a2956c73" width="250"> |


---

## Estructura del Proyecto

```bash
PetFect/
├── app/
│   ├── src/main/
│   │   ├── java/com/aipasa/
│   │   │   ├── auth/
│   │   │   ├── fragment/
│   │   │   ├── firebase/
│   │   │   ├── main/
│   │   │   └── model/
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── drawable/
│   │   │   ├── values/
│   │   │   ├── menu/
│   │   │   └── anim/
│   │   └── AndroidManifest.xml
```

---

## Instalación

### 1. Clonar repositorio

```bash
git clone https://github.com/Ainoo-git/PetFect.git
```

### 2. Abrir en Android Studio

### 3. Sincronizar Gradle

### 4. Configurar claves necesarias

Añadir credenciales en:

```properties
local.properties
```

### 5. Ejecutar aplicación

- Emulador Android
- Dispositivo físico

---

## Roadmap

- [ ] Geolocalización avanzada
- [ ] Sistema de favoritos
- [ ] Notificaciones push
- [ ] Filtros inteligentes
- [ ] Panel para refugios
- [ ] Historial de publicaciones
- [ ] Chat entre usuarios

---

## Objetivo Académico

Proyecto desarrollado como solución tecnológica enfocada al bienestar animal, aplicando conocimientos de:

- Desarrollo Android nativo
- Integración de servicios cloud
- Diseño UI/UX
- Gestión de datos en tiempo real
- Arquitectura modular

---

## Licencia

Distribuido bajo licencia **MIT**.

Consulta el archivo:

`LICENSE`

---

## PetFect

*"Conectando personas para ayudar a quienes más lo necesitan."*
