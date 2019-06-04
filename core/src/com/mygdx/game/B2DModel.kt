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

class B2DModel(val controller: KeyboardController, val camera: OrthographicCamera) {
    val world: World = World(Vector2(0f,-9.8f),true)
    //val world: World = World(Vector2(0f,0f),true)
    private val bodyFactory = BodyFactory(world)

    private val atlas = TextureAtlas("character_sprites/Player.pack")

    val map: TiledMap = TmxMapLoader().load("maps/map2.tmx")

    val tmr: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(map)

    val batch = SpriteBatch()

    var player = Player(bodyFactory, controller, atlas, camera)
    private val playerBody = player.playerBody

    //private val follower = bodyFactory.makeCirclePolyBody(30f,100f,15f, STONE)
    //private val entity = SteeringEntity(follower, 15f)

    private val water = bodyFactory.makeBoxPolyBody(176f,40f,95f,48f, STONE, BodyDef.BodyType.StaticBody)

    var activeBullet = false
    var bullet:Bullet? = null

    //var w = 1f
    var height = 1f

    var spaceReleased = false
    var lastSpaceState = false

    var enemies = Array<Enemy>()


    init {
        initEnemies()

        world.setContactListener(B2DContactListener(this))

        bodyFactory.makeAllFixturesSensors(water)
        water.userData = "water"

        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("collisionLayer").objects,"MainPlat")
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("noFricLayer").objects,"SidePlat")

    }

    fun initEnemies(){
        for(enemy in enemies){
            enemy.body.userData = "delete"
            enemy.shoot.cancel()
        }
        enemies.clear()

        val enemy = Enemy(30f,100f,200f,bodyFactory,batch,playerBody)
        val enemy2 = Enemy(90f,100f,500f,bodyFactory,batch,playerBody)

        enemies.add(enemy)
        enemies.add(enemy2)
    }

    fun initPlayer(){
        player.playerBody.setTransform(Vector2(150f/ PPM,100f/ PPM),0f)

        player.health = 100f
    }


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
        if(player.playerBody.position.y * PPM < -500){
            player.health -= 100
        }


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