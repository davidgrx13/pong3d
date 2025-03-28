package com.android.pong3d.model

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox

class Ball(private val model: Model) {
    val position = Vector3(0f, 0f, 0f)
    private val velocity = Vector3(10f, 0f, 5f)
    val bounds = BoundingBox(Vector3(), Vector3())
    private val modelInstance = ModelInstance(model)

    fun update(delta: Float) {
        position.add(velocity.x * delta, 0f, velocity.z * delta)
        if (position.z > 10f || position.z < -10f) velocity.z *= -1
        bounds.set(position.cpy().add(-0.5f, -0.5f, -0.5f), position.cpy().add(0.5f, 0.5f, 0.5f))
    }

    fun render(modelBatch: ModelBatch) {
        modelInstance.transform.setToTranslation(position)
        modelBatch.render(modelInstance)
    }

    fun bounceX() { velocity.x *= -1 }
    fun reset() {
        position.set(0f, 0f, 0f)
        velocity.set(10f * if (Math.random() > 0.5) 1 else -1, 0f, 5f)
    }
}
