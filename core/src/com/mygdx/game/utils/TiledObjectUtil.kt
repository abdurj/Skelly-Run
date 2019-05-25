package com.mygdx.game.utils

import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.*
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class TiledObjectUtil {


    companion object{
        fun parseTiledObjectLayer(world: World, objects:MapObjects){
            for(obj in objects){
                val shape: Shape

                if(obj is PolylineMapObject){
                    shape = createPolyLine(obj)
                }
                else if(obj is PolygonMapObject){
                    shape = createPolygon(obj)
                }
                else if(obj is RectangleMapObject){
                    val rect =  obj.rectangle
                    shape = PolygonShape()
                    shape.setAsBox(rect.width/ PPM,rect.height/ PPM,Vector2(rect.x,rect.y),0f)
                    //shape.setAsBox(rect.width/ PPM,rect.height/ PPM)
                }
                else{
                    continue
                }

                val body: Body;
                val bodyDef = BodyDef()
                bodyDef.type = BodyDef.BodyType.StaticBody
                body = world.createBody(bodyDef)

                body.createFixture(shape, 1.0f)
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
            val worldVertices: FloatArray = FloatArray(vertices.size)
            for(i in 0 until vertices.size){
                worldVertices[i] = vertices[i] / PPM
            }

            val polygonShape = PolygonShape()
            polygonShape.set(worldVertices)
            return polygonShape
        }

    }
}
