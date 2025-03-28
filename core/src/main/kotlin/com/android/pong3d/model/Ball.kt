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
    val position = Vector3(0f, 0f, 0f)
    private val velocity = Vector3(10f, 0f, 5f)
    val bounds = BoundingBox(Vector3(), Vector3())

    private val modelInstance = ModelInstance(model)

    private val shadowMaterial = Material(ColorAttribute.createDiffuse(Color.BLACK))
    private val shadowModel = ModelBuilder().createSphere(
        1f, 1f, 1f, 16, 16, shadowMaterial,
        VertexAttributes.Usage.Position.toLong() // sin normales, sin iluminación
    )
    private val shadowInstance = ModelInstance(shadowModel)

    fun update(delta: Float) {
        position.add(velocity.x * delta, 0f, velocity.z * delta)
        if (position.z > 10f || position.z < -10f) velocity.z *= -1
        bounds.set(position.cpy().add(-0.5f, -0.5f, -0.5f), position.cpy().add(0.5f, 0.5f, 0.5f))
    }

    fun render(modelBatch: ModelBatch, environment: Environment) {
        modelInstance.transform.setToTranslation(position)
        modelBatch.render(modelInstance, environment)

        val shadowTransform = Matrix4()
        shadowTransform.setToTranslation(position.x, position.y - 0.49f, position.z)
        shadowTransform.scale(1f, 0.01f, 1f)
        shadowInstance.transform = shadowTransform

        modelBatch.render(shadowInstance) // sin environment para que no brille
    }

    fun bounceX() { velocity.x *= -1 }

    fun reset() {
        position.set(0f, 0f, 0f)
        velocity.set(10f * if (Math.random() > 0.5) 1 else -1, 0f, 5f)
    }
}
