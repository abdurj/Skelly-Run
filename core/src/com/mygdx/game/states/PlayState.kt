package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.B2DModel
import com.mygdx.game.entities.Player

import com.mygdx.game.utils.MAX_Y_VELOCITY
import java.sql.DriverManager.println

class PlayState(gsm: GameStateManager, var model: B2DModel) : State(gsm) {

    private val player = model.player

    init{
        model.initEnemies()
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
