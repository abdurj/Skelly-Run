package com.mygdx.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.Fixture
import com.mygdx.game.entities.Bullet
import com.mygdx.game.entities.Enemy
import com.mygdx.game.entities.EnemyBullet
import com.mygdx.game.entities.Player
import com.mygdx.game.utils.PPM


class B2DContactListener(private val parent: B2DModel) : ContactListener{

    private val player = parent.player

    override fun endContact(contact: Contact) {
        val fa = contact.fixtureA
        val fb = contact.fixtureB
        //println("${fa.body.userData} has left ${fb.body.userData}")

        //If you leave the top of a platform you're jumping
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

        //If you hit a portal and you cleared all the enemies, erase the level and initialize the next level on the next iteration of the world
        if((userDataA == "portal1" || userDataB == "portal1")){
            if(userDataA is Player || userDataB is Player) {
                if (parent.enemies.size == 0) {
                    parent.clearLevel = true
                    parent.currentLevel = Level.Level2
                } else {
                    parent.clearLevel = false
                    parent.player.resetPlayer = true
                }
                return
            }
            else if(userDataA is Bullet || userDataB is Bullet){
                parent.clearLevel=true
                parent.currentLevel=Level.Level2
            }
        }
        if((userDataA == "portal2" || userDataB == "portal2")){
            if(userDataA is Player || userDataB is Player) {
                if (parent.enemies.size == 0) {
                    parent.clearLevel = true
                    parent.currentLevel = Level.Level3
                } else {
                    parent.clearLevel = false
                    parent.player.resetPlayer = true
                }
                return
            }
            else if(userDataA is Bullet || userDataB is Bullet){
                parent.clearLevel=true
                parent.currentLevel=Level.Level3
            }
        }
        if((userDataA == "portal3" || userDataB == "portal3")){
            if(userDataA is Player || userDataB is Player) {
                if (parent.enemies.size == 0) {
                    parent.clearLevel = true
                    parent.currentLevel = Level.Level4
                } else {
                    parent.clearLevel = false
                    parent.player.resetPlayer = true
                }
                return
            }
            else if(userDataA is Bullet || userDataB is Bullet){
                parent.clearLevel=true
                parent.currentLevel=Level.Level4
            }
        }
        if((userDataA == "portal4" || userDataB == "portal4")){
            if(userDataA is Player || userDataB is Player) {
                if (parent.enemies.size == 0) {
                    parent.clearLevel = true
                    parent.currentLevel = Level.Level5
                } else {
                    parent.clearLevel = false
                    parent.player.resetPlayer = true
                }
                return
            }
            else if(userDataA is Bullet || userDataB is Bullet){
                parent.clearLevel=true
                parent.currentLevel=Level.Level5
            }
        }
        if((userDataA == "portal5" || userDataB == "portal5")){
            if(userDataA is Player || userDataB is Player) {
                if (parent.enemies.size == 0) {
                    parent.clearLevel = true
                    parent.currentLevel = Level.Level1
                    parent.winGame = true
                } else {
                    parent.clearLevel = false
                    parent.player.resetPlayer = true
                }
                return
            }
            else if(userDataA is Bullet || userDataB is Bullet){
                parent.clearLevel=true
                parent.currentLevel=Level.Level1
                parent.winGame = true
            }
        }

        //If you're standing at the top of a platform you arent jumping
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

        //Check for collisions between player bullet and enemy
        if(userDataA is Bullet || userDataB is Bullet){
            val bullet = if(userDataA is Bullet) userDataA else userDataB as Bullet

            if(bullet.shot) {
                if (userDataA is Enemy || userDataB is Enemy) {
                    val enemy = if (userDataA is Enemy) userDataA else userDataB as Enemy
                    //subtract
                    enemy.subtractHealth(bullet.damage)
                    if (enemy.health < 0) {
                        enemy.body.userData = "delete"
                    }
                }
                //Check which userdata is the player
                if(userDataA is Player || userDataB is Player){
                    val player = if(userDataA is Player) userDataA else userDataB as Player
                    //Subtract the damage from the health
                    player.health -= bullet.damage
                    if(player.health < 0){
                        player.playerBody.userData = "playerDelete"
                    }
                }
                //set the bullet to be deleted later
                bullet.body.userData = "delete"
                bullet.dispose()
                player.bullets.remove(bullet)
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
            bullet.parent.bullets.remove(bullet)
        }

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }



}