package com.mygdx.game.entities

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.mygdx.game.utils.SteeringUtil
import sun.awt.windows.ThemeReader.getPosition



class SteeringEntity(internal val body: Body, internal val boundingRadius: Float) : Steerable<Vector2>{

    internal var maxLinearSpeed = 30f
    internal var maxAngularSpeed = 5f
    internal var maxLinearAcceleration = 75f
    internal var maxAngularAcceleration = 5f

    internal var zeroThreshold = 0.1f
    internal var tagged = false

    internal var behavior: SteeringBehavior<Vector2>? = null
    internal var steeringOutput: SteeringAcceleration<Vector2> = SteeringAcceleration<Vector2>(Vector2())

    fun update(delta: Float){
        if(behavior!=null)
            behavior?.calculateSteering(steeringOutput)
            applySteering(delta)
    }

    fun applySteering(delta: Float){
        var anyAccelerations = false

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true)
            anyAccelerations = true
        }

        // Update orientation and angular velocity
        if (steeringOutput.angular != 0f) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true)
                anyAccelerations = true
            }
            // If we haven't got any velocity, then we can do nothing.

        val linVel = linearVelocity
        if (!linVel.isZero(zeroLinearSpeedThreshold)) {
            val newOrientation = vectorToAngle(linVel)
            body.angularVelocity = (newOrientation - angularVelocity) * delta // this is superfluous if independentFacing is always true
            body.setTransform(body.position, newOrientation)
        }


        if (anyAccelerations) {
            // Cap the linear speed
            val velocity = body.linearVelocity
            val currentSpeedSquare = velocity.len2()
            val maxLinearSpeed = getMaxLinearSpeed()
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.linearVelocity = velocity.scl(maxLinearSpeed / Math.sqrt(currentSpeedSquare.toDouble()).toFloat())
            }
            // Cap the angular speed
            val maxAngVelocity = getMaxAngularSpeed()
            if (body.angularVelocity > maxAngVelocity) {
                body.angularVelocity = maxAngVelocity
            }
        }
    }

/*
    fun applySteering(delta: Float){
        var anyAcceleration = false

        if(!steeringOutput.linear.isZero){
            val force = steeringOutput.linear.scl(delta)
            body.applyForceToCenter(force,true)
            anyAcceleration = true
        }

        if (steeringOutput.angular != 0f){
            body.applyTorque(steeringOutput.angular, true);
            anyAcceleration = true;
        }
        else{
            val linVel = linearVelocity
            if(!linVel.isZero){
                val newOrientation = vectorToAngle(linVel)
                body.angularVelocity = newOrientation-angularVelocity*delta
                body.setTransform(body.position,newOrientation)
            }
        }

        if(anyAcceleration){
            val velocity = body.linearVelocity
            val currentSpeedSquare = velocity.len2()

            //Linear Limit
            if(currentSpeedSquare > maxLinearSpeed*maxLinearSpeed){
                body.setLinearVelocity(velocity.scl(maxLinearSpeed/(Math.sqrt(currentSpeedSquare.toDouble()).toFloat())))
            }
            //Angular Limit
            if(body.angularVelocity>maxAngularSpeed){
                body.angularVelocity = maxAngularSpeed
            }
        }

    }
*/

    //Getters - Setters

    fun setBehaviour(behavior: SteeringBehavior<Vector2>?){
        this.behavior = behavior
    }

    fun getBehaviour(): SteeringBehavior<Vector2>?{
        return behavior
    }

    fun getBody(): Body{
        return body
    }

    override fun setTagged(tagged: Boolean) {
        this.tagged = tagged
    }

    override fun newLocation(): Location<Vector2> {
        return Box2dLocation()
    }

    override fun getMaxAngularSpeed(): Float {
        return maxAngularSpeed
    }

    override fun getMaxLinearSpeed(): Float {
        return maxLinearSpeed
    }

    override fun getAngularVelocity(): Float {
        return body.angularVelocity
    }

    override fun getMaxAngularAcceleration(): Float {
        return maxAngularAcceleration
    }

    override fun getLinearVelocity(): Vector2 {
        return body.linearVelocity
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        this.maxLinearSpeed = maxLinearSpeed
    }

    override fun getMaxLinearAcceleration(): Float {
        return maxLinearAcceleration
    }

    override fun getPosition(): Vector2 {
        return body.position
    }

    override fun isTagged(): Boolean {
        return tagged
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        this.maxLinearAcceleration = maxLinearAcceleration
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        this.maxAngularSpeed = maxAngularSpeed
    }

    override fun setOrientation(orientation: Float) {
        body.setTransform(position,orientation)
    }

    override fun getZeroLinearSpeedThreshold(): Float {
        return zeroThreshold
    }

    override fun setZeroLinearSpeedThreshold(value: Float) {
        this.zeroThreshold = value
    }

    override fun getOrientation(): Float {
        return body.angle
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        this.maxAngularAcceleration = maxAngularAcceleration
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return SteeringUtil.angleToVector(outVector,angle)
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return SteeringUtil.vectorToAngle(vector)
    }

    override fun getBoundingRadius(): Float {
        return boundingRadius
    }

}