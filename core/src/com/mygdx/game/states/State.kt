package com.mygdx.game.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3

abstract class State protected constructor(protected var gsm: GameStateManager) {
    protected var camera: OrthographicCamera
    protected var mouse: Vector3

    init {
        camera = OrthographicCamera()
        mouse = Vector3()
    }

    protected abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
    abstract fun dispose()

}
