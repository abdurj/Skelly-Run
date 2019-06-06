package com.mygdx.game.Parallax

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.TextureRegion


class ParallaxLayer
/**
 * @param region   the TextureRegion to draw , this can be any width/height
 * @param parallaxRatio   the relative speed of x,y [ParallaxBackground.ParallaxBackground]
 * @param startPosition the init position of x,y
 * @param padding  the padding of the region at x,y
 */
(var region: TextureRegion, var parallaxRatio: Vector2, var startPosition: Vector2, var padding: Vector2) {
    constructor(region: TextureRegion, parallaxRatio: Vector2, padding: Vector2) : this(region, parallaxRatio, Vector2(0f, 0f), padding) {}
}
