package com.android.pong3d.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Paddle(val name: String, val x: Float, private val model: ModelInstance) {
    val position = Vector3(x, 0f, 0f)
    val bounds = BoundingBox(Vector3(), Vector3())

    // Sombra
    private val shadowMaterial = Material(ColorAttribute.createDiffuse(Color.BLACK))
    private val shadowModel = ModelBuilder().createBox(
        1f, 1f, 6f,
        shadowMaterial,
        VertexAttributes.Usage.Position.toLong()
    )
    private val shadowInstance = ModelInstance(shadowModel)

    fun update(delta: Float) {
        if (name == "Jugador") {
            val mouseY = Gdx.input.y.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            val normalized = -mouseY / screenHeight
            val targetZ = -10f - normalized * 20f

            position.z += (targetZ - position.z) * 10f * delta
        }

        position.z = position.z.coerceIn(-8f, 8f)
        updateBounds()
    }

    fun followBall(ball: Ball, delta: Float) {
        val speed = 70f
        if (ball.position.z > position.z) position.z += speed * delta
        else if (ball.position.z < position.z) position.z -= speed * delta

        position.z = position.z.coerceIn(-8f, 8f)
        updateBounds()
    }

    private fun updateBounds() {
        bounds.set(position.cpy().add(-0.5f, -0.5f, -3f), position.cpy().add(0.5f, 0.5f, 3f))
    }

    fun render(modelBatch: ModelBatch, environment: Environment) {
        // Render sombra (aplastada y desplazada ligeramente hacia abajo)
        val shadowTransform = Matrix4()
        shadowTransform.setToTranslation(position.x, -0.45f, position.z)
        shadowInstance.transform = shadowTransform
        modelBatch.render(shadowInstance) // sin environment, para que no brille

        // Render pala normal con luz
        model.transform.setToTranslation(position)
        modelBatch.render(model, environment)
    }
}
