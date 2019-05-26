package com.mygdx.game.controller

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Input.Keys.*

class KeyboardController : InputProcessor{
    var left = false
    var right = false
    var down = false
    var up = false
    var space = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        var keyProcessed = false
        when(keycode){
            W -> {
                up = false
                keyProcessed = true
            }
            S -> {
                down = false
                keyProcessed = true
            }
            A -> {
                left = false
                keyProcessed = true
            }
            D -> {
                right = false
                keyProcessed = true
            }
            SPACE -> {
                space = false
                keyProcessed = true
            }
        }
        return keyProcessed
    }

    override fun keyDown(keycode: Int): Boolean {
        var keyProcessed = false
        when(keycode){
            W -> {
                up = true
                keyProcessed = true
            }
            S -> {
                down = true
                keyProcessed = true
            }
            A -> {
                left = true
                keyProcessed = true
            }
            D -> {
                right = true
                keyProcessed = true
            }
            SPACE -> {
                keyProcessed = true
                space = true
            }
        }
        return keyProcessed
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

}