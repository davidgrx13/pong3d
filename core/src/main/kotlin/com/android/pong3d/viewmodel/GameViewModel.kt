package com.android.pong3d.viewmodel

import com.android.pong3d.model.Ball
import com.android.pong3d.model.Paddle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.collision.BoundingBox

fun BoundingBox.overlaps(other: BoundingBox): Boolean {
    return !(this.max.x < other.min.x || this.min.x > other.max.x ||
        this.max.y < other.min.y || this.max.y > other.max.y ||
        this.max.z < other.min.z || this.min.z > other.max.z)
}

class GameViewModel {
    private val modelBuilder = ModelBuilder()
    private val whiteMaterial = Material(ColorAttribute.createDiffuse(Color.WHITE))
    private val redMaterial = Material(ColorAttribute.createDiffuse(Color.RED))
    private val greenMaterial = Material(ColorAttribute.createDiffuse(Color.SKY))

    // Fondo con textura
    private val boardTexture = Texture(Gdx.files.internal("board.png"))
    private val floorMaterial = Material(TextureAttribute.createDiffuse(boardTexture))
    private val floorModel: Model = modelBuilder.createBox(
        42f, 0.1f, 22f, // tamaño del campo
        floorMaterial,
        (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
    )
    private val floorInstance = ModelInstance(floorModel)

    private val ballModel = modelBuilder.createSphere(
        1f, 1f, 1f, 16, 16, whiteMaterial,
        (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    )
    private val paddleModel = modelBuilder.createBox(
        1f, 1f, 6f, redMaterial,
        (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    )
    private val playerPaddleModel = modelBuilder.createBox(
        1f, 1f, 6f, greenMaterial,
        (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
    )

    val ball = Ball(ballModel)
    private val player = Paddle("Jugador", x = 15f, model = ModelInstance(playerPaddleModel))
    private val cpu = Paddle("Pc", x = -15f, model = ModelInstance(paddleModel))

    var scorePlayer = 0
    var scoreCpu = 0
    private val bounds = 20f

    fun update(delta: Float) {
        ball.update(delta)
        player.update(delta)
        cpu.followBall(ball, delta * 0.05f)
        checkCollisions()
    }

    fun render(modelBatch: ModelBatch, environment: Environment) {
        floorInstance.transform.setToTranslation(0f, -0.55f, 0f)
        modelBatch.render(floorInstance, environment)

        ball.render(modelBatch, environment)
        player.render(modelBatch, environment)
        cpu.render(modelBatch, environment)
    }

    private fun checkCollisions() {
        if (ball.bounds.overlaps(player.bounds)) ball.bounceX()
        if (ball.bounds.overlaps(cpu.bounds)) ball.bounceX()

        if (ball.position.x > bounds) {
            scoreCpu++
            ball.reset()
        } else if (ball.position.x < -bounds) {
            scorePlayer++
            ball.reset()
        }
    }
}
