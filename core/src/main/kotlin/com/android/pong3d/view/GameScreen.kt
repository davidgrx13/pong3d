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
import com.android.pong3d.audio.SoundManager
import com.android.pong3d.model.audio.BotonDeSonido
import com.badlogic.gdx.utils.viewport.FitViewport
class GameScreen(private val difficulty: Difficulty) : ScreenAdapter() {
    private val viewModel = GameViewModel(difficulty)
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val modelBatch = ModelBatch()
    private val font = BitmapFont().apply {
        data.setScale(3f)
    }
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()
    private val projectedBallPos = Vector3()
    private var isPaused = false

    private val environment = Environment().apply {
        set(ColorAttribute.createAmbientLight(1f, 1f, 1f, 1f))
        add(DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f))
    }

    private val stage = Stage(FitViewport(800f, 480f))
    private val pauseTexture = Texture(Gdx.files.internal("pause.png"))
    private val pauseIconTexture = Texture(Gdx.files.internal("pauseC.png"))
    private val playTexture = Texture(Gdx.files.internal("play.png"))
    private val exitTexture = Texture(Gdx.files.internal("exit.png"))
    private val soundOnTexture = Texture(Gdx.files.internal("sound_on.png"))
    private val soundOffTexture = Texture(Gdx.files.internal("sound_off.png"))

    private val pauseOverlay = Group()

    init {
        Gdx.input.inputProcessor = stage
        SoundManager.playGameMusic()

        val aspectRatio = stage.viewport.worldWidth / stage.viewport.worldHeight
        val boardWidth = 42f
        val boardHeight = 22f
        font.data.setScale(3f)
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

        val pauseButton = ImageButton(TextureRegionDrawable(TextureRegion(pauseIconTexture)))
        pauseButton.setSize(50f, 50f)
        pauseButton.setPosition(20f, stage.viewport.worldHeight - 70f)
        pauseButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                showPauseOverlay()
            }
        })
        stage.addActor(pauseButton)

        BotonDeSonido.agregarBotones(stage, soundOnTexture, soundOffTexture, isMenu = false)

        setupPauseOverlay()
    }

    private fun setupPauseOverlay() {
        val overlayWidth = stage.viewport.worldWidth
        val overlayHeight = stage.viewport.worldHeight

        val darkOverlay = Image(TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal("black.png")))))
        darkOverlay.setSize(overlayWidth, overlayHeight)
        darkOverlay.color.a = 0.6f

        val pauseImage = Image(TextureRegionDrawable(TextureRegion(pauseTexture)))
        pauseImage.setSize(overlayWidth * 0.5f, overlayHeight * 0.1f)
        pauseImage.setPosition((overlayWidth - pauseImage.width) / 2, (overlayHeight + 100f) / 2)

        val playButton = ImageButton(TextureRegionDrawable(TextureRegion(playTexture)))
        playButton.setSize(120f, 120f)
        playButton.setPosition((overlayWidth / 2f) - 140f, (overlayHeight - 100f) / 2f - 80f)
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hidePauseOverlay()
            }
        })

        val exitButton = ImageButton(TextureRegionDrawable(TextureRegion(exitTexture)))
        exitButton.setSize(120f, 120f)
        exitButton.setPosition((overlayWidth / 2f) + 20f, (overlayHeight - 100f) / 2f - 80f)
        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                SoundManager.stopGameMusic()
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

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        if (!isPaused) viewModel.update(delta)

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
        soundOnTexture.dispose()
        soundOffTexture.dispose()
        SoundManager.stopGameMusic()
    }
}
