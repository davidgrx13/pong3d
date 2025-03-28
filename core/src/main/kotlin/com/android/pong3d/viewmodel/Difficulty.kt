package com.android.pong3d.viewmodel

// Enum que representa los niveles de dificultad del juego.
// Cada nivel define la velocidad inicial de la pelota y el factor de reacción de la ia.
enum class Difficulty(
    val ballSpeed: Float,         // Velocidad horizontal inicial de la pelota (eje X)
    val cpuSpeedMultiplier: Float // Multiplicador de velocidad para la pala del pc
) {
    EASY(ballSpeed = 8f, cpuSpeedMultiplier = 0.02f),     // Movimiento más lento
    NORMAL(ballSpeed = 10f, cpuSpeedMultiplier = 0.05f),  // Equilibrado
    HARD(ballSpeed = 13f, cpuSpeedMultiplier = 0.08f)     // Movimiento rápido
}
