package com.mygdx.game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.mygdx.game.BodyFactory
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.*

class Player(bodyFactory: BodyFactory, private val controller: KeyboardController, private val batch: SpriteBatch, val camera: OrthographicCamera){

    val texture = Texture("images/ichigo.png")

    val playerBody = bodyFactory.makeBoxPolyBody(150f, 100f, 14f, 14f, STONE, BodyDef.BodyType.DynamicBody, true)
    val playerEntity = SteeringEntity(playerBody,10f)
    var isSwimming = false
    var isJumping = false
    var doubleJump = false

    init{
        playerBody.userData = "player"
    }

    fun update(){
        //gravityMove()
        omniMove()
    }

    fun move(vector2: Vector2){
        playerBody.applyLinearImpulse(vector2,Vector2(playerBody.position.x,playerBody.position.y),true)
    }


    fun drawPlayer(){
        val xPos = playerBody.position.x * PPM - (0.43f*PPM / 2f)
        val yPos = playerBody.position.y * PPM - (0.43f*PPM / 2f)

        batch.draw(texture,xPos,yPos,14f,14f)
    }

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