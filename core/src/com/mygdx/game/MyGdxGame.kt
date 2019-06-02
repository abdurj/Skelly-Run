package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.states.*
import com.mygdx.game.utils.*

const val SCALE: Float = 2.0f


class MyGdxGame : ApplicationAdapter() {

    //World, Camera, Renderer
    private lateinit var model: B2DModel
    private lateinit var b2dr: Box2DDebugRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var music: Music
    private lateinit var background: Texture
    private lateinit var batch: SpriteBatch
    private var gsm = GameStateManager()
    private val controller = KeyboardController()

    override fun create() {
        val width = Gdx.graphics.width.toFloat()
        val height = Gdx.graphics.height.toFloat()

        gsm = GameStateManager()

        background = Texture("images/forest.jpg")
        batch = SpriteBatch()

        camera = OrthographicCamera()
        camera.zoom *= 0.5f
        camera.setToOrtho(false, width / SCALE, height / SCALE)

        model = B2DModel(controller,camera)

        b2dr = Box2DDebugRenderer(true,true,true,true,true,true)

        music = Gdx.audio.newMusic(Gdx.files.internal("sds.mp3"))
        music.setLooping(true)
        music.setVolume(0.1f)
        music.play()

        gsm.push(MenuState(gsm))

    }

    override fun render() {
        //Render

        Gdx.input.inputProcessor = controller

        model.update(Gdx.graphics.deltaTime)

        batch.begin()
        batch.draw(background, 0f, 0f, 720f, 480f)
        batch.end()

        model.render()

        b2dr.render(model.world,camera.combined.scl(PPM))

        gsm.update(Gdx.graphics.getDeltaTime())
        gsm.render(batch)
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
        //model.player.texture.dispose()
        music.dispose()
        b2dr.dispose()
    }
}
