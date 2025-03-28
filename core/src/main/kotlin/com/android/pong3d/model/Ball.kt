package com.android.pong3d.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Ball(model: Model) {
    // Posición actual de la pelota en el espacio 3D
    val position = Vector3(0f, 0f, 0f)

    // Velocidad de la pelota en los ejes X y Z (horizontal y profundidad)
    val velocity = Vector3(10f, 0f, 5f)

    // Bounding box para detección de colisiones
    val bounds = BoundingBox(Vector3(), Vector3())

    // Instancia visual 3D de la pelota
    private val modelInstance = ModelInstance(model)

    // Instancia secundaria para simular una sombra proyectada debajo de la pelota
    private val shadowMaterial = Material(ColorAttribute.createDiffuse(Color.BLACK))
    private val shadowModel = ModelBuilder().createSphere(
        1f, 1f, 1f, 16, 16, shadowMaterial,
        VertexAttributes.Usage.Position.toLong() // solo posiciones, sin iluminación
    )
    private val shadowInstance = ModelInstance(shadowModel)

    // Actualiza la posición y el bounding box según la velocidad y colisiones con los bordes Z
    fun update(delta: Float) {
        position.add(velocity.x * delta, 0f, velocity.z * delta)

        // Rebote en los límites verticales del campo (eje Z)
        if (position.z > 10f || position.z < -10f) velocity.z *= -1

        // Actualización de la caja de colisión en base a la nueva posición
        bounds.set(position.cpy().add(-0.5f, -0.5f, -0.5f), position.cpy().add(0.5f, 0.5f, 0.5f))
    }

    // Renderiza la pelota y su sombra en la escena
    fun render(modelBatch: ModelBatch, environment: Environment) {
        modelInstance.transform.setToTranslation(position)
        modelBatch.render(modelInstance, environment)

        // Posiciona la sombra justo debajo de la pelota y la aplana
        val shadowTransform = Matrix4()
        shadowTransform.setToTranslation(position.x, position.y - 0.49f, position.z)
        shadowTransform.scale(1f, 0.01f, 1f)
        shadowInstance.transform = shadowTransform

        // Render sin luces para que se mantenga negra y plana
        modelBatch.render(shadowInstance)
    }

    // Invierte la dirección horizontal (eje X) de la pelota al colisionar con una pala
    fun bounceX() {
        velocity.x *= -1
    }

    // Reinicia la posición y dirección de la pelota tras anotar un punto
    fun reset() {
        position.set(0f, 0f, 0f)
        velocity.set(10f * if (Math.random() > 0.5) 1 else -1, 0f, 5f)
    }
}
