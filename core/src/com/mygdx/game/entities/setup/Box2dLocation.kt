package com.mygdx.game.entities.setup

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.utils.SteeringUtil

class Box2dLocation() : Location<Vector2>{

    internal val position = Vector2()
    internal var orientation = 0f

    override fun newLocation(): Location<Vector2> {
        return Box2dLocation()
    }

    override fun setOrientation(orientation: Float) {
        this.orientation = orientation
    }

    override fun getOrientation(): Float {
        return orientation
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return SteeringUtil.angleToVector(outVector,angle)
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return SteeringUtil.vectorToAngle(vector)
    }

    override fun getPosition(): Vector2 {
        return position
    }

}