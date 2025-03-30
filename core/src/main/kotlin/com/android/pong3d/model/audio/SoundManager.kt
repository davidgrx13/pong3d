package com.android.pong3d.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object SoundManager {
    private var isMusicMuted = false
    private var isSoundMuted = false

    private val clickSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"))
    private val hitSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/golpe_pelota.ogg"))

    private val menuMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music_menu_opc1.ogg"))
    private val gameMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music_in_game.wav"))


    init {
        menuMusic.isLooping = true
        menuMusic.volume = 0.5f
        gameMusic.isLooping = true
        gameMusic.volume = 0.5f
    }

    fun playClick() {
        if (!isSoundMuted) clickSound.play()
    }

    fun playHit() {
        if (!isSoundMuted) hitSound.play()
    }

    fun playMenuMusic() {
        if (!isMusicMuted && !menuMusic.isPlaying) {
            stopGameMusic()
            menuMusic.play()
        }
    }

    fun stopMenuMusic() {
        menuMusic.stop()
    }

    fun playGameMusic() {
        if (!isMusicMuted && !gameMusic.isPlaying) {
            stopMenuMusic()
            gameMusic.play()
        }
    }

    fun stopGameMusic() {
        gameMusic.stop()
    }

    fun toggleMusicMute() {
        isMusicMuted = !isMusicMuted
        if (isMusicMuted) {
            stopMenuMusic()
            stopGameMusic()
        } else {
            // Esto se maneja desde la pantalla que llama a la música
        }
    }

    fun toggleSoundMute() {
        isSoundMuted = !isSoundMuted
    }
    fun toggleMute() {
        toggleMusicMute()
        toggleSoundMute()
    }

    fun isMusicEnabled(): Boolean = !isMusicMuted
    fun isSoundEnabled(): Boolean = !isSoundMuted
}
