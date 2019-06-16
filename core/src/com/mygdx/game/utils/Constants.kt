package com.mygdx.game.utils

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.entities.Enemy

const val PPM: Float = 32f

const val STEEL = 0
const val WOOD = 1
const val RUBBER = 2
const val STONE = 3
const val HIGH_FRIC = 4
const val LOW_FRIC = 5

const val MAX_X_VELOCITY = 5
const val MAX_Y_VELOCITY = 6

const val MAX_SPRINT_VELOCITY = 6

const val MAX_ENEMY_X_VELOCITY = 1f

//Constant enemy positions
val enemyPosStageOne: Array<Pair<Float,Float>> = arrayOf(
        Pair(537f,54f),
        Pair(720f,120f),
        Pair(810f,120f),
        Pair(1000f,166f),
        Pair(1030f,325f),
        Pair(830f,405f),
        Pair(694f,405f),
        Pair(276f,600f),
        Pair(713f,600f),
        Pair(1005f,600f),
        Pair(1170f,540f)
)

val enemyPosStageTwo: Array<Pair<Float,Float>> = arrayOf(
        Pair(62f,327f),
        Pair(190f,410f),
        Pair(785f,425f),
        Pair(914f,425f),
        Pair(1000f,425f),
        Pair(1025f,360f),
        Pair(864f,185f),
        Pair(880f,55f),
        Pair(1428f,311f),
        Pair(1425f,570f)
)

val enemyPosStageThree: Array<Pair<Float,Float>> = arrayOf(
        Pair(575f,40f),
        Pair(470f,440f),
        Pair(410f,680f),
        Pair(560f,727f),
        Pair(1242f,455f),
        Pair(1000f,327f),
        Pair(1100f,215f),
        Pair(1481f,215f),
        Pair(1517f,585f)
)

val enemyPosStageFour: Array<Pair<Float,Float>> = arrayOf(
        Pair(497f,727f),
        Pair(493f,551f),
        Pair(759f,551f),
        Pair(907f,760f)
)

val enemyPosStageFive: Array<Pair<Float,Float>> = arrayOf(
        Pair(401f,40f),
        Pair(437f,103f),
        Pair(522f,311f),
        Pair(494f,360f),
        Pair(232f,360f),
        Pair(927f,360f),
        Pair(1160f,615f),
        Pair(1171f,711f)
)

