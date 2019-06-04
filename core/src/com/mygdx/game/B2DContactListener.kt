package com.mygdx.game

import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.Fixture
import com.mygdx.game.entities.Bullet
import com.mygdx.game.entities.Enemy
import com.mygdx.game.entities.EnemyBullet
import com.mygdx.game.entities.Player


class B2DContactListener(private val parent: B2DModel) : ContactListener{

    private val player = parent.player

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

        //println("${fA.body.userData} has hit ${fB.body.userData}")

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
            val bullet = if(userDataA is Bullet) userDataA else userDataB as Bullet

            if(bullet.shot) {
                if (userDataA is Enemy || userDataB is Enemy) {
                    val enemy = if (userDataA is Enemy) userDataA else userDataB as Enemy
                    enemy.subtractHealth(bullet.damage)
                    if (enemy.health < 0) {
                        enemy.body.userData = "delete"
                    }
                    println(enemy.health)
                }
                if(userDataA is Player || userDataB is Player){
                    val player = if(userDataA is Player) userDataA else userDataB as Player
                    player.health -= bullet.damage
                    if(player.health < 0){
                        player.playerBody.userData = "playerDelete"
                    }
                }
                bullet.body.userData = "delete"
            }
        }

        if(userDataA is EnemyBullet || userDataB is EnemyBullet){
            val bullet = if(userDataA is EnemyBullet) userDataA else userDataB as EnemyBullet
            if(userDataA is Player || userDataB is Player){
                val player = if(userDataA is Player) userDataA else userDataB as Player
                player.health -= bullet.damage
            }
            //println("${fA.body.userData} A, ${fB.body.userData}")
            bullet.body.userData = "delete"
        }

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }



}