package com.android.pong3d.view

import com.android.pong3d.audio.SoundManager
import com.android.pong3d.model.audio.BotonDeSonido
import com.android.pong3d.viewmodel.Difficulty
import com.android.pong3d.viewmodel.GameViewModel
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport


class MainMenuScreen(private val game: Game) : ScreenAdapter() {
    private val stage = Stage(FitViewport(800f, 480f))

    private val previewViewModel = GameViewModel(Difficulty.NORMAL, autoPlay = true, enableHitSound = false)
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val modelBatch = ModelBatch()
    private val environment = Environment().apply {
        set(ColorAttribute.createAmbientLight(1f, 1f, 1f, 1f))
        add(DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f))
    }

    private val titleTexture = Texture(Gdx.files.internal("title.png"))
    private val playTexture = Texture(Gdx.files.internal("play.png"))
    private val blackOverlay = Texture(Gdx.files.internal("black.png"))
    private val soundOnTexture = Texture(Gdx.files.internal("sound_on.png"))
    private val soundOffTexture = Texture(Gdx.files.internal("sound_off.png"))


    init {
        Gdx.input.inputProcessor = stage
        SoundManager.playMenuMusic()

        camera.position.set(0f, 25f, 15f)
        camera.lookAt(0f, 0f, 0f)
        camera.near = 1f
        camera.far = 100f
        camera.update()

        val overlayImage = Image(TextureRegionDrawable(TextureRegion(blackOverlay))).apply {
            setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            color.a = 0.6f
        }
        stage.addActor(overlayImage)

        val table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        val titleImage = Image(TextureRegionDrawable(TextureRegion(titleTexture))).apply {
            setSize(250f, 100f)
        }

        val playButton = Image(TextureRegionDrawable(TextureRegion(playTexture))).apply {
            setSize(220f, 220f)
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    SoundManager.playClick()
                    SoundManager.stopMenuMusic()
                    game.screen = DifficultyScreen(game)
                }
            })
        }

        table.add(titleImage).size(250f, 70f).padBottom(40f).row()
        table.add(playButton).size(170f, 60f)

        BotonDeSonido.agregarBotones(stage, soundOnTexture, soundOffTexture, isMenu = true)


    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        previewViewModel.update(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        modelBatch.begin(camera)
        previewViewModel.render(modelBatch, environment)
        modelBatch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        modelBatch.dispose()
        titleTexture.dispose()
        playTexture.dispose()
        blackOverlay.dispose()
    }
}

class DifficultyScreen(private val game: Game) : ScreenAdapter() {
    private val stage = Stage(FitViewport(800f, 480f))

    private val titleTexture = Texture(Gdx.files.internal("title.png"))
    private val easyTexture = Texture(Gdx.files.internal("easy.png"))
    private val mediumTexture = Texture(Gdx.files.internal("medium.png"))
    private val hardTexture = Texture(Gdx.files.internal("hard.png"))
    private val soundOnTexture = Texture(Gdx.files.internal("sound_on.png"))
    private val soundOffTexture = Texture(Gdx.files.internal("sound_off.png"))

    init {
        Gdx.input.inputProcessor = stage

        val table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        val titleImage = Image(TextureRegionDrawable(TextureRegion(titleTexture))).apply {
            setSize(250f, 100f)
        }

        val easy = Image(TextureRegionDrawable(TextureRegion(easyTexture))).apply {
            setSize(200f, 70f)
            addListener(click { startGame(Difficulty.EASY) })
        }
        val normal = Image(TextureRegionDrawable(TextureRegion(mediumTexture))).apply {
            setSize(200f, 70f)
            addListener(click { startGame(Difficulty.NORMAL) })
        }
        val hard = Image(TextureRegionDrawable(TextureRegion(hardTexture))).apply {
            setSize(200f, 70f)
            addListener(click { startGame(Difficulty.HARD) })
        }

        table.add(titleImage).size(250f, 70f).padBottom(50f).row()
        table.add(easy).size(100f, 30f).padBottom(25f).row()
        table.add(normal).size(155f, 28f).padBottom(25f).row()
        table.add(hard).size(120f, 30f)


    }
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    private fun click(action: () -> Unit) = object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) = action()
    }

    private fun startGame(difficulty: Difficulty) {
        game.screen = GameScreen(difficulty)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        titleTexture.dispose()
        easyTexture.dispose()
        mediumTexture.dispose()
        hardTexture.dispose()
        soundOnTexture.dispose()
        soundOffTexture.dispose()
    }
}
