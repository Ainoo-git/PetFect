# 🐾 PetFect – “Ayuda y Encuentra”

<div align="center">

Aplicación Android para la localización de mascotas perdidas y la promoción de la adopción responsable.

[![Android](https://img.shields.io/badge/Platform-Android-0F5052?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-3ACAAE?style=for-the-badge&logo=java&logoColor=white)]()
[![Firebase](https://img.shields.io/badge/Backend-Firebase-C3FFFC?style=for-the-badge&logo=firebase&logoColor=black)]()
[![Supabase](https://img.shields.io/badge/Storage-Supabase-C3FFFC?style=for-the-badge&logo=supabase&logoColor=black)](https://supabase.com/)
[![Google Maps](https://img.shields.io/badge/Maps-Google%20Maps-0F5052?style=for-the-badge&logo=googlemaps&logoColor=white)]()
[![Material Design](https://img.shields.io/badge/UI-Material%203-FFC3C3?style=for-the-badge&logo=material-design&logoColor=white)]()
[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-FF8C8C?style=for-the-badge&logoColor=black)]()

</div>

---

## Índice

- [Sobre el Proyecto](#-sobre-el-proyecto)
- [Funcionamiento de la Aplicación](#-funcionamiento-de-la-aplicación)
- [Vídeo Promocional](#-vídeo-promocional)
- [Características Principales](#-características-principales)
- [Arquitectura Tecnológica](#-arquitectura-tecnológica)
- [Paleta Visual](#-paleta-visual)
- [Vista Previa](#-vista-previa)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Notificaciones Push](#-notificaciones-push)
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
- Permitir la consulta de mascotas mediante mapa
- Avisar a los usuarios mediante notificaciones cuando se publica una nueva mascota

El sistema permite crear publicaciones detalladas, adjuntar imágenes, consultar mascotas disponibles, guardar publicaciones favoritas y contactar directamente con los responsables.

---

## Funcionamiento de la Aplicación

Al abrir **PetFect**, aparece una pantalla inicial con el logotipo de la aplicación. Después, si el usuario no ha aceptado previamente los términos y condiciones, se muestra una ventana donde debe aceptar la política de privacidad y los términos de uso para poder continuar.

Una vez aceptados, la aplicación redirige al usuario a la pantalla de inicio de sesión. Desde ahí puede acceder mediante correo electrónico y contraseña o iniciar sesión con Google. Si el usuario ya tiene una sesión iniciada, no se le vuelve a pedir iniciar sesión y entra directamente a la aplicación.

Después del acceso, se muestra la pantalla principal, donde aparecen las mascotas publicadas separadas por secciones: mascotas perdidas y mascotas en adopción. Desde esta pantalla el usuario puede consultar publicaciones, filtrar el contenido, acceder al perfil, abrir el buscador, ver el mapa o crear una nueva publicación mediante el botón central.

Cuando el usuario publica una mascota, puede indicar si está perdida o en adopción, añadir su nombre, tipo, edad, teléfono, chip, información adicional, ubicación e imagen. La imagen puede seleccionarse desde galería o hacerse directamente con la cámara. Una vez publicada, la información se guarda en Firebase Firestore y la imagen se almacena en Supabase Storage.

También se ha añadido una zona de perfil donde el usuario puede ver sus datos, cambiar su nombre, modificar su foto de perfil, consultar sus propias publicaciones y cerrar sesión. La imagen de perfil queda guardada para que se mantenga aunque se cierre y se vuelva a abrir la aplicación.

Además, la aplicación incluye una pantalla de configuración desde la que se puede acceder a los términos y condiciones, política de privacidad, licencias, atribuciones, permisos del dispositivo, mascotas guardadas, notificaciones y modo oscuro.

---

## Vídeo Promocional

<div align="center">

[Ver vídeo promocional de PetFect](https://youtu.be/lzR7Zqn5gJ4?si=AUesscdH2lUIxQCj)

</div>

---

## Características Principales

### Autenticación
- Registro mediante email y contraseña
- Inicio de sesión con Google
- Persistencia automática de sesión
- Validación de campos obligatorios
- Aceptación obligatoria de términos y privacidad
- Redirección automática si el usuario ya ha iniciado sesión

### Gestión de Publicaciones
- Alta de mascotas perdidas o en adopción
- Subida de imágenes desde cámara o galería
- Información editable y ampliable
- Registro de nombre, tipo, estado, edad, teléfono, chip e información adicional
- Guardado de ubicación mediante latitud y longitud
- Almacenamiento de datos en Cloud Firestore
- Almacenamiento multimedia en Supabase Storage
- Edición de publicaciones existentes
- Visualización de publicaciones propias desde el perfil

### Visualización Dinámica
- Feed actualizado en tiempo real
- RecyclerView optimizado
- Separación entre mascotas perdidas y mascotas en adopción
- Vista detallada por publicación
- Contacto telefónico directo
- Carga de imágenes mediante Glide
- Ordenación de publicaciones por fecha

### Búsqueda
- Buscador de publicaciones
- Filtrado por nombre de mascota
- Filtrado por tipo de animal
- Separación entre resultados de mascotas perdidas y mascotas en adopción
- Actualización dinámica al escribir

### Mapa
- Integración con Google Maps
- Solicitud de permisos de ubicación
- Visualización de la ubicación actual del usuario
- Carga de mascotas publicadas con ubicación
- Marcadores en el mapa para mascotas registradas
- Acceso a la información de la mascota al pulsar sobre un marcador

### Sistema de Guardados
- Posibilidad de guardar mascotas como favoritas
- Pantalla de mascotas guardadas
- Eliminación de mascotas guardadas
- Guardado asociado al usuario actual
- Visualización mediante tarjetas personalizadas

### Perfil de Usuario
- Gestión de datos personales
- Cambio de nombre de usuario
- Cambio de imagen de perfil
- Subida de foto de perfil desde cámara o galería
- Almacenamiento de imagen de perfil en Supabase Storage
- Carga automática de la foto guardada
- Visualización de publicaciones creadas por el usuario
- Cierre de sesión seguro

### Configuración
- Acceso a términos y condiciones
- Acceso a política de privacidad
- Acceso a licencias
- Acceso a atribuciones
- Consulta de permisos del dispositivo
- Acceso a mascotas guardadas
- Acceso a notificaciones
- Activación de modo oscuro
- Contacto con soporte mediante correo electrónico
- Copia del correo de soporte mediante pulsación larga

### Notificaciones
- Sistema de notificaciones internas
- Pantalla con listado de notificaciones
- Notificaciones asociadas a nuevas publicaciones
- Modelo propio de notificación
- Adaptador personalizado para mostrar cada aviso
- Integración con Firebase Cloud Messaging
- Suscripción de usuarios al topic `allUsers`
- Cloud Function que detecta nuevas mascotas publicadas
- Envío de aviso según el estado de la mascota: perdida o en adopción

### Experiencia de Usuario
- Material Design 3
- Navegación mediante Fragments
- BottomNavigationView personalizada
- FloatingActionButton central para publicar
- Modo claro y modo oscuro
- Diseño visual coherente con la identidad de PetFect
- Interfaz sencilla, directa e intuitiva

---

## Arquitectura Tecnológica

| Componente | Tecnología |
|-----------|------------|
| Plataforma | Android Native |
| Lenguaje | Java |
| Arquitectura | Activities + Fragments + Adapter Pattern |
| Backend | Firebase |
| Base de datos | Cloud Firestore |
| Autenticación | Firebase Authentication |
| Notificaciones | Firebase Cloud Messaging |
| Funciones backend | Firebase Cloud Functions |
| Almacenamiento multimedia | Supabase Storage |
| Mapas | Google Maps API |
| Ubicación | Google Play Services Location |
| UI | Material Design 3 |
| Listados | RecyclerView |
| Imágenes | Glide |
| Peticiones HTTP | OkHttp |
| JSON | Gson |
| Animaciones | Lottie / Animaciones Android |
| Preferencias locales | SharedPreferences |
| Build | Gradle Kotlin DSL |

---

## Paleta Visual

La interfaz de PetFect mantiene una estética suave, cercana y relacionada con el bienestar animal. Se utilizan tonos cian para elementos principales, colores claros para fondos y tonos rosados/coral para avisos, tarjetas o elementos destacados.

| Uso | Color |
|-----|-------|
| Cian oscuro para iconos | `#0F5052` |
| Cian principal para botones | `#3ACAAE` |
| Cian claro para bordes | `#C3FFFC` |
| Rosa claro para fondos y tarjetas | `#FFC3C3` |
| Coral para alertas y errores | `#FF8C8C` |
| Blanco | `#FFFFFF` |
| Negro | `#000000` |
| Gris para iconos inactivos | `#9E9E9E` |

---

## Vista Previa

### Pantallas Principales

| Splash | Login | Registro |
|--------|-------|----------|
| <img src="https://github.com/user-attachments/assets/a0b5ac40-3f4d-42e9-9817-664bba802920" width="250"> | <img src="https://github.com/user-attachments/assets/c7ffad0d-9086-4f3f-9aa1-6daf80e0468e" width="250"> | <img src="https://github.com/user-attachments/assets/6f379b6c-03ba-417b-8a5e-8ed9b4be4fb6" width="250"> |

---

### Navegación Principal

| Home | Formulario |
|------|------------|
| <img src="https://github.com/user-attachments/assets/44698d40-babe-41ad-b78e-fae9bda97bca" width="250"> | <img src="https://github.com/user-attachments/assets/567757cf-5cad-49b1-9e82-543b55291a9e" width="250"> |

| Búsqueda | Mapa |
|----------|------|
| <img src="https://github.com/user-attachments/assets/22d42854-aea9-4495-977b-eee1c01fb3f7" width="250"> | <img src="https://github.com/user-attachments/assets/db71bf33-b332-49d8-9c59-5658a3bf1a65" width="250"> |

| Modo Oscuro | Modo Oscuro |
|-------------|-------------| 
| <img src="https://github.com/user-attachments/assets/192ca139-fbd8-4d4e-a90c-6743a2956c73" width="250"> |

---

### Nuevas Funcionalidades

| Perfil | Configuración | Guardados |
|--------|---------------|-----------|
| <!-- Añadir captura perfil --> | <!-- Añadir captura configuración --> | <!-- Añadir captura guardados --> |

| Notificaciones | Detalle Publicación | Editar Perfil |
|----------------|---------------------|---------------|
| <img width="1080" height="2400" alt="Screenshot_2026-06-01-10-52-51-781_com miui home" src="https://github.com/user-attachments/assets/f04e00be-d16c-4e92-b955-63dfd1cef1e9" /> | <!-- Añadir captura detalle --> | <!-- Añadir captura editar perfil --> |

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
