package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.utils.PPM

const val SCALE: Float = 2.0f


class MyGdxGame : ApplicationAdapter() {

    //World, Camera, Renderer
    private lateinit var model: B2DModel
    private lateinit var b2dr: Box2DDebugRenderer
    private lateinit var camera: OrthographicCamera
    private val controller = KeyboardController()

    override fun create() {
        val width = Gdx.graphics.width.toFloat()
        val height = Gdx.graphics.height.toFloat()

        camera = OrthographicCamera()
        camera.setToOrtho(false, width / SCALE, height / SCALE)

        model = B2DModel(controller,camera)

        b2dr = Box2DDebugRenderer(true,true,true,true,true,true)

    }

    override fun render() {
        //Render

        Gdx.input.inputProcessor = controller

        model.update(Gdx.graphics.deltaTime)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        model.render()

        b2dr.render(model.world,camera.combined.scl(PPM))
    }


    fun update(deltaTime: Float){
        model.update(deltaTime)
    }


    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width/SCALE,height/SCALE)
    }

    override fun dispose() {
        model.world.dispose()
        model.batch.dispose()
        model.tmr.dispose()
        model.map.dispose()
        model.player.texture.dispose()
        b2dr.dispose()
    }
}
