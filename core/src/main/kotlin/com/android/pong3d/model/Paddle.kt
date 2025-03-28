package com.android.pong3d.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Paddle(val name: String, val x: Float, private val model: ModelInstance) {
    val position = Vector3(x, 0f, 0f)
    val bounds = BoundingBox(Vector3(), Vector3())

    fun update(delta: Float) {
        if (name == "Jugador") {
            val mouseY = Gdx.input.y.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            // Convertimos la coordenada Y de pantalla a eje Z del juego (de -10 a +10)
            val normalized = -mouseY / screenHeight
            val targetZ = -10f - normalized * 20f

            // Movimiento suave hacia el mouse
            position.z += (targetZ - position.z) * 10f * delta
        }
        bounds.set(position.cpy().add(-0.5f, -0.5f, -3f), position.cpy().add(0.5f, 0.5f, 3f))
        updateBounds()
    }

    fun followBall(ball: Ball, delta: Float) {
        val speed = 70f
        if (ball.position.z > position.z) position.z += speed * delta
        else if (ball.position.z < position.z) position.z -= speed * delta
        updateBounds()
    }

    private fun updateBounds() {
        bounds.set(position.cpy().add(-0.5f, -0.5f, -3f), position.cpy().add(0.5f, 0.5f, 3f))
    }

    fun render(modelBatch: ModelBatch) {
        model.transform.setToTranslation(position)
        modelBatch.render(model)
    }
}
