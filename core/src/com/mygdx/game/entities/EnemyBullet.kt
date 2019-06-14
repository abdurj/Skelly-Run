package com.mygdx.game.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class EnemyBullet(internal val parent: Enemy, internal var width: Float, internal var height: Float, private val bodyFactory: BodyFactory, internal var left: Boolean) {
    val damage = 10f

    val body: Body
    val sprite = Sprite(Texture("images/fireball.png"))


    init{

        if(left) {
            body = bodyFactory.makeBoxPolyBody(parent.body.position.x * PPM - 16, parent.body.position.y * PPM, width, height, LOW_FRIC)
            body.gravityScale = 0f
            body.isBullet = true
            body.userData = this
            body.applyLinearImpulse(Vector2(-0.1f,0f),body.position,true)
        }
        else{
            body = bodyFactory.makeBoxPolyBody(parent.body.position.x * PPM+16,parent.body.position.y * PPM,width,height, LOW_FRIC)
            body.gravityScale = 0f
            body.isBullet = true
            body.userData = this
            body.applyLinearImpulse(Vector2(0.1f,0f),body.position,true)
        }

        sprite.setSize(width,height)
    }
    fun update(){
        sprite.setPosition(body.position.x,body.position.y)
    }
}