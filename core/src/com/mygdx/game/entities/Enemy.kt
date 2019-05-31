package com.mygdx.game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Timer
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class Enemy(private val x: Float,private val y: Float,private val shootInterval: Float,private val bodyFactory: BodyFactory, private val batch: SpriteBatch, private val player: Body) {

    internal var health = 50f;

    internal val body = bodyFactory.makeBoxPolyBody(x,y,20f,20f, STONE,BodyDef.BodyType.DynamicBody,true)

    internal var shooting = false


    init{
        body.userData = this
        body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
    }
    fun subtractHealth(damage: Float){
        health -= damage
    }

    fun update(){
        if(!detectPlayer(50f,50f)){
            moveSideToSide(10f,10f)
            shooting = false
        }
        else{
            body.linearVelocity = Vector2(0f,body.linearVelocity.y)
            println("In box Velocity: ${body.linearVelocity.x}")
            if(!shooting){
                shootBullets()
                shooting = true
            }
        }

    }

    private fun detectPlayer(leftBound: Float, rightBound: Float): Boolean{
        //println("Player position: ${player.position.x*PPM} LeftBound: ${body.position.x* PPM - leftBound} RightBound: ${body.position.x* PPM + rightBound}")
        val playerDetected = player.position.x * PPM < body.position.x * PPM + rightBound && player.position.x * PPM > body.position.x * PPM - leftBound
        return playerDetected
    }

    private fun moveSideToSide(leftBound: Float, rightBound: Float){
        //println("BodyPosition: ${body.position.x * PPM} LeftBound: ${x - leftBound} RightBound: ${x + rightBound}")
        if(body.position.x * PPM > x + rightBound){
            //body.setTransform(body.position.x - 1f,body.position.y,0f)
            if(body.linearVelocity.x > -MAX_ENEMY_X_VELOCITY)
                body.applyLinearImpulse(Vector2(-0.2f,0f),body.position,true)
            //body.applyForceToCenter(Vector2(-1f,0f),true)
        }
        else if(body.position.x * PPM < x - leftBound){
            //body.setTransform(body.position.x + 1f,body.position.y,0f)
            if(body.linearVelocity.x < MAX_ENEMY_X_VELOCITY)
              body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
        }
    }

    private fun shootBullets(){
        Timer.schedule(object: Timer.Task(){
            override fun run() {
                EnemyBullet(body,5f,5f,bodyFactory)
            }
        },0f,1f)
    }

}