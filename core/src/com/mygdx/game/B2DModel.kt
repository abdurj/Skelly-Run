package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
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

enum class Level{
    Level1,
    Level2,
    Level3,
    Level4,
    Level5
}

class B2DModel(val controller: KeyboardController, val camera: OrthographicCamera) {
    //Create world with -9.8 gravity
    val world: World = World(Vector2(0f,-9.8f),true)
    //val world: World = World(Vector2(0f,0f),true)
    private val bodyFactory = BodyFactory(world)

    var winGame = false

    //Import Player texture atlas
    private val atlas = TextureAtlas("character_sprites/Player.pack")

    //Import map
    var map: TiledMap = TmxMapLoader().load("maps/map.tmx")

    val tmr: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(map)

    val batch = SpriteBatch()

    val shapeRenderer = ShapeRenderer()

    //Create Player
    var player = Player(bodyFactory, controller, atlas)
    val health = Rectangle(Gdx.graphics.width/2f,Gdx.graphics.height/2f,player.health,1f)
    private val playerBody = player.playerBody


    private var portalSprite = Sprite()


    var lastSpaceState = false

    var currentLevel = Level.Level5
    var clearLevel = false

    var enemies = ArrayList<Enemy>()

    //Backgrounds
    private val cityBackground: ParallaxBackground
    private val forestBackground: ParallaxBackground
        private val glacialBackground: ParallaxBackground
        private val mountainBackground: ParallaxBackground
    private val industrialBackground: ParallaxBackground


    var background: ParallaxBackground

