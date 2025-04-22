package com.android.pong3d.model.audio
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.android.pong3d.audio.SoundManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin


object BotonDeSonido {


    fun agregarBotones(stage: Stage, soundOn: Texture, soundOff: Texture, isMenu: Boolean) {
        val font = BitmapFont() // fuente por defecto
        val labelStyle = Label.LabelStyle(font, Color.WHITE)

        if (isMenu) {
            val centerX = (Gdx.graphics.width - 50f) / 2f
            val labelMusica = Label("Music", labelStyle)
            labelMusica.setPosition(centerX, 70f)
            stage.addActor(labelMusica)
            crearBotonMusica(stage, soundOn, soundOff, centerX, 20f, isMenu)
        } else {
            val labelMusica = Label("Music", labelStyle)
            labelMusica.setPosition(Gdx.graphics.width - 140f, Gdx.graphics.height - 40f)
            stage.addActor(labelMusica)

            val labelSonido = Label("Effects", labelStyle)
            labelSonido.setPosition(Gdx.graphics.width - 70f, Gdx.graphics.height - 40f)
            stage.addActor(labelSonido)

            crearBotonMusica(stage, soundOn, soundOff, Gdx.graphics.width - 140f, Gdx.graphics.height - 100f, isMenu)
            crearBotonSonido(stage, soundOn, soundOff, Gdx.graphics.width - 70f, Gdx.graphics.height - 100f)
        }
    }

}

    fun crearBotonMusica(stage: Stage, soundOn: Texture, soundOff: Texture, x: Float, y: Float, isMenu: Boolean) {
        val button = ImageButton(TextureRegionDrawable(TextureRegion(
            if (SoundManager.isMusicEnabled()) soundOn else soundOff
        )))
        button.setSize(50f, 50f)
        button.setPosition(x, y)
        button.addListener(object : ClickListener() {
            override fun clicked(event: com.badlogic.gdx.scenes.scene2d.InputEvent?, x: Float, y: Float) {
                SoundManager.toggleMusicMute()
                val icon = if (SoundManager.isMusicEnabled()) soundOn else soundOff
                button.style.imageUp = TextureRegionDrawable(TextureRegion(icon))
                if (SoundManager.isMusicEnabled()) {
                    if (isMenu) SoundManager.playMenuMusic() else SoundManager.playGameMusic()
                } else {
                    if (isMenu) SoundManager.stopMenuMusic() else SoundManager.stopGameMusic()
                }
            }
        })
        stage.addActor(button)
    }

    fun crearBotonSonido(stage: Stage, soundOn: Texture, soundOff: Texture, x: Float, y: Float) {
        val button = ImageButton(TextureRegionDrawable(TextureRegion(
            if (SoundManager.isSoundEnabled()) soundOn else soundOff
        )))
        button.setSize(50f, 50f)
        button.setPosition(x, y)
        button.addListener(object : ClickListener() {
            override fun clicked(event: com.badlogic.gdx.scenes.scene2d.InputEvent?, x: Float, y: Float) {
                SoundManager.toggleSoundMute()
                val icon = if (SoundManager.isSoundEnabled()) soundOn else soundOff
                button.style.imageUp = TextureRegionDrawable(TextureRegion(icon))
            }
        })
        stage.addActor(button)
    }

