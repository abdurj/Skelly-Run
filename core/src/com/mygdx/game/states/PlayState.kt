package com.mygdx.game.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel

class PlayState(gsm: GameStateManager, var model: B2DModel) : State(gsm) {

    private val player = model.player

    init{
        model.initLevel()
        model.initPlayer()
    }

    public override fun handleInput() {
    }
    override fun update(dt: Float) {
        handleInput()
        if(player.health <= 0){
            gsm.set(DeadState(gsm,model))
        }
    }

    override fun render(sb: SpriteBatch) {

    }

    override fun dispose() {

    }
}
