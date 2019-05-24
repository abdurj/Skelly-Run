package com.mygdx.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import sun.management.snmp.util.JvmContextFactory.getUserData
import com.badlogic.gdx.physics.box2d.Fixture



class B2DContactListener(private val parent: B2DModel) : ContactListener{
    override fun endContact(contact: Contact) {
        val fa = contact.fixtureA
        val fb = contact.fixtureB
        println("${fa.body.type} has left ${fb.body.userData}")

        if (fa.body.userData == "MainPlat") {
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = false
            return
        }
        else if (fb.body.userData == "MainPlat") {
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = false
            return
        }
        if (fa.body.userData == "MovingPlat"){
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = false
            return
        }
        else if (fb.body.userData == "MovingPlat"){
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = false
            return
        }

    }

    override fun beginContact(contact: Contact) {
        println("contact")
        val fixtureA: Fixture = contact.fixtureA
        val fixtureB: Fixture = contact.fixtureB
        println("${fixtureA.body.type} has hit ${fixtureB.body.userData}")

        if(fixtureA.body.userData == "MainPlat"){
            parent.isJumping = false
            parent.doubleJump = false
            return
        }
        else if(fixtureB.body.userData == "MainPlat"){
            parent.isJumping = false
            parent.doubleJump = false
            return
        }
        if (fixtureA.body.userData == "MovingPlat"){
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = true
            return
        }
        else if (fixtureB.body.userData == "MovingPlat"){
            parent.isJumping = true
            parent.doubleJump = false
            parent.onMPlat = true
            return
        }

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

    private fun applyUpwardsForce(staticFixture: Fixture,otherFixture: Fixture){
        println("Applying Force")
        otherFixture.body.applyForceToCenter(Vector2(0f,300f),true)
    }


}