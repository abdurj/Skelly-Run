package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.*

class B2DModel(val controller: KeyboardController, val camera: OrthographicCamera) {
    val world: World = World(Vector2(0f,-9.8f),true)
    var player: Body
    val bodyFactory = BodyFactory(world)
    val mPlat = bodyFactory.makePlatform(800f,400f,50f,25f,STONE, LOW_FRIC,BodyDef.BodyType.KinematicBody,Vector2(1f,0f))
    val mPlat2 = bodyFactory.makePlatform(850f, 400f, 50f, 25f, STONE, LOW_FRIC, BodyDef.BodyType.KinematicBody, Vector2(0f, 1f))
    var isSwimming = false
    var isJumping = false
    var doubleJump = false
    var onMPlat = false


    init {
        world.setContactListener(B2DContactListener(this))

        //player
        player = bodyFactory.makeBoxPolyBody(1f, 100f, 28f, 28f, STONE, BodyDef.BodyType.DynamicBody, true)

        val floor = bodyFactory.makeBoxPolyBody(400f, -5f, 1000f, 80f, STONE, BodyDef.BodyType.StaticBody, true)
        floor.userData = "MainPlat"

        val wall = bodyFactory.makeBoxPolyBody(-200f, 185f, 200f, 460f, LOW_FRIC, BodyDef.BodyType.StaticBody, true)

        val plat = bodyFactory.makePlatform(120f, 60f, 50f, 50f, STONE , LOW_FRIC)
        plat.userData = "MainPlat"

        val plat2 = bodyFactory.makePlatform(220f, 85f, 50f, 100f, STONE, LOW_FRIC)
        plat2.userData = "MainPlat"

        val plat3 = bodyFactory.makePlatform(320f, 160f, 50f, 250f,  STONE, LOW_FRIC)
        plat3.userData = "MainPlat"

        val plat4 = bodyFactory.makePlatform(500f, 275f, 50f, 25f,  STONE, LOW_FRIC)
        plat4.userData = "MainPlat"

        val plat5 = bodyFactory.makePlatform(680f, 240f, 100f, 410f,  STONE, LOW_FRIC)
        plat5.userData = "MainPlat"

        mPlat.userData = "MovingPlat"
    }


    fun logicStep(deltaTime: Float){

        if(mPlat.position.x * PPM >= 950f){
            mPlat.linearVelocity = Vector2(-1f, 0f)
        }
        else if (mPlat.position.x * PPM <= 770){
            mPlat.linearVelocity = Vector2(1f, 0f )
        }

        if(mPlat2.position.y * PPM >= 600f){
            mPlat2.linearVelocity = Vector2(0f, -1f)
        }

        else if(mPlat2.position.y * PPM <= 400f){
            mPlat2.linearVelocity = Vector2(0f, 1f)
        }

        if(controller.left && player.linearVelocity.x >= -3){
            player.applyLinearImpulse(Vector2(-1f,0f), Vector2(player.position.x,player.position.y),true)
            if (onMPlat){
                player.applyLinearImpulse(Vector2(-2f,0f), Vector2(player.position.x,player.position.y),true)
            }
        }

        if(controller.right && player.linearVelocity.x <= 3){
            player.applyLinearImpulse(Vector2(1f,0f),Vector2(player.position.x,player.position.y),true)
            if (onMPlat){
                player.applyLinearImpulse(Vector2(2f,0f), Vector2(player.position.x,player.position.y),true)
            }
        }

        else if(!controller.right && !controller.left){
            player.linearVelocity = Vector2(0f, player.linearVelocity.y)
        }

        if(!isJumping){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.linearVelocity.y <= 10){
                player.applyLinearImpulse(Vector2(0f,5f),Vector2(player.position.x,player.position.y),true)
                isJumping = true
                doubleJump = false
                onMPlat = false
            }
        }

        else if(isJumping && !doubleJump){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.linearVelocity.y <= 10){
                player.linearVelocity = Vector2(player.linearVelocity.x,0f)
                player.applyLinearImpulse(Vector2(0f,6.5f),Vector2(player.position.x,player.position.y),true)
                isJumping = true
                doubleJump = true
            }
        }


        if(isSwimming){
            player.applyForceToCenter(Vector2(0f,50f),false)
        }

        world.step(deltaTime,6,2)
    }

    fun cameraStep(deltaTime: Float){
        val position: Vector3 = camera.position

        position.x = player.position.x * PPM
        position.y = player.position.y * PPM + 30
        camera.position.set(position)

        camera.update()
    }
    private fun createObject(width: Int,height: Int,x: Int,y: Int, density:Float=1f, friction:Float = 0f, restitution:Float=0f): Body {
        val def = BodyDef()

        def.type = BodyDef.BodyType.DynamicBody

        //Set position
        def.position.set(x/ PPM,y/ PPM)
        def.fixedRotation = true

        val body:Body = world.createBody(def)

        //set shape of object
        val shape = PolygonShape()
        shape.setAsBox(width/2/ PPM,height/2/ PPM)

        //Properties of  the Object
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = density
        fixtureDef.friction = friction
        fixtureDef.restitution = restitution

        //Create object into the world with given properties
        body.createFixture(fixtureDef)

        //Dispose of unused shape since it is no longer needed
        shape.dispose()

        return body
    }

    private fun createFloor(width: Int,height: Int,x: Int,y: Int): Body {
        val groundBodyDef = BodyDef()
        //Set floor position
        groundBodyDef.position.set(x/ PPM,y/ PPM)

        //Create a body from its definition
        val groundBody: Body = world.createBody(groundBodyDef)

        //Create the shape
        val shape = PolygonShape()
        shape.setAsBox(width/2/PPM,height/2/PPM)

        groundBody.createFixture(shape,1f)

        shape.dispose()

        return groundBody
    }

    private fun createMovingObject(width: Int,height: Int,x: Int,y: Int,density: Float = 1f,friction: Float=0f,restitution: Float=0f, linearVelocity: Vector2 = Vector2(0f,0f)): Body {
        val kinematicBodyDef = BodyDef()

        kinematicBodyDef.type = BodyDef.BodyType.KinematicBody

        //Body properties/definition
        kinematicBodyDef.position.set(x/ PPM,y/ PPM)
        kinematicBodyDef.fixedRotation=true

        //Create body
        val kinematicBody: Body = world.createBody(kinematicBodyDef)

        //Create body shape
        val shape = PolygonShape()
        shape.setAsBox(width/2/ PPM,height/2/ PPM)

        //Create body fixture/properties
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = density
        fixtureDef.friction = friction
        fixtureDef.restitution = restitution

        kinematicBody.createFixture(fixtureDef)

        shape.dispose()

        kinematicBody.linearVelocity = linearVelocity

        return kinematicBody
    }

}