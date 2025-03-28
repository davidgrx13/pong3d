package com.android.pong3d

import com.android.pong3d.view.GameScreen
import com.badlogic.gdx.Game

class MainGame : Game() {
    override fun create() {
        setScreen(GameScreen())
    }
}
