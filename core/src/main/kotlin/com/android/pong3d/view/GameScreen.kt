package com.android.pong3d.view

import com.android.pong3d.viewmodel.GameViewModel
import com.android.pong3d.viewmodel.Difficulty
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3

class GameScreen(private val difficulty: Difficulty) : ScreenAdapter() {
    // ViewModel que gestiona la lógica del juego y recibe la dificultad
    private val viewModel = GameViewModel(difficulty)

    // Cámara 3D con perspectiva desde arriba
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    // Batches para renderizar modelos 3D y texto 2D
    private val modelBatch = ModelBatch()
    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    // Coordenadas proyectadas de la pelota (para efectos 2D sobre ella, como el glow)
    private val projectedBallPos = Vector3()

    // Entorno de iluminación con luz ambiental y una luz direccional (foco principal)
    private val environment = Environment().apply {
        set(ColorAttribute.createAmbientLight(1f, 1f, 1f, 1f))
        add(DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f))
    }

    init {
        // Configuración del viewport para que el tablero siempre se vea completo y centrado
        val boardWidth = 42f
        val boardHeight = 22f
        val aspectRatio = Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()

        if (aspectRatio >= boardWidth / boardHeight) {
            camera.viewportHeight = boardHeight
            camera.viewportWidth = boardHeight * aspectRatio
        } else {
            camera.viewportWidth = boardWidth
            camera.viewportHeight = boardWidth / aspectRatio
        }

        // Posicionamiento cenital de la cámara
        camera.position.set(0f, 25f, 15f) // un poco más atrás en Z
        camera.lookAt(0f, 0f, 0f)         // sigue mirando al centro del campo
        camera.near = 1f
        camera.far = 100f
        camera.update()
    }

    override fun render(delta: Float) {
        // Actualiza la lógica del juego
        viewModel.update(delta)

        // Limpia la pantalla para preparar el render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        // Renderiza los modelos 3D
        modelBatch.begin(camera)
        viewModel.render(modelBatch, environment)
        modelBatch.end()

        // Proyecta la posición 3D de la pelota a coordenadas de pantalla 2D
        projectedBallPos.set(viewModel.ball.position)
        camera.project(projectedBallPos)

        // Dibuja el marcador de puntuación centrado en la parte superior
        val scoreText = "${viewModel.scoreCpu} : ${viewModel.scorePlayer}"
        layout.setText(font, scoreText)
        val x = (Gdx.graphics.width - layout.width) / 2
        val y = Gdx.graphics.height * 0.75f

        batch.begin()
        font.draw(batch, layout, x, y)
        batch.end()
    }

    override fun dispose() {
        modelBatch.dispose()
        font.dispose()
        batch.dispose()
    }
}
