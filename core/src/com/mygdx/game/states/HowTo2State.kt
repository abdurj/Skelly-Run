package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel

class HowTo2State(gsm: GameStateManager, private val model: B2DModel) : State(gsm) {

    private val background: Texture = Texture("images/howToPlay2.png")

    override fun handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
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