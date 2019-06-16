package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel

class MenuState(gsm: GameStateManager, val model: B2DModel) : State(gsm) {
    private val background: Texture


    init {
        background = Texture("images/start.png")
        model.winGame = false

    }

    public override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            gsm.set(PlayState(gsm, model))
            dispose()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            gsm.set(HowToState(gsm,model))
            dispose()
        }

    }

    override fun update(dt: Float) {
        handleInput()

    }

    override fun render(sb: SpriteBatch) {
        sb.begin()
        sb.draw(background, 0f, 0f, 720f, 480f)
        sb.end()
    }

    override fun dispose() {
        background.dispose()
    }
}
