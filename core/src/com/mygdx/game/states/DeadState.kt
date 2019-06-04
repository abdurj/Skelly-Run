package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel

class DeadState(gsm: GameStateManager, var model: B2DModel): State(gsm) {

    private val background: Texture = Texture("images/black.png")

    override fun handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            gsm.set(MenuState(gsm,model))
            dispose()
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        sb.begin()

        sb.draw(background,0f,0f,720f,480f)

        sb.end()
    }

    override fun dispose() {
        background.dispose()
    }
}