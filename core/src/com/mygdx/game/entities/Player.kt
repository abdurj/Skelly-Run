package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Array
import com.mygdx.game.BodyFactory
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.entities.setup.SteeringEntity
import com.mygdx.game.utils.*
import com.mygdx.game.entities.State.*

enum class State{
    FALLING,
    JUMPING,
    DOUBLE_JUMPING,
    CHARGING,
    SHOOTING,
    MOVING_LEFT,
    MOVING_RIGHT,
    STANDING
}

class Player(private val bodyFactory: BodyFactory, private val controller: KeyboardController, atlas: TextureAtlas){

    private val region = atlas.findRegion("GXM00")

    private val texture = TextureRegion(region, 64,64,32,32)

    private val idleFrames =  Array<TextureRegion>()

    private val sprite: Sprite

    var currentPlayerState: State = STANDING
    var previousPlayerState: State = FALLING
    var lastPlayerState = FALLING


    private val w = 14f
    private val h = 14f

    val playerBody = bodyFactory.makeBoxPolyBody(150f, 100f, w, h, STONE, BodyDef.BodyType.DynamicBody, true)
    val playerEntity = SteeringEntity(playerBody, 10f)
    var isSwimming = false
    var isJumping = false
    var doubleJump = false

    var lastSpaceState = false
    var spaceReleased = false

    var activeBullet = false

    var bullet: Bullet? = null

    var right = false

    private var stateTimer = 0f

    var health = 100f

    private val idleAnimation: Animation<TextureRegion>
    internal val bullets = ArrayList<Bullet>()

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
        idleAnimation = Animation(0.2f,idleFrames)


        sprite.setSize(w+7f,h+7f)
        playerBody.angularDamping = 100f
        playerBody.userData = this
    }

    fun update(dt: Float){

        //Update player states based on player actions
        currentPlayerState = STANDING

        if (controller.left){
            right = false
            currentPlayerState = MOVING_LEFT
        }
        if (controller.right){
            right = true
            currentPlayerState = MOVING_RIGHT
        }

        spaceReleased = lastSpaceState == true && controller.space == false
        gravityMove()
        //omniMove()

        if(playerBody.linearVelocity.y > 0.1 && isJumping){
            currentPlayerState = JUMPING
        }
        else if(doubleJump){
            currentPlayerState = DOUBLE_JUMPING
        }
        if(playerBody.linearVelocity.y < -0.1){
            currentPlayerState = FALLING
        }

        bulletLogic()

        lastSpaceState = controller.space

        if(previousPlayerState != currentPlayerState) {
            lastPlayerState = previousPlayerState
        }

        sprite.setRegion(getFrame(dt))

        if(right){
            sprite.flip(true,false)
        }
        else{
            sprite.flip(false,false)
        }
        previousPlayerState = currentPlayerState

        //println("State: $currentPlayerState Last State: $lastPlayerState")
    }

    private fun getFrame(dt: Float): TextureRegion {
        val region: TextureRegion

        when(currentPlayerState) {
            STANDING -> {
                region = idleAnimation.getKeyFrame(stateTimer)
            }
            //FALLING -> TODO()
            //JUMPING -> TODO()
            //DOUBLE_JUMPING -> TODO()
            //CHARGING ->
            //SHOOTING -> TODO()
            MOVING_LEFT -> {
                region = TextureRegion(this.region,64,64,32,32)
            }
            MOVING_RIGHT -> {
                region = TextureRegion(this.region, 64, 64, 32, 32)
            }
            else -> {
                region = TextureRegion(this.region, 64, 64, 32, 32)
            }

        }

        //println("Current player state $currentPlayerState Previous state $previousPlayerState")

        if(currentPlayerState == previousPlayerState){
            if(stateTimer > idleAnimation.frameDuration * idleFrames.size){
                stateTimer = 0f
            }
            stateTimer += dt
        }
        else{
            stateTimer = 0f
        }
        return region
    }

    private fun getState(): State {
        return currentPlayerState
    }

    private fun bulletLogic() {
        //Increase bullet size as long as you hold space
        if(controller.space) {
            if (!activeBullet) {
                bullet = Bullet(playerBody, 1f,1f, bodyFactory, controller,right)
                bullet!!.maxHeight = 5f
                bullet!!.maxWidth = 5f
                bullets.add(bullet!!)
                activeBullet = true
            } else {
                bullet?.update(0.1f, 0.1f, playerBody)
            }
            currentPlayerState = CHARGING
        }
        //When you release space, shoot the bullet
        if(spaceReleased){
            bullet?.release()
            activeBullet = false
            currentPlayerState = SHOOTING
        }
    }

    fun move(vector2: Vector2){
        //Move based on the vector given
        playerBody.applyLinearImpulse(vector2,Vector2(playerBody.position.x,playerBody.position.y),true)
    }


    fun drawPlayer(batch: SpriteBatch){
        //draw the sprite ontop of the player
        val xPos = playerBody.position.x * PPM - ((w+7f) / 2f)
        val yPos = playerBody.position.y * PPM - ((h) / 2f)

        sprite.setPosition(xPos,yPos)
        for(bullet in bullets) {
            if(bullet.drawSprite) {
                if (bullet.right == true) {
                    bullet.sprite.setFlip(false, false)
                } else {
                    bullet.sprite.setFlip(true, false)
                }
                bullet.sprite.setPosition(bullet.body.position.x * PPM - bullet.width / 2, bullet.body.position.y * PPM - bullet.height / 2)
                bullet.sprite.draw(batch)
            }
        }
        sprite.draw(batch)
    }

    //Move with gravity
    fun gravityMove(){
        if(isSwimming && playerBody.linearVelocity.y <= MAX_Y_VELOCITY){
            playerBody.applyForceToCenter(Vector2(0f,2f),true)
        }

        if(controller.left && playerBody.linearVelocity.x >= -MAX_X_VELOCITY){
            move(Vector2(-0.5f,0f))
        }

        if(controller.right && playerBody.linearVelocity.x <= MAX_X_VELOCITY){
            move(Vector2(0.5f,0f))
        }

        else if(!controller.right && !controller.left){
            playerBody.linearVelocity = Vector2(0f, playerBody.linearVelocity.y)
        }

        if(!isJumping){
            //if(true){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && playerBody.linearVelocity.y <= MAX_Y_VELOCITY){
                move(Vector2(0f,1f))
                isJumping = true
                doubleJump = false
            }
        }
        else if(isJumping && !doubleJump){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && playerBody.linearVelocity.y <= MAX_Y_VELOCITY){
                playerBody.linearVelocity = Vector2(playerBody.linearVelocity.x,0f)

                move(Vector2(0f,1f))
                isJumping = true
                doubleJump = true
            }
        }
    }

    //Move with no gravity
    fun omniMove(){
        if(controller.up && playerBody.linearVelocity.y <= MAX_Y_VELOCITY){
            move(Vector2(0f,0.5f))
        }
        if(controller.down && playerBody.linearVelocity.y >= -MAX_Y_VELOCITY){
            move(Vector2(0f,-0.5f))
        }
        if(!controller.up && !controller.down){
            playerBody.linearVelocity = Vector2(playerBody.linearVelocity.x,0f)
        }

        if(controller.left && playerBody.linearVelocity.x >= -MAX_X_VELOCITY){
            move(Vector2(-0.5f,0f))
        }
        if(controller.right && playerBody.linearVelocity.x <= MAX_X_VELOCITY){
            move(Vector2(0.5f,0f))
        }

        if(!controller.left && !controller.right){
            playerBody.linearVelocity = Vector2(0f, playerBody.linearVelocity.y)
        }
    }


}