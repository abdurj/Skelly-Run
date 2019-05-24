package com.mygdx.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.mygdx.game.utils.*
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.BodyDef



class BodyFactory(val world: World){

    fun makeCirclePolyBody(x:Float, y:Float, radius:Float, material:Int = 5, bodyType: BodyDef.BodyType = BodyDef.BodyType.DynamicBody, fixedRotation: Boolean = false): Body{
        val boxBodyDef = BodyDef()
        boxBodyDef.type = bodyType
        boxBodyDef.position.set(x/ PPM,y/ PPM)
        boxBodyDef.fixedRotation = true

        val boxBody = world.createBody(boxBodyDef)
        val circleShape = CircleShape()
        circleShape.radius = radius / 2 / PPM
        boxBody.createFixture( makeFixture(material,circleShape) )
        circleShape.dispose()
        return boxBody
    }

    fun makeBoxPolyBody(x: Float, y: Float, width: Float, height: Float, material: Int = 5, bodyType: BodyDef.BodyType = BodyDef.BodyType.DynamicBody, fixedRotation: Boolean = true): Body {

        val boxBodyDef = BodyDef()
        boxBodyDef.type = bodyType
        boxBodyDef.position.set(x/ PPM,y/ PPM)
        boxBodyDef.fixedRotation = fixedRotation

        val boxBody = world.createBody(boxBodyDef)

        val poly = PolygonShape()
        poly.setAsBox(width / 2 / PPM, height / 2 / PPM)
        boxBody.createFixture(makeFixture(material, poly))
        poly.dispose()

        return boxBody
    }

    fun makePolygonShapeBody(verticies: Array<Vector2>, x: Float, y: Float, material: Int = 5, bodyType: BodyDef.BodyType = BodyDef.BodyType.DynamicBody ): Body? {

        //Create the body definition
        val boxBodyDef = BodyDef()
        boxBodyDef.type = bodyType
        boxBodyDef.position.set(x/ PPM,y / PPM)

        //Create the body in the world using the definition
        val boxBody = world.createBody(boxBodyDef)

        val polygon = PolygonShape()
        verticies.forEach { vertex -> vertex.set( vertex.x/ PPM, vertex.y/ PPM) }
        polygon.set(verticies)

        boxBody.createFixture(makeFixture(material,polygon))
        polygon.dispose()
        return boxBody
    }

    fun makePlatform(x: Float, y: Float, width: Float, height: Float, material: Int = -1, sideMaterial: Int = -1, type: BodyDef.BodyType = BodyDef.BodyType.StaticBody,speed: Vector2 = Vector2(0f,0f)): Body{
        val mainPlat = makeBoxPolyBody(x,y,width,height,material,type)
        val leftPlat = makeBoxPolyBody(x-width/2, y, 1f, height, sideMaterial,type )
        val rightPlat = makeBoxPolyBody(x+width/2, y, 1f, height, sideMaterial, type)
        if(type == BodyDef.BodyType.KinematicBody){
            mainPlat.linearVelocity = speed
            leftPlat.linearVelocity = mainPlat.linearVelocity
            rightPlat.linearVelocity = mainPlat.linearVelocity
        }
        return mainPlat
    }

    fun makeAllFixturesSensors(body: Body){
        for(fixture in body.fixtureList){
            fixture.isSensor = true
        }
    }

}



fun makeFixture(material: Int, shape: Shape): FixtureDef{
    val fixtureDef = FixtureDef()
    fixtureDef.shape = shape
    when(material){
        STEEL -> {
            fixtureDef.density = 1f
            fixtureDef.friction = 0.3f
            fixtureDef.restitution = 0.1f
        }
        WOOD -> {
            fixtureDef.density = 0.5f
            fixtureDef.friction = 0.7f
            fixtureDef.restitution = 0.3f
        }
        RUBBER -> {
            fixtureDef.density = 1f
            fixtureDef.friction = 0f
            fixtureDef.restitution = 1f
        }
        STONE -> {
            fixtureDef.density = 1f
            fixtureDef.friction = 1.5f
            fixtureDef.restitution = 0.01f
        }
        LOW_FRIC -> {
            fixtureDef.density = 1f
            fixtureDef.friction = 0f
            fixtureDef.restitution = 0f
        }
        HIGH_FRIC -> {
            fixtureDef.density = 1f
            fixtureDef.friction = 10f
            fixtureDef.restitution = 0f
        }
        else -> {
            fixtureDef.density = 10f
            fixtureDef.friction = 3f
            fixtureDef.restitution = 0f
        }
    }
    return fixtureDef
}