    lateinit var portal: Body
    init {
        shapeRenderer.color = Color(1f,0f,0f,0f)
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
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/sky.png")), Vector2(0.05f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/clouds_BG.png")), Vector2(0.1f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/mountains.png")), Vector2(0.2f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/clouds_MG_3.png")), Vector2(0.2f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/clouds_MG_2.png")), Vector2(0.2f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/clouds_MG_1.png")), Vector2(0.2f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/GlacialParallax/cloud_lonely.png")), Vector2(1.5f,0f), Vector2(0f,0f))

                )
                ,camera.viewportWidth/1.5f,camera.viewportHeight/ 1.5f,Vector2(0f,0f))
        mountainBackground = ParallaxBackground(
                arrayOf(
                        ParallaxLayer(TextureRegion(Texture("Parallax/MountainParallax/parallax-mountain-bg.png")), Vector2(0.05f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/MountainParallax/parallax-mountain-montain-far.png")), Vector2(0.1f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/MountainParallax/parallax-mountain-mountains.png")), Vector2(0.3f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/MountainParallax/parallax-mountain-trees.png")), Vector2(0.3f,0f), Vector2(0f,0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/MountainParallax/parallax-mountain-foreground-trees.png")), Vector2(0.8f,0f), Vector2(0f,0f))

                )
                ,camera.viewportWidth/1.5f,camera.viewportHeight/ 1.5f,Vector2(0f,0f))
        industrialBackground = ParallaxBackground(
                arrayOf(
                        ParallaxLayer(TextureRegion(Texture("Parallax/IndustrialParallax/skill-desc_0003_bg.png")), Vector2(0.05f, 0f), Vector2(0f, 0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/IndustrialParallax/skill-desc_0002_far-buildings.png")), Vector2(0.1f, 0f), Vector2(0f, 0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/IndustrialParallax/skill-desc_0001_buildings.png")), Vector2(0.3f, 0f), Vector2(0f, 0f)),
                        ParallaxLayer(TextureRegion(Texture("Parallax/IndustrialParallax/skill-desc_0000_foreground.png")), Vector2(0.5f, 0f), Vector2(0f, 75f))
                )
                , camera.viewportWidth / 1.5f, camera.viewportHeight/1.5f, Vector2(0f, 0f))

        background = cityBackground


        //Initialize the enemies and players
        initPlayer()
        initLevel()


        world.setContactListener(B2DContactListener(this))


    }

    fun initLevel(){
        //If there are currently enemies, delete them and cancel all shooting actions
        for(enemy in enemies){
            enemy.body.userData = "delete"
            enemy.shoot.cancel()
        }
        //Clear the enemy array
        enemies.clear()

        //Initialize enemies, adds enemies based on currentLevel
        when(currentLevel){
            Level.Level1 -> {
                map = TmxMapLoader().load("maps/map.tmx")
                tmr.map = map
                for (pair in enemyPosStageOne) {
                    enemies.add(Enemy(pair.first, pair.second, 1f, bodyFactory, batch, playerBody,atlas))
                }
                playerBody.setTransform(Vector2(150f/ PPM,100f/ PPM),playerBody.angle)
                portal = bodyFactory.makeBoxPolyBody(1335f,200f,50f,100f,STONE,BodyDef.BodyType.StaticBody)
                portal.userData = "portal1"

                background = forestBackground
                portalSprite = Sprite(Texture("images/portal1.png"))
                portalSprite.setSize(25f,50f)
                portalSprite.setPosition(portal.position.x* PPM - portalSprite.width/2,portal.position.y*PPM-portalSprite.height/2)


            }
            Level.Level2->{
                map = TmxMapLoader().load("maps/map2.tmx")
                tmr.map = map

                for (pair in enemyPosStageTwo) {
                    enemies.add(Enemy(pair.first, pair.second, 1f, bodyFactory, batch, playerBody,atlas))
                }

                background = mountainBackground

                portal = bodyFactory.makeBoxPolyBody(1220f,727f,50f,50f,STONE,BodyDef.BodyType.StaticBody)
                portal.userData = "portal2"
                portalSprite = Sprite(Texture("images/portal2.png"))
                portalSprite.setSize(25f,50f)
                portalSprite.setPosition(portal.position.x* PPM - portalSprite.width/2,portal.position.y*PPM-portalSprite.height/2)

            }
            Level.Level3 ->{
                map = TmxMapLoader().load("maps/map3.tmx")
                tmr.map = map

                for (pair in enemyPosStageThree) {
                    enemies.add(Enemy(pair.first, pair.second, 1f, bodyFactory, batch, playerBody,atlas))
                }

                background = glacialBackground

                portal = bodyFactory.makeBoxPolyBody(1390f,647f,50f,50f,STONE,BodyDef.BodyType.StaticBody)
                portal.userData = "portal3"
                portalSprite = Sprite(Texture("images/portal3.png"))
                portalSprite.setSize(25f,50f)
                portalSprite.setPosition(portal.position.x* PPM - portalSprite.width/2,portal.position.y*PPM-portalSprite.height/2)

            }
            Level.Level4 ->{
                map = TmxMapLoader().load("maps/map4.tmx")
                tmr.map = map

                for (pair in enemyPosStageFour) {
                    enemies.add(Enemy(pair.first, pair.second, 1f, bodyFactory, batch, playerBody,atlas))
                }

                background = industrialBackground

                portal = bodyFactory.makeBoxPolyBody(1522f,711f,50f,50f,STONE,BodyDef.BodyType.StaticBody)
                portal.userData = "portal4"
                portalSprite = Sprite(Texture("images/portal4.png"))
                portalSprite.setSize(25f,50f)
                portalSprite.setPosition(portal.position.x* PPM - portalSprite.width/2,portal.position.y*PPM-portalSprite.height/2)

            }
            Level.Level5->{
                map = TmxMapLoader().load("maps/map5.tmx")
                tmr.map = map

                for (pair in enemyPosStageFive) {
                    enemies.add(Enemy(pair.first, pair.second, 1f, bodyFactory, batch, playerBody,atlas))
                }

                background = cityBackground

                portal = bodyFactory.makeBoxPolyBody(2496f,448f,50f,50f,STONE,BodyDef.BodyType.StaticBody)
                portal.userData = "portal5"
                portalSprite = Sprite(Texture("images/portal5.png"))
                portalSprite.setSize(25f,50f)
                portalSprite.setPosition(portal.position.x* PPM - portalSprite.width/2,portal.position.y*PPM-portalSprite.height/2)

            }
        }


        playerBody.setTransform(Vector2(50f,150f/ PPM),playerBody.angle)

        bodyFactory.makeAllFixturesSensors(portal)
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("collisionLayer").objects,"MainPlat")
        TiledObjectUtil.parseTiledObjectLayer(world,map.layers.get("noFricLayer").objects,"SidePlat")

    }

    fun initPlayer(){
        //Initialize your player
        player.playerBody.setTransform(Vector2(50f/ PPM,100f/ PPM),0f)

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
                enemies.remove(enemy)
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

        //println("PLAYER POS X: ${playerBody.position.x*PPM} PLAYER POS Y: ${playerBody.position.y*PPM}")

        health.width = player.health / 5
        health.x = playerBody.position.x * PPM - health.width/2
        health.y = playerBody.position.y * PPM + 8f

        //Update camera
        cameraStep()

        //Update sprite batch camera and tilemap camera
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        tmr.setView(camera)

        lastSpaceState = controller.space

        if(winGame){
            currentLevel = Level.Level1
            clearLevel()
        }
    }

    //Render spritebatch/tiledmap
    fun render(dt: Float){


        background.render(dt)
        batchUpdate()
        tmr.render()
        //Render healthbar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.rect(health.x,health.y,health.width,health.height)
        shapeRenderer.end()

    }

    private fun batchUpdate(){
        //Begin drawing the batch
        batch.begin()
        //Draw the player using the batch
        player.drawPlayer(batch)
        for(enemy in enemies){
            enemy.render(batch)
        }
        portalSprite.draw(batch)
        batch.end()
    }

    private fun logicStep(deltaTime: Float){
        //If player falls off the map, kill him to reset the game
        if(player.playerBody.position.y * PPM < -500){
            player.health -= 100
        }


        world.step(deltaTime,6,2)
        sweepDeadBodies()
        if(clearLevel){
            clearLevel()
        }
        if(player.resetPlayer){
            resetPlayer()
        }
    }

    //Reset player back to start
    private fun resetPlayer(){
        player.playerBody.setTransform(Vector2(50f/PPM,50f/PPM),0f)
        player.resetPlayer = false
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

    private fun clearLevel(){
        val bodies = Array<Body>()
        world.getBodies(bodies)
        run { val iter: Iterator<Body> = bodies.iterator()
            while (iter.hasNext())
            {
                val body: Body? = iter.next()
                val data = body?.userData
                if (data != player) {
                    world.destroyBody(body)
                }

            } }
        clearLevel = false
        initLevel()
        playerBody.setTransform(Vector2(50f/PPM,150f/PPM),playerBody.angle)
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