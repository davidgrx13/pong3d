package com.android.pong3d.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.android.pong3d.viewmodel.Difficulty
import com.badlogic.gdx.Game

class MainMenuScreen(private val game: Game) : ScreenAdapter() {
    private val stage = Stage(ScreenViewport())

    init {
        // Define el Stage como el inputProcessor para capturar eventos de interfaz
        Gdx.input.inputProcessor = stage
        val skin = Skin(Gdx.files.internal("uiskin.json")) // skin por defecto para los widgets

        val table = Table()
        table.setFillParent(true) // ocupa toda la pantalla
        stage.addActor(table)

        // Título del juego
        val titleLabel = Label("PONG", skin, "default").apply {
            setFontScale(3f)
        }

        // Botón para pasar a la selección de dificultad
        val playButton = TextButton("JUGAR", skin)
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = DifficultyScreen(game)
            }
        })

        // Distribución vertical: título + botón
        table.add(titleLabel).padBottom(40f).row()
        table.add(playButton).width(200f).height(60f).padTop(20f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}

class DifficultyScreen(private val game: Game) : ScreenAdapter() {
    private val stage = Stage(ScreenViewport())

    init {
        Gdx.input.inputProcessor = stage
        val skin = Skin(Gdx.files.internal("uiskin.json"))

        val table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        // Título reutilizado
        val titleLabel = Label("PONG", skin, "default").apply {
            setFontScale(3f)
        }

        // Botones para seleccionar dificultad
        val easy = TextButton("FÁCIL", skin)
        val normal = TextButton("NORMAL", skin)
        val hard = TextButton("DIFÍCIL", skin)

        easy.addListener(click { startGame(Difficulty.EASY) })
        normal.addListener(click { startGame(Difficulty.NORMAL) })
        hard.addListener(click { startGame(Difficulty.HARD) })

        // Distribución vertical de opciones
        table.add(titleLabel).padBottom(40f).row()
        table.add(easy).padBottom(20f).width(200f).row()
        table.add(normal).padBottom(20f).width(200f).row()
        table.add(hard).width(200f)
    }

    // Utilidad para crear listeners más limpios
    private fun click(action: () -> Unit) = object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) = action()
    }

    // Lanza el juego con la dificultad seleccionada
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
    }
}
