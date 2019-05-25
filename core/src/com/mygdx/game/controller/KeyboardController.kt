package com.mygdx.game.controller

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Input.Keys.*

class KeyboardController : InputProcessor{
    var left = false
    var right = false
    var down = false
    var up = false

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
            UP -> {
                up = false
                keyProcessed = true
            }
            DOWN -> {
                down = false
                keyProcessed = true
            }
            LEFT -> {
                left = false
                keyProcessed = true
            }
            RIGHT -> {
                right = false
                keyProcessed = true
            }
        }
        return keyProcessed
    }

    override fun keyDown(keycode: Int): Boolean {
        var keyProcessed = false
        when(keycode){
            UP -> {
                up = true
                keyProcessed = true
            }
            DOWN -> {
                down = true
                keyProcessed = true
            }
            LEFT -> {
                left = true
                keyProcessed = true
            }
            RIGHT -> {
                right = true
                keyProcessed = true
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