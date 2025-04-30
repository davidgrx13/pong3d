# 🎮 Pong3D

## 🧠 Objetivo del Juego

**Pong3D** es un videojuego en tres dimensiones del clásico Pong. El jugador controla una pala que debe interceptar una pelota en movimiento para evitar que atraviese su lado del campo. El objetivo es anotar más puntos que la CPU, eligiendo entre tres niveles de dificultad: **Fácil**, **Normal** y **Difícil**.

El juego incluye:

- Sombras dinámicas
- Efectos de sonido
- Un menú intuitivo
- Botones para pausar
- Reiniciar y controlar el sonido


---

## 🛠️ Tecnologías Utilizadas

- **Kotlin** : Lenguaje de programación principal
- **libGDX** : Framework de desarrollo de juegos multiplataforma
- **Android Studio** : IDE para desarrollo y ejecución
- **LWJGL3** : Plataforma para ejecutar en escritorio
- **OpenGL ES** : Motor de renderizado 3D
- **Scene2D (Stage, Table)** : Para UI responsiva con botones e imágenes
- **Arquitectura MVVM** : Separación clara entre lógica, interfaz y datos
- **Gradle** : Sistema de construcción y dependencias
- **Gestión de sonido** : A través de `SoundManager`

---

## 🚀 Instalación y Ejecución
✅ Requisitos

- Android Studio instalado (versión reciente)

- JDK 11 o superior

- SDK de Android configurado

- Dispositivo Android o emulador (AVD)

- Conexión a Internet (para sincronizar dependencias de Gradle)
---

## 🕹️ Mecánicas del Juego
- Control táctil o con ratón (según plataforma).

- IA adaptable según dificultad elegida.

- Física básica y colisiones con paletas y bordes.

- Sonido al golpear la pelota o anotar punto.

- Interfaz para pausar/reanudar el juego o salir al menú.

- Detección de ganador cuando se alcanza la puntuación objetivo.

---

## ⚙️ Arquitectura del Proyecto (MVVM)

El juego está organizado siguiendo el patrón **MVVM (Modelo - Vista - ViewModel)** para una estructura clara y mantenible:

### 📁 Modelo (`model/`)
Contiene las entidades centrales del juego:
- `Ball`: controla la posición, movimiento y colisiones de la pelota.
- `Paddle`: maneja el comportamiento de la pala (jugador y CPU), incluyendo IA.

### 📁 Vista (`view/`)
Contiene las pantallas del juego:
- `MainMenuScreen`: menú principal con botón de iniciar juego y botones de sonido.
- `GameScreen`: vista principal del juego con renderizado, puntuación, pausa, etc.
- `DifficultyScreen`: permite seleccionar la dificultad antes de iniciar la partida.

### 📁 ViewModel (`viewmodel/`)
Contiene y encapsula la lógica del juego:
- `GameViewModel`: gestiona actualizaciones del juego, colisiones, puntuaciones y renderizado de objetos.
- `Difficulty`: enum que define la dificultad (velocidad de pelota ).

Esta separación permite mantener el código modular, reutilizable y más fácil de testear o extender.

---

## 🚀 Instrucciones de Instalación y Ejecución

### ✅ Requisitos

- Android Studio (última versión)
- SDK de Android configurado
- JDK 11 o superior
- Emulador o dispositivo Android físico
- Conexión a Internet para sincronizar dependencias de Gradle

### 📱   Android

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/pong3d.git  ```

--- 
## ▶️ Funcionalidades

✅ Selección de dificultad (Fácil, Normal, Difícil)

✅ Sonido de fondo y efectos de colisión/punto

✅ Botones de sonido ON/OFF

✅ Pausa del juego semitransparente

✅ Colisiones entre pelota y paletas

✅ Sombra dinámica debajo de objetos 3D

✅ Demo automática en el menú principal

✅ Diseño responsive y controles táctiles

---

## 🧱 Estructura del Proyecto

📁 **Pong3D/**
├── 📂 **android/**                  → Plataforma Android (Launcher)
│   └── 📄 AndroidLauncher.kt
│
├── 📂 **core/**                     → Lógica principal del juego
│   ├── 📂 assets/               → Archivos multimedia
│   │   ├── 🎵 sounds/           → Efectos de sonido y música
│   │   └── 🖼️ *.png             → Texturas y botones
│   └── 📂 src/
│       └── 📂 com.android.pong3d/
│           ├── 📁 **model/**        → Lógica de objetos (Ball, Paddle, etc.)
│           ├── 📁 **view/**         → Pantallas del juego (UI)
│           └── 📁 **viewmodel/**    → Lógica del juego y reglas (GameViewModel)
│
├── 📂 **lwjgl3/**                   → Plataforma escritorio (PC)
│   └── 📄 Lwjgl3Launcher.kt
├── 📝 **README.md**                → Documento del proyecto
└── ⚙️ build.gradle             → Configuración de compilación


---

## 📸 Capturas de Pantalla

---

## 🎥 Video

---
