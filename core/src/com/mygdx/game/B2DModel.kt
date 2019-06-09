package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.mygdx.game.Parallax.ParallaxBackground
import com.mygdx.game.Parallax.ParallaxLayer
import com.mygdx.game.controller.KeyboardController
import com.mygdx.game.entities.Enemy
import com.mygdx.game.entities.Player
import com.mygdx.game.utils.*

class B2DModel(val controller: KeyboardController, val camera: OrthographicCamera) {
    //Create world with -9.8 gravity
    val world: World = World(Vector2(0f,-9.8f),true)
    //val world: World = World(Vector2(0f,0f),true)
    private val bodyFactory = BodyFactory(world)

    //Import Player texture atlas
    private val atlas = TextureAtlas("character_sprites/Player.pack")

    //Import map
    val map: TiledMap = TmxMapLoader().load("maps/map2.tmx")

    val tmr: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(map)

    val batch = SpriteBatch()

    //Create Player
    var player = Player(bodyFactory, controller, atlas)
    private val playerBody = player.playerBody

    //private val water = bodyFactory.makeBoxPolyBody(176f,40f,95f,48f, STONE, BodyDef.BodyType.StaticBody)

    private val forestPlat = bodyFactory.makeBoxPolyBody(0f,50f,50f,50f,STONE,BodyDef.BodyType.StaticBody)

    var lastSpaceState = false

    var enemies = Array<Enemy>()

    //Backgrounds
    val cityBackground: ParallaxBackground
    val forestBackground: ParallaxBackground
    val glacialBackground: ParallaxBackground

    var background: ParallaxBackground
    init {
        cityBackground = ParallaxBackground(
                arrayOf(
                        ParallaxLayer(TextureRegion(Texture("Parallax/CityParallax/far-buildings.png")), Vector2(0f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/CityParallax/back-buildings.png")), Vector2(0.05f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/CityParallax/foreground.png")), Vector2(0.2f,0f),Vector2(0f,0f)))
                ,camera.viewportWidth/1.5f,camera.viewportHeight/ 1.5f,Vector2(0f,0f))
        forestBackground = ParallaxBackground(
                arrayOf(
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-back-trees.png")), Vector2(0.01f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-lights.png")), Vector2(0.01f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-middle-trees.png")), Vector2(0.05f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-front-trees.png")), Vector2(0.2f,0f), Vector2(0f,0f))
                )
                ,camera.viewportWidth/1.5f,camera.viewportHeight/1.5f
                ,Vector2(0f,0f))
        glacialBackground = ParallaxBackground(
                arrayOf(
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-back-trees.png")), Vector2(0f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-lights.png")), Vector2(0f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-middle-trees.png")), Vector2(0f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/ForestParallax/parallax-forest-front-trees.png")), Vector2(0f,0f), Vector2(0f,0f))
                )
                ,camera.viewportWidth/1.5f,camera.viewportHeight/ 1.5f,Vector2(0f,0f))

        background = cityBackground


        //Initialize the enemies and players
        initPlayer()
        initEnemies()

        world.setContactListener(B2DContactListener(this))

        bodyFactory.makeAllFixturesSensors(forestPlat)
        forestPlat.userData = "forest"

        //val floor = bodyFactory.makeBoxPolyBody(0f,0f,5000f,1f,STONE,BodyDef.BodyType.StaticBody,true)
        //floor.userData = "MainPlat"
        //Parse the layers from the Tilemap
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("collisionLayer").objects,"MainPlat")
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("noFricLayer").objects,"SidePlat")

    }

    fun initEnemies(){
        //If there are currently enemies, delete them and cancel all shooting actions
        for(enemy in enemies){
            enemy.body.userData = "delete"
            enemy.shoot.cancel()
        }
        //Clear the enemy array
        enemies.clear()

        //Initialize enemies
        val enemy = Enemy(30f,100f,200f,bodyFactory,batch,playerBody)
        val enemy2 = Enemy(90f,100f,500f,bodyFactory,batch,playerBody)

        //Add enemies to array
        enemies.add(enemy)
        enemies.add(enemy2)
    }

    fun initPlayer(){
        //Initialize your player
        player.playerBody.setTransform(Vector2(150f/ PPM,100f/ PPM),0f)

        //Set player health to 100
        player.health = 100f

        player.playerBody.userData = player
    }


    fun update(deltaTime: Float){

        //Update your logic
        logicStep(deltaTime)
        //Update player
        player.update(deltaTime)

        //Update all enemies, if they have been deleted, cancel their shooting actions and remove them from the array

        for(x in enemies.size-1 downTo 0){
            val enemy = enemies[x]
            if(enemy.body.userData == null){
                enemy.shoot.cancel()
                enemies.removeIndex(x)
            }
        }

        for(x in 0 until enemies.size){
            enemies[x].update()
        }


        if(playerBody.linearVelocity.x > 0.2){
            background.speed.set(Vector2(playerBody.linearVelocity.x*16f,0f))
        }
        else{
            background.speed.set(Vector2(0f,0f))
        }

        //Update camera
        cameraStep()

        //Update sprite batch camera and tilemap camera
        batch.projectionMatrix = camera.combined
        tmr.setView(camera)

        lastSpaceState = controller.space
    }

    //Render spritebatch/tiledmap
    fun render(dt: Float){
        background.render(dt)

        batchUpdate()
        tmr.render()
    }

    private fun batchUpdate(){
        //Begin drawing the batch
        batch.begin()
        //Draw the player using the batch
        player.drawPlayer(batch)
        batch.end()
    }

    private fun logicStep(deltaTime: Float){
        //If player falls off the map, kill him to reset the game
        if(player.playerBody.position.y * PPM < -500){
            player.health -= 100
        }


        world.step(deltaTime,6,2)
        sweepDeadBodies()
    }

    //Run after a world step, deletes all bodies with the delete tag
    private fun sweepDeadBodies(){
        val bodies = Array<Body>()
        world.getBodies(bodies)
        run { val iter: Iterator<Body> = bodies.iterator()
            while (iter.hasNext())
            {
                val body: Body? = iter.next()
                val data = body?.userData
                if (data == "delete") {
                    world.destroyBody(body)
                    body.userData = null
                }
            } }
    }

    //Update the camera using the player's position
    private fun cameraStep() {
        val position: Vector3 = camera.position

        position.x = playerBody.position.x * PPM
        position.y = playerBody.position.y * PPM + 30
        camera.position.set(position)

        camera.update()
    }


}