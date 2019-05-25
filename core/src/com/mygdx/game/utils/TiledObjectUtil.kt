package com.mygdx.game.utils

import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.maps.objects.CircleMapObject

class TiledObjectUtil {


    companion object{
        fun parseTiledObjectLayer(world: World, objects:MapObjects,data:String){
            for(obj in objects){
                val shape: Shape

                if(obj is PolylineMapObject){
                    shape = createPolyLine(obj)
                }
                else if(obj is PolygonMapObject){
                    shape = createPolygon(obj)
                }
                else if(obj is RectangleMapObject){
                    shape = createRectangle(obj)
                }
                else if(obj is CircleMapObject){
                    shape = createCircle(obj)
                }
                /*else if(obj is EllipseMapObject){
                    shape = createEllipse(obj)
                }*/
                else{
                    continue
                }

                val body: Body
                val bodyDef = BodyDef()
                bodyDef.type = BodyDef.BodyType.StaticBody
                body = world.createBody(bodyDef)
                val fixture = FixtureDef()
                fixture.shape = shape
                fixture.density = 1.0f
                if(data == "MainPlat"){
                    fixture.friction = 1f
                }
                else{
                    fixture.friction = 0f
                }

                fixture.friction = 0f
                body.createFixture(fixture)
                body.userData = data
                shape.dispose()
            }

        }

        private fun createPolyLine(obj: PolylineMapObject): ChainShape {
            val vertices: FloatArray = obj.polyline.transformedVertices
            val worldVertices: Array<Vector2?> = arrayOfNulls<Vector2?>(vertices.size/2)

            for(i in 0 until worldVertices.size){
                worldVertices[i] = Vector2(vertices[i*2]/ PPM, vertices[i*2 + 1]/ PPM)
            }

            val cs = ChainShape()
            cs.createChain(worldVertices)
            return cs
        }

        private fun createPolygon(obj: PolygonMapObject): PolygonShape{
            val vertices: FloatArray = obj.polygon.transformedVertices
            val worldVertices = FloatArray(vertices.size)
            for(i in 0 until vertices.size){
                worldVertices[i] = vertices[i] / PPM
            }

            val polygonShape = PolygonShape()
            polygonShape.set(worldVertices)
            return polygonShape
        }

        private fun createRectangle(obj: RectangleMapObject): PolygonShape {
            val rectangle = obj.rectangle
            val polygon = PolygonShape()

            val size = Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                    (rectangle.y + rectangle.height * 0.5f) / PPM)

            polygon.setAsBox(rectangle.width * 0.5f / PPM,
                    rectangle.height * 0.5f / PPM,
                    size,
                    0.0f)

            return polygon
        }


        private fun createCircle(obj: CircleMapObject): CircleShape {
            val circle = obj.circle
            val circleShape = CircleShape()
            circleShape.radius = circle.radius
            circleShape.position = Vector2(circle.x / PPM, circle.y / PPM)
            return circleShape
        }

        /*
        private fun createEllipse(obj: EllipseMapObject): Shape {
            val ellipse = obj.ellipse
            val ellipseShape = CircleShape()
            ellipseShape.radius = (ellipse.circumference() / (2 * Math.PI)).toFloat()
            ellipseShape.position = Vector2(ellipse.x / PPM,ellipse.y/ PPM)
            return ellipseShape
        }*/

    }
}
