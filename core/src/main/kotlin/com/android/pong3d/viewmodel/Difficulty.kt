package com.android.pong3d.viewmodel

enum class Difficulty(
    val ballSpeed: Float,
    val cpuSpeedMultiplier: Float
) {
    EASY(ballSpeed = 8f, cpuSpeedMultiplier = 0.02f),
    NORMAL(ballSpeed = 10f, cpuSpeedMultiplier = 0.05f),
    HARD(ballSpeed = 13f, cpuSpeedMultiplier = 0.08f)
}
