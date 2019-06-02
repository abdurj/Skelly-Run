package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.steer.behaviors.Arrive
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.entities.Bullet
import com.mygdx.game.entities.Enemy
import com.mygdx.game.entities.Player
import com.mygdx.game.entities.setup.SteeringEntity
import com.mygdx.game.utils.*

class B2DModel(private val controller: KeyboardController, private val camera: OrthographicCamera) {
    val world: World = World(Vector2(0f,-9.8f),true)
    //val world: World = World(Vector2(0f,0f),true)
    private val bodyFactory = BodyFactory(world)

    private val atlas = TextureAtlas("character_sprites/Player.pack")

    val map: TiledMap = TmxMapLoader().load("maps/map2.tmx")

    val tmr: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(map)

    val batch = SpriteBatch()

    val player = Player(bodyFactory, controller, atlas, camera)
    private val playerBody = player.playerBody
    private val target = player.playerEntity

    //private val follower = bodyFactory.makeCirclePolyBody(30f,100f,15f, STONE)
    //private val entity = SteeringEntity(follower, 15f)

    private val water = bodyFactory.makeBoxPolyBody(176f,40f,95f,48f, STONE, BodyDef.BodyType.StaticBody)

    var activeBullet = false
    var bullet:Bullet? = null

    //var w = 1f
    var height = 1f

    var spaceReleased = false
    var lastSpaceState = false

    private val enemy = Enemy(30f,100f,200f,bodyFactory,batch,playerBody)

    var enemies = Array<Enemy>()


    init {
        world.setContactListener(B2DContactListener(this))

        //playerBody
        val floor = bodyFactory.makeBoxPolyBody(0f, 32f, 150f, 1f, STONE, BodyDef.BodyType.StaticBody, true)
        floor.userData = "MainPlat"

        bodyFactory.makeAllFixturesSensors(water)
        water.userData = "water"

        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("collisionLayer").objects,"MainPlat")
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("noFricLayer").objects,"SidePlat")

        enemies.add(enemy)

/*
        val arriveSB = Arrive<Vector2>(entity,target)
                .setArrivalTolerance(2f)
                .setDecelerationRadius(10f)
        entity.setBehaviour(arriveSB)
  */  }

    fun update(deltaTime: Float){


        logicStep(deltaTime)

        player.update()
        for(x in 0 until enemies.size){
            var enemy = enemies[x]
            if(enemy.body.userData != null){
                enemy.update()
            }
            else{
                enemy.shoot.cancel()
                enemies.removeIndex(x)
            }
        }

        cameraStep(deltaTime)

        target.update(deltaTime)
        //entity.update(deltaTime)

        batch.projectionMatrix = camera.combined
        tmr.setView(camera)

        lastSpaceState = controller.space
    }

    fun render(){
        batchUpdate()
        tmr.render()
    }

    private fun batchUpdate(){
        batch.begin()
        player.drawPlayer(batch)
        batch.end()
    }

    private fun logicStep(deltaTime: Float){
        world.step(deltaTime,6,2)
        sweepDeadBodies()
    }

    fun sweepDeadBodies(){
        val bodies = Array<Body>()
        world.getBodies(bodies)
        run { val iter: Iterator<Body> = bodies.iterator()
            while (iter.hasNext())
            {
                var body: Body? = iter.next()
                val data = body?.userData
                if (data == "delete") {
                    world.destroyBody(body)
                    body?.userData = null
                }
                else if(data == "playerDelete"){
                    world.destroyBody(body)
                    body?.userData = null
                    //player.texture.dispose()
                }
            } }
    }




    private fun cameraStep(deltaTime: Float){
        val position: Vector3 = camera.position

        position.x = playerBody.position.x * PPM
        position.y = playerBody.position.y * PPM + 30
        camera.position.set(position)

        camera.update()
    }




}