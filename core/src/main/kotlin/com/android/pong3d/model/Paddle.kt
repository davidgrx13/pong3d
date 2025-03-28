package com.android.pong3d.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Paddle(val name: String, val x: Float, private val model: ModelInstance) {
    // Posición de la pala en el espacio 3D (se mantiene fija en eje X)
    val position = Vector3(x, 0f, 0f)

    // Bounding box para detección de colisiones
    val bounds = BoundingBox(Vector3(), Vector3())

    // Modelo 3D que representa la sombra negra debajo de la pala
    private val shadowMaterial = Material(ColorAttribute.createDiffuse(Color.BLACK))
    private val shadowModel = ModelBuilder().createBox(
        1f, 1f, 6f,
        shadowMaterial,
        VertexAttributes.Usage.Position.toLong() // sin normales ni iluminación
    )
    private val shadowInstance = ModelInstance(shadowModel)

    private var autoPlay = false

    fun enableAutoPlay() {
        autoPlay = true
    }

    // Actualiza la pala del jugador según la posición del ratón o en modo IA
    fun update(delta: Float) {
        if (name == "Jugador") {
            if (autoPlay) return // evita movimiento manual en autoPlay

            val mouseY = Gdx.input.y.toFloat()
            val screenHeight = Gdx.graphics.height.toFloat()

            // Normaliza la posición del ratón y la convierte a coordenadas del tablero (Z)
            val normalized = -mouseY / screenHeight
            val targetZ = -10f - normalized * 20f

            // Movimiento suave hacia la posición del cursor
            position.z += (targetZ - position.z) * 10f * delta
        }

        // Limita el movimiento vertical de la pala dentro del campo
        position.z = position.z.coerceIn(-8f, 8f)
        updateBounds()
    }

    // IA del PC (o del jugador si autoPlay = true): sigue la posición de la pelota en el eje Z
    fun followBall(ball: Ball, delta: Float) {
        val speed = 70f
        if (ball.position.z > position.z) position.z += speed * delta
        else if (ball.position.z < position.z) position.z -= speed * delta

        position.z = position.z.coerceIn(-8f, 8f)
        updateBounds()
    }

    // Actualiza el bounding box según la posición actual
    private fun updateBounds() {
        bounds.set(position.cpy().add(-0.5f, -0.5f, -3f), position.cpy().add(0.5f, 0.5f, 3f))
    }

    // Renderiza la sombra y luego la pala con iluminación
    fun render(modelBatch: ModelBatch, environment: Environment) {
        // Posiciona y aplana la sombra sobre el suelo
        val shadowTransform = Matrix4()
        shadowTransform.setToTranslation(position.x, -0.45f, position.z)
        shadowInstance.transform = shadowTransform
        modelBatch.render(shadowInstance)

        // Renderiza el modelo principal con iluminación
        model.transform.setToTranslation(position)
        modelBatch.render(model, environment)
    }
}
