
# PetFect

<div align="center">

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)  
[![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)](https://developer.android.com/studio)  
[![Material Design](https://img.shields.io/badge/Material--Design-0081CB?style=for-the-badge&logo=material-design&logoColor=white)](https://material.io/)  
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)  
[![Licencia](https://img.shields.io/badge/Licencia-CC%20BY--SA%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by-sa/4.0/)  

</div>

---

##  1. Descripción
PetFect es una aplicación diseñada para facilitar la adopción responsable de mascotas y ayudar a encontrar animales perdidas. Su objetivo principal es conectar adoptantes, propietarios y refugios en una red solidaria y confiable, utilizando tecnologías modernas como geolocalización y alertas en tiempo real.

---

##  2.Funcionalidades
Consulta de animales perdidos: Busca mascotas perdidas en tu zona.
Adopciones: Visualiza animales disponibles para adopción en refugios cercanos.
Registro y perfil de usuario: Permite guardar tus búsquedas y favoritos.
Notificaciones: Recibe alertas sobre animales encontrados cerca de ti.

  ## 2.1. Funcionalidades Implementadas
    Autenticación
      Registro con email y contraseña
      Inicio de sesión con Google
      Persistencia de sesión (si el usuario ya ha iniciado sesión, no se vuelve a pedir login)
    Publicación de Mascotas
      Formulario para registrar mascota perdida o en adopción
      Subida de imagen mediante:
      Cámara 
      Galería
      Información opcional editable (chip, descripción adicional)
      Datos almacenados en Firebase Firestore
      Imagen almacenada en Firebase Storage
    Perfil de Usuario
      Visualización de datos del usuario
      Cambio de imagen de perfil
      Cierre de sesión
    UI/UX
      Material Design 3
      BottomAppBar con FloatingActionButton para crear publicaciones
      Navegación sencilla y clara
      Soporte modo claro / oscuro

--- 
##  3. Pantallas de la Aplicación

| Pantalla              | Imagen                                                                 | Descripción                                         |
|-----------------------|------------------------------------------------------------------------|---------------------------------------------------|
| Uso de la app         |                                                                        | Breve explicación de cómo se usa la app          |
| Splash / Principal    | ![AnimacionPetFect](https://github.com/user-attachments/assets/fe8cc27f-e294-4c66-8a1d-c7fb9501b668) | Animación inicial al abrir la app                |
| Icono app             | <img width="1259" height="1080" alt="petfectfin" src="https://github.com/user-attachments/assets/0add9382-9a19-4bf3-822d-22a89886957c" /> | Icono de la aplicación |
| Login                 | <img width="383" height="853" alt="image" src="https://github.com/user-attachments/assets/b39e7266-8319-498e-bc84-1868e63a578d" /> | Pantalla de login con campos y botones de navegación |
| Registro (Signup)     | <img width="386" height="853" alt="image" src="https://github.com/user-attachments/assets/d5cd5c2e-c4df-4d54-a6db-8d4d9e50967d" /> | Registro de usuario con Username, Password y Email |
| Perfil de Usuario     |                                                                        | Visualización de avatar, nombre y descripción    |
| MainActivity          |                                                                        | WebView con SwipeRefreshLayout y menú contextual |



##  4.Tecnologías utilizadas
Plataforma: Android
Lenguaje de programación: Java / Kotlin
IDE: Android Studio
Bases de datos: API de refugios y veterinarias
Otros: Google Maps API, notificaciones push

---

## Configuración adicional
Para el uso del mapa será necesaria una API Key de Google Maps, que deberá configurarse en el archivo `local.properties`.

---

##  5.Equipo de desarrollo

Ainoha Yubero
Patricia Paun
Sara Ulgazi

---

##6. Estructura del Proyecto

```
PetFect/
├── AndroidManifest.xml
├── java/com.petfect/
│   ├── MainActivity.java
│   ├── Login.java
│   ├── Signup.java
│   ├── Splash.java
│   ├── Profile.java
│   ├── PreferenciasActivity.java
│   │
│   ├── PerdidosActivity.java
│   ├── PublicacionActivity.java
│   ├── AdopcionesActivity.java
│   ├── ScrollingActivity.java
│   
│  
│
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_login.xml
│   │   ├── activity_signup.xml
│   │   ├── activity_profile.xml
│   │   ├── activity_preferencias.xml
│   │   ├── activity_perdidos.xml
│   │   ├── activity_publicacion.xml
│   │   ├── activity_adopciones.xml
│   │   ├── activity_scrolling.xml
│   │   └── splash.xml
│   │
│   ├── menu/
│   │   └── bottom_nav_menu.xml
│   │
│   ├── drawable/
│   ├── anim/
│   ├── color/
│   └── values/
│
└── Gradle Scripts/


```

---
##7. Características Principales

### 7.1 Diseño UI/UX
- Material Design 3  
- Layouts responsivos con Edge-to-Edge  
- Animaciones con Lottie y Glide  
- Gradientes personalizados e iconos vectoriales  
- Soporte para tema claro/oscuro  

### 7.2 Autenticación y Navegación
- Login y Signup con navegación directa  
- Campos de usuario, contraseña y email  
- Botones con colores y estilos personalizados  
- Toolbar en perfil con flecha atrás  

### 7.3 Funcionalidades Especiales
- BottomAppBar con FloatingActionButton  
- Menú contextual con opciones de guardar animales, pantalla principal, cámara para registrar al animal y perfil del usuario
- WebView con SwipeRefreshLayout  
- Cambio de idioma y alternancia modo claro/oscuro

---
##  8. Enlace al prototipo en Figma:  
https://www.figma.com/design/06eBS84aSirWxt0tDWtGxG/PetFect?node-id=0-1&t=PAzMS1DZz4Tj30AR-1

<img width="1383" height="765" alt="image" src="https://github.com/user-attachments/assets/fc34b954-b5a2-43b1-8354-a52894b076ea" />

---

## 9. Clona el repositorio:
git clone https://github.com/Ainoo-git/PetFect.git

---

##  10. Licencia

Este proyecto está licenciado bajo **Creative Commons Atribución-CompartirIgual 4.0 Internacional**.  
[![CC BY-SA 4.0](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)](https://creativecommons.org/licenses/by-sa/4.0/)
