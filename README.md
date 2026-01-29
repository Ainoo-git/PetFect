
# 🐾 PetFect

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
Mapa interactivo: Localiza mascotas perdidas y veterinarias cercanas.
Registro y perfil de usuario: Permite guardar tus búsquedas y favoritos.
Notificaciones: Recibe alertas sobre animales encontrados cerca de ti.

--- 

##  3.Tecnologías utilizadas
Plataforma: Android
Lenguaje de programación: Java / Kotlin
IDE: Android Studio
Bases de datos: API de refugios y veterinarias
Otros: Google Maps API, notificaciones push

---

## Configuración adicional
Para el uso del mapa será necesaria una API Key de Google Maps, que deberá configurarse en el archivo `local.properties`.

---

##  4.Equipo de desarrollo

Ainoha Yubero
Patricia Paun
Sara Ulgazi

---

##5. Estructura del Proyecto

```
PetFect/
├── AndroidManifest.xml
├── java/com.petfect/
│   ├── MainActivity.java
│   ├── Login.java
│   ├── Signup.java
│   ├── Splash.java
│   ├── Profile.java
│   └── PreferenciasActivity.java
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_login.xml
│   │   ├── activity_signup.xml
│   │   ├── activity_profile.xml
│   │   ├── activity_preferencias.xml
│   │   └── splash.xml
│   ├── menu/
│   ├── drawable/
│   ├── anim/
│   ├── color/
│   └── values/
└── Gradle Scripts/

```

---
##6. Características Principales

### 6.1 Diseño UI/UX
- Material Design 3  
- Layouts responsivos con Edge-to-Edge  
- Animaciones con Lottie y Glide  
- Gradientes personalizados e iconos vectoriales  
- Soporte para tema claro/oscuro  

### 6.2 Autenticación y Navegación
- Login y Signup con navegación directa  
- Campos de usuario, contraseña y email  
- Botones con colores y estilos personalizados  
- Toolbar en perfil con flecha atrás  

### 6.3 Funcionalidades Especiales
- BottomAppBar con FloatingActionButton  
- Menú contextual con opciones de guardar animales, pantalla principal, cámara para registrar al animal y perfil del usuario
- WebView con SwipeRefreshLayout  
- Cambio de idioma y alternancia modo claro/oscuro

---
##  7. Enlace al prototipo en Figma:  
https://www.figma.com/design/06eBS84aSirWxt0tDWtGxG/PetFect?node-id=0-1&t=PAzMS1DZz4Tj30AR-1

<img width="1383" height="765" alt="image" src="https://github.com/user-attachments/assets/fc34b954-b5a2-43b1-8354-a52894b076ea" />

---

## 7. Clona el repositorio:
git clone https://github.com/Ainoo-git/PetFect.git

---

##  4. Licencia

Este proyecto está licenciado bajo **Creative Commons Atribución-CompartirIgual 4.0 Internacional**.  
[![CC BY-SA 4.0](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)](https://creativecommons.org/licenses/by-sa/4.0/)
