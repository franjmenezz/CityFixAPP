# CityFixAPP

![CityFix Logo](app/src/main/ic_launcher-playstore.png)

CityFixAPP es una aplicación nativa Android desarrollada en Android Studio para **reportar** y gestionar incidencias urbanas en tu ciudad. El objetivo es que cualquier ciudadano pueda comunicar problemas del entorno (baches, basuras, farolas fundidas, etc.) y que el ayuntamiento pueda priorizarlos y resolverlos de forma más eficiente.

---

## Objetivo

- Facilitar que los ciudadanos reporten incidencias de forma rápida desde el móvil.
- Centralizar la información de incidencias en un único sistema.
- Mejorar la comunicación entre ciudadanía y administración mediante estados y notificaciones.

---

## Funcionalidades principales

- Creación de incidencias con:
  - Foto (cámara o galería).
  - Ubicación mediante GPS / mapa.
  - Título, descripción y categoría.
- Listado de “Mis incidencias” con estado (pendiente, en progreso, resuelta).
- Detalle de incidencia con toda la información y su historial.
- Pantalla de inicio con acceso rápido a crear nueva incidencia y ver las existentes.
- Diseño limpio, enfocado a usarse con una mano.

## Arquitectura y organización

El proyecto está organizado como una app Android estándar:

## 🧱 Arquitectura y organización

El proyecto está organizado como una app Android estándar:

```text
app/
├── src/
│   └── main/
│       ├── java/com/example/cityfixapp/
│       │   ├── ui/         // Activities, Fragments, Adapters
│       │   ├── data/       // Modelos y acceso a datos
│       │   └── utils/      // Clases auxiliares
│       └── res/
│           ├── layout/     // XML de interfaces
│           ├── drawable/   // Iconos, fondos, logo
│           └── values/     // strings, colors, styles
└── build.gradle
````


- Capa de **presentación**: Activities/Fragments que gestionan la interfaz, listas de incidencias, formularios, etc.
- Capa de **datos**: modelos de incidencia y clases que se encargan de guardar/recuperar la información (BD local o servicios remotos, según la evolución del proyecto).
- Recursos en `res/` para layouts, textos e imágenes (incluyendo el icono `ic_launcher-playstore.png`).

---

## Posibles mejoras futuras

- Sistema de autenticación para que cada usuario vea solo sus propias incidencias.
- Panel administrativo (app o web) para el personal del ayuntamiento.
- Notificaciones push cuando cambie el estado de una incidencia.
- Modo offline: permitir crear incidencias sin conexión y sincronizarlas después.

---

## Autor

Proyecto desarrollado como trabajo final de **DAM (Desarrollo de Aplicaciones Multiplataforma)** en Android Studio.  
Autor: *Francisco Jiménez* (franjmenezz)

