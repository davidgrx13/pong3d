package com.android.pong3d.view

import com.android.pong3d.viewmodel.GameViewModel
import com.android.pong3d.viewmodel.Difficulty
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameScreen(private val difficulty: Difficulty) : ScreenAdapter() {
    private val viewModel = GameViewModel(difficulty)
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val modelBatch = ModelBatch()
    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()
    private val projectedBallPos = Vector3()
    private var isPaused = false

    private val environment = Environment().apply {
        set(ColorAttribute.createAmbientLight(1f, 1f, 1f, 1f))
        add(DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f))
    }

    private val stage = Stage(ScreenViewport())
    private val pauseTexture = Texture(Gdx.files.internal("pause.png"))
    private val pauseIconTexture = Texture(Gdx.files.internal("pauseC.png"))
    private val playTexture = Texture(Gdx.files.internal("play.png"))
    private val exitTexture = Texture(Gdx.files.internal("exit.png"))

    private val pauseOverlay = Group()

    init {
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

        camera.position.set(0f, 25f, 25f)
        camera.lookAt(0f, 0f, 0f)
        camera.near = 1f
        camera.far = 100f
        camera.update()

        Gdx.input.inputProcessor = stage

        val pauseButton = ImageButton(TextureRegionDrawable(TextureRegion(pauseIconTexture)))
        pauseButton.setSize(50f, 50f)
        pauseButton.setPosition(20f, Gdx.graphics.height - 100f)
        pauseButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                showPauseOverlay()
            }
        })

        stage.addActor(pauseButton)
        setupPauseOverlay()
    }

    private fun setupPauseOverlay() {
        val darkOverlay = Image(TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("black.png")))))
        darkOverlay.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        darkOverlay.color.a = 0.6f

        val pauseImage = Image(TextureRegionDrawable(TextureRegion(pauseTexture)))
        pauseImage.setSize(250f, 60f)
        pauseImage.setPosition(
            (Gdx.graphics.width - pauseImage.width) / 2,
            (Gdx.graphics.height + 100f) / 2
        )

        val playButton = ImageButton(TextureRegionDrawable(TextureRegion(playTexture)))
        playButton.setSize(160f, 160f)
        playButton.setPosition(
            (Gdx.graphics.width / 2f) - 180f,
            (Gdx.graphics.height - 100f) / 2f - 80f
        )
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hidePauseOverlay()
            }
        })

        val exitButton = ImageButton(TextureRegionDrawable(TextureRegion(exitTexture)))
        exitButton.setSize(160f, 160f)
        exitButton.setPosition(
            (Gdx.graphics.width / 2f) + 20f,
            (Gdx.graphics.height - 100f) / 2f - 80f
        )
        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                (Gdx.app.applicationListener as Game).screen = MainMenuScreen(Gdx.app.applicationListener as Game)
            }
        })

        pauseOverlay.addActor(darkOverlay)
        pauseOverlay.addActor(pauseImage)
        pauseOverlay.addActor(playButton)
        pauseOverlay.addActor(exitButton)
        pauseOverlay.isVisible = false

        stage.addActor(pauseOverlay)
    }

    private fun showPauseOverlay() {
        isPaused = true
        pauseOverlay.isVisible = true
    }

    private fun hidePauseOverlay() {
        isPaused = false
        pauseOverlay.isVisible = false
    }

    override fun render(delta: Float) {
        if (!isPaused) {
            viewModel.update(delta)
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(camera)
        viewModel.render(modelBatch, environment)
        modelBatch.end()

        projectedBallPos.set(viewModel.ball.position)
        camera.project(projectedBallPos)

        val scoreText = "${viewModel.scoreCpu} : ${viewModel.scorePlayer}"
        layout.setText(font, scoreText)
        val x = (Gdx.graphics.width - layout.width) / 2
        val y = Gdx.graphics.height * 0.75f

        batch.begin()
        font.draw(batch, layout, x, y)
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        modelBatch.dispose()
        font.dispose()
        batch.dispose()
        stage.dispose()
        pauseTexture.dispose()
        pauseIconTexture.dispose()
        playTexture.dispose()
        exitTexture.dispose()
    }
}
