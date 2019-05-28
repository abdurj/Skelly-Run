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
import com.mygdx.game.utils.PPM
import com.mygdx.game.utils.STONE

class Bullet(private var player: Body,private var width: Float, private var height: Float, private val bodyFactory: BodyFactory, private val controller: KeyboardController){
    internal var damage = 0f
    var shot = false
    val body = bodyFactory.makeBoxPolyBody(player.position.x + 16/ PPM, player.position.y, width, height, STONE)
    val maxWidth = 10f;
    val maxHeight = 10f;


    init{
        body.userData = this
    }

    fun update(widthIncrement: Float,heightIncrement: Float,player: Body){
        if(controller.space) {
            chargeBullet(widthIncrement, heightIncrement)
        }
        setPosition(player)
        damage = height
    }

    fun release() {
        println("bang")
        body.applyLinearImpulse(Vector2(0.1f,0f),body.position,true)
        shot = true
    }

    private fun chargeBullet(widthIncrement: Float, heightIncrement: Float){
        println("Charging bullet")
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
        body.setTransform(player.position.x + 16/ PPM, player.position.y, body.angle)
    }


}