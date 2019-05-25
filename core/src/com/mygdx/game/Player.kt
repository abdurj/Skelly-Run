package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.*

class Player(bodyFactory: BodyFactory, private val controller: KeyboardController, private val batch: SpriteBatch, val camera: OrthographicCamera){

    val texture = Texture("images/ichigo.png")

    val playerBody = bodyFactory.makeBoxPolyBody(150f, 100f, 28f, 28f, STONE, BodyDef.BodyType.DynamicBody, true)
    var isSwimming = false
    var isJumping = false
    var doubleJump = false

    fun update(){
        if(controller.left && playerBody.linearVelocity.x >= -MAX_X_VELOCITY){
            playerBody.applyLinearImpulse(Vector2(-1.5f,0f), Vector2(playerBody.position.x,playerBody.position.y),true)
        }

        if(controller.right && playerBody.linearVelocity.x <= MAX_X_VELOCITY){
            playerBody.applyLinearImpulse(Vector2(1.5f,0f), Vector2(playerBody.position.x,playerBody.position.y),true)
        }

        else if(!controller.right && !controller.left){
            playerBody.linearVelocity = Vector2(0f, playerBody.linearVelocity.y)
        }

        //if(!isJumping){
        if(true){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && playerBody.linearVelocity.y <= MAX_Y_VELOCITY){
                playerBody.applyLinearImpulse(Vector2(0f,5f), Vector2(playerBody.position.x,playerBody.position.y),true)
                isJumping = true
                doubleJump = false
            }
        }
        else if(isJumping && !doubleJump){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && playerBody.linearVelocity.y <= 10){
                playerBody.linearVelocity = Vector2(playerBody.linearVelocity.x,0f)
                playerBody.applyLinearImpulse(Vector2(0f,6.5f), Vector2(playerBody.position.x,playerBody.position.y),true)
                isJumping = true
                doubleJump = true
            }
        }


        if(isSwimming){
            playerBody.applyForceToCenter(Vector2(0f,50f),false)
        }
    }


    fun drawPlayer(){
        val xPos = playerBody.position.x * PPM - (PPM / 2)
        val yPos = playerBody.position.y * PPM - (PPM / 2)

        batch.draw(texture,xPos,yPos,32f,32f)
    }

}