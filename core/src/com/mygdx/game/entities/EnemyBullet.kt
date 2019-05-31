package com.mygdx.game.entities

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class EnemyBullet(private var parent: Body, private var width: Float, private var height: Float, private val bodyFactory: BodyFactory) {
    val damage = 10f
    val body = bodyFactory.makeBoxPolyBody(parent.position.x * PPM + 16/ PPM, parent.position.y*PPM, width, height, LOW_FRIC)

    init{
        body.isBullet = true
        body.userData = this
        body.applyLinearImpulse(Vector2(5f,0f),body.position,true)
    }
}