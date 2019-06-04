package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel

class HowToState(gsm: GameStateManager,val model: B2DModel) : State(gsm) {

    private val background: Texture
    private val message: Texture
    private val back: Texture

    init {
        background = Texture("images/black.png")
        message = Texture("images/wasd.png")
        back = Texture("images/back.png")
    }

    public override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            gsm.set(MenuState(gsm, model))
            dispose()
        }
    }

    override fun update(dt: Float) {
        handleInput()

    }

    override fun render(sb: SpriteBatch) {
        sb.begin()
        sb.draw(background, 0f, 0f, 720f, 480f)
        sb.draw(message, (720 / 2 - 100).toFloat(), (480 / 2).toFloat(), 200f, 200f)
        sb.draw(back, 10f, 425f, 50f, 50f)
        sb.end()

    }

    override fun dispose() {
        background.dispose()
        message.dispose()
        back.dispose()
    }
}
