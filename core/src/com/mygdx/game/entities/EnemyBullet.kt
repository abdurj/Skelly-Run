package com.mygdx.game.entities

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class EnemyBullet(parent: Body,  private var width: Float, private var height: Float, private val bodyFactory: BodyFactory, private var left: Boolean) {
    val damage = 10f

    val body: Body

    init{

        if(left) {
            body = bodyFactory.makeBoxPolyBody(parent.position.x * PPM - 16, parent.position.y * PPM, width, height, LOW_FRIC)
            body.gravityScale = 0f
            body.isBullet = true
            body.userData = this
            body.applyLinearImpulse(Vector2(-0.1f,0f),body.position,true)
        }
        else{
            body = bodyFactory.makeBoxPolyBody(parent.position.x * PPM+16,parent.position.y * PPM,width,height, LOW_FRIC)
            body.gravityScale = 0f
            body.isBullet = true
            body.userData = this
            body.applyLinearImpulse(Vector2(0.1f,0f),body.position,true)
        }


    }
}