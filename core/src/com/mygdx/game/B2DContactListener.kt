package com.mygdx.game

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.Fixture
import com.mygdx.game.entities.Bullet


class B2DContactListener(private val parent: B2DModel) : ContactListener{

    val player = parent.player

    override fun endContact(contact: Contact) {
        val fa = contact.fixtureA
        val fb = contact.fixtureB
        //println("${fa.body.userData} has left ${fb.body.userData}")

        if (fa.body.userData == "MainPlat") {
            player.isJumping = true
            player.doubleJump = false
            return
        }
        else if (fb.body.userData == "MainPlat") {
            player.isJumping = true
            player.doubleJump = false
            return
        }
        if(fa.body.userData == "water" && fb.body.userData == "player"){
            player.isSwimming = false
        }
        else if(fb.body.userData == "water" && fa.body.userData == "player"){
            player.isSwimming = false
        }
    }

    override fun beginContact(contact: Contact) {
        //println("contact")
        val fA: Fixture = contact.fixtureA
        val fB: Fixture = contact.fixtureB

        val bodyA = fA.body
        val bodyB = fB.body

        val userDataA = bodyA.userData
        val userDataB = bodyB.userData

        //println("${fixtureA.body.userData} has hit ${fixtureB.body.userData}")

        if(userDataB == "water" && userDataA == "player"){
            player.isSwimming = true
        }
        else if(userDataB == "water" && userDataA == "player"){
            //println("isswimming")
            player.isSwimming = true
        }

        if(fA.body.userData == "MainPlat"){
            player.isJumping = false
            player.doubleJump = false
            return
        }
        else if(fB.body.userData == "MainPlat"){
            player.isJumping = false
            player.doubleJump = false
            return
        }

        //println("${userDataA} USERDATA A, ${userDataB} USERDATA B")
        if(userDataA is Bullet || userDataB is Bullet){
            val bullet = if(userDataA is Bullet) userDataA as Bullet else userDataB as Bullet

            println("bong")
            print(bullet.damage)
        }



    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }



}