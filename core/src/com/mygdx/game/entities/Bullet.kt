package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.mygdx.game.B2DModel
import com.mygdx.game.BodyFactory
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.LOW_FRIC
import com.mygdx.game.utils.PPM
import com.mygdx.game.utils.STONE

class Bullet(private var player: Body,private var width: Float, private var height: Float, private val bodyFactory: BodyFactory, private val controller: KeyboardController, private var right: Boolean){
    internal var damage = 0f
    var shot = false
    val maxWidth = 10f;
    val maxHeight = 10f;

    val body: Body

    init{
        if(right){
            body = bodyFactory.makeBoxPolyBody(player.position.x + 16/ PPM, player.position.y, width, height, LOW_FRIC)

        }
        else{
            body = bodyFactory.makeBoxPolyBody(player.position.x - 16/ PPM, player.position.y, width, height, LOW_FRIC)
        }
        body.isBullet = true
        body.userData = this
        body.gravityScale = 0.1f
    }

    fun update(widthIncrement: Float,heightIncrement: Float,player: Body){
        if(controller.space) {
            chargeBullet(widthIncrement, heightIncrement)
        }
        if(controller.left){
            println("left")
            right = false
        }
        if(controller.right){
            println("right")
            right = true
        }
        setPosition(player)
        damage = height
    }

    fun release() {
        if (right) {
            println("bang right")
            body.applyLinearImpulse(Vector2(0.1f, 0f), body.position, true)
            shot = true
        }
        else{
            println("bang left")
            body.applyLinearImpulse(Vector2(-0.1f, 0f), body.position, true)
            shot = true
        }
    }

    private fun chargeBullet(widthIncrement: Float, heightIncrement: Float){
        shot = false
        //println("Charging bullet")
        body.destroyFixture(body.fixtureList.first())
        val shape = PolygonShape()
        if(width<maxWidth && height < maxHeight){
            shape.setAsBox(width/2/PPM,height/2/ PPM)

            width += widthIncrement
            height += heightIncrement
        }
        else
            shape.setAsBox(width/2/ PPM,maxHeight/2/ PPM)

        body.createFixture(shape,1f)
        shape.dispose()

    }

    private fun setPosition(player:Body){
        if (right) {
            body.setTransform(player.position.x + 16 / PPM, player.position.y, body.angle)
        }
        else{
            body.setTransform(player.position.x - 16 / PPM, player.position.y, body.angle)
        }
    }


}