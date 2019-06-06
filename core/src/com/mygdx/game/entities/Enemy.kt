package com.mygdx.game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Timer
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class Enemy(private val x: Float,private val y: Float,private val shootInterval: Float,private val bodyFactory: BodyFactory, private val batch: SpriteBatch, private val player: Body) {

    internal var health = 10f;

    internal val body = bodyFactory.makeBoxPolyBody(x,y,20f,20f, STONE,BodyDef.BodyType.DynamicBody,true)

    internal var notShooting = false

    //Timer task the shoots the bullet at the player based on their position
    val shoot = object: Timer.Task() {
        override fun run() {
            println("shot")
            if(player.position.x < body.position.x) {
                EnemyBullet(body, 5f, 10f, bodyFactory,true)
            }
            else{
                EnemyBullet(body,5f,10f,bodyFactory,false)
            }
        }
    }


    init{
        body.userData = this
        //body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
    }
    fun subtractHealth(damage: Float){
        health -= damage
    }

    fun update(){
        if(!detectPlayer(50f,50f)){
            shoot.cancel()
            moveSideToSide(10f,10f)
            notShooting = true
        }
        else{
            body.linearVelocity = Vector2(0f,body.linearVelocity.y)
            //println("In box Velocity: ${body.linearVelocity.x}")
            if(notShooting){
                shootBullets()
                notShooting = false
            }
        }
    }

    //Check if a player is within left and right bounds
    private fun detectPlayer(leftBound: Float, rightBound: Float): Boolean{
        //println("Player position: ${player.position.x*PPM} LeftBound: ${body.position.x* PPM - leftBound} RightBound: ${body.position.x* PPM + rightBound}")
        val xPlayerDetected = player.position.x * PPM < body.position.x * PPM + rightBound && player.position.x * PPM > body.position.x * PPM - leftBound
        val yPlayerDetected = player.position.y < body.position.y + 15/PPM && player.position.y > body.position.y - 15/PPM
        return xPlayerDetected && yPlayerDetected
    }

    //Move side to side within the left and right bounds
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
        else
            if(Math.abs(body.linearVelocity.x) < MAX_ENEMY_X_VELOCITY){
                body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
            }
    }

    //Schedule bullet shooting to happen once every second
    private fun shootBullets(){
        Timer.schedule(shoot,0f,1f)
    }

}