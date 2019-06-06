package com.mygdx.game.Parallax

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2


class ParallaxBackground
/**
 * @param layers  The  background layers
 * @param width   The screenWith
 * @param height The screenHeight
 * @param speed A Vector2 attribute to point out the x and y speed
 */
(private val layers: Array<ParallaxLayer>, width: Float, height: Float, speed: Vector2) {
    private val camera: OrthographicCamera
    private val batch: SpriteBatch
    val speed = Vector2()

    init {
        this.speed.set(speed)
        camera = OrthographicCamera(width, height)
        batch = SpriteBatch()
    }

    fun render(delta: Float) {
        this.camera.position.add(speed.x * delta, speed.y * delta, 0f)
        for (layer in layers) {
            batch.projectionMatrix = camera.projection
            batch.begin()
            var currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.regionWidth + layer.padding.x)

            if (speed.x < 0) currentX += -(layer.region.regionWidth + layer.padding.x)
            do {
                var currentY = -camera.position.y * layer.parallaxRatio.y % (layer.region.regionHeight + layer.padding.y)
                if (speed.y < 0) currentY += -(layer.region.regionHeight + layer.padding.y)
                do {
                    batch.draw(layer.region,
                            -this.camera.viewportWidth / 2 + currentX + layer.startPosition.x,
                            -this.camera.viewportHeight / 2 + currentY + layer.startPosition.y)
                    currentY += layer.region.regionHeight + layer.padding.y
                } while (currentY < camera.viewportHeight)
                currentX += layer.region.regionWidth + layer.padding.x
            } while (currentX < camera.viewportWidth)
            batch.end()
        }
    }
}