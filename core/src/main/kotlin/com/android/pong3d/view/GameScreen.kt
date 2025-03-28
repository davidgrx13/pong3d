package com.android.pong3d.view

import com.android.pong3d.viewmodel.GameViewModel
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch

class GameScreen : ScreenAdapter() {
    private val viewModel = GameViewModel()
    private val camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val modelBatch = ModelBatch()

    private val font = BitmapFont()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    init {
        camera.position.set(0f, 30f, 0f)
        camera.lookAt(0f, 0f, 0f)
        camera.near = 1f
        camera.far = 100f
        camera.update()
    }

    override fun render(delta: Float) {
        viewModel.update(delta)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(camera)
        viewModel.render(modelBatch)
        modelBatch.end()

        val scoreText = "${viewModel.scoreCpu} : ${viewModel.scorePlayer}"
        layout.setText(font, scoreText)
        val x = (Gdx.graphics.width - layout.width) / 2
        val y = Gdx.graphics.height - 30f

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
