package com.mygdx.game.entities

import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Timer
import com.mygdx.game.BodyFactory
import com.mygdx.game.utils.*

class Enemy(private val x: Float, private val y: Float,private val shootInterval: Float,private val bodyFactory: BodyFactory, private val batch: SpriteBatch, private val player: Body, atlas: TextureAtlas) {

    internal var health = 10f;

    internal val body = bodyFactory.makeBoxPolyBody(x,y,20f,20f, STONE,BodyDef.BodyType.DynamicBody,true)

    private val region = atlas.findRegion("GXM00")

    private val texture = TextureRegion(region, 64,64,32,32)

    private val idleFrames =  Array<TextureRegion>()

    val sprite: Sprite
    val animation: Animation<TextureRegion>

    internal var notShooting = false
    val bullets = ArrayList<EnemyBullet>()
    var previousPosition = Vector2(0f,0f)

    //Timer task the shoots the bullet at the player based on their position
    val shoot = object: Timer.Task() {
        override fun run() {
            println("shot")
            if(player.position.x < body.position.x) {
                bullets.add(EnemyBullet(this@Enemy, 10f, 10f, bodyFactory,true))
            }
            else{
                bullets.add(EnemyBullet(this@Enemy,10f,10f,bodyFactory,false))
            }
        }
    }


    init{

        sprite = Sprite(texture)

        //Add all frames of player animation to the array
        idleFrames.add(TextureRegion(region,0,0,32,32))
        idleFrames.add(TextureRegion(region,32,0,32,32))
        idleFrames.add(TextureRegion(region,64,0,32,32))
        idleFrames.add(TextureRegion(region,0,32,32,32))
        idleFrames.add(TextureRegion(region,32,32,32,32))
        idleFrames.add(TextureRegion(region,64,32,32,32))
        idleFrames.add(TextureRegion(region,0,64,32,32))
        idleFrames.add(TextureRegion(region,32,64,32,32))
        idleFrames.add(TextureRegion(region,64,64,32,32))

        //Setup idle animation using the frames
        animation = Animation(0.2f,idleFrames)


        sprite.setSize(25f,25f)
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
        sprite.setPosition(body.position.x*PPM - sprite.width/2,body.position.y * PPM - sprite.height/2 +2.5f)
    }

    fun render(batch: SpriteBatch){

        sprite.draw(batch)

        for(bullet in bullets) {
                if (bullet.left == true) {
                    bullet.sprite.setFlip(true, false)
                } else {
                    bullet.sprite.setFlip(false, false)
                }
                bullet.sprite.setPosition(bullet.body.position.x * PPM - bullet.width / 2, bullet.body.position.y * PPM - bullet.height / 2)
                bullet.sprite.draw(batch)
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
        }
        else if(body.position.x * PPM < x - leftBound){
            if(body.linearVelocity.x < MAX_ENEMY_X_VELOCITY)
                body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
        }
        else
            if(Math.abs(body.linearVelocity.x) < MAX_ENEMY_X_VELOCITY){
                body.applyLinearImpulse(Vector2(0.2f,0f),body.position,true)
            }

        if(body.linearVelocity.x > 0){
            sprite.setFlip(true,false)
        }
        else if(body.linearVelocity.x < 0){
            sprite.setFlip(false,false)
        }

        previousPosition = body.position
    }

    //Schedule bullet shooting to happen once every second
    private fun shootBullets(){
        Timer.schedule(shoot,0f,1f)
    }

}