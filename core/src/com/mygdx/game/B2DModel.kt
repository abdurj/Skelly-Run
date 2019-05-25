package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.*

class B2DModel(val controller: KeyboardController, private val camera: OrthographicCamera) {
    val world: World = World(Vector2(0f,-9.8f),true)
    private val bodyFactory = BodyFactory(world)

    val map: TiledMap = TmxMapLoader().load("maps/map.tmx")

    val tmr: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(map)

    val batch = SpriteBatch()

    val player = Player(bodyFactory,controller,batch,camera)
    private val playerBody = player.playerBody


    init {
        world.setContactListener(B2DContactListener(this))

        //playerBody
        val floor = bodyFactory.makeBoxPolyBody(0f, 32f, 150f, 1f, STONE, BodyDef.BodyType.StaticBody, true)
        floor.userData = "MainPlat"

        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("collisionLayer").objects)
    }

    fun update(deltaTime: Float){

        logicStep(deltaTime)

        player.update()
        cameraStep(deltaTime)

        batch.projectionMatrix = camera.combined
        tmr.setView(camera)
    }

    fun render(){
        batchUpdate()
        tmr.render()
    }

    private fun batchUpdate(){
        batch.begin()
        player.drawPlayer()
        batch.end()
    }

    private fun logicStep(deltaTime: Float){
        world.step(deltaTime,6,2)
    }

    private fun cameraStep(deltaTime: Float){
        val position: Vector3 = camera.position

        position.x = playerBody.position.x * PPM
        position.y = playerBody.position.y * PPM + 30
        camera.position.set(position)

        camera.update()
    }

}