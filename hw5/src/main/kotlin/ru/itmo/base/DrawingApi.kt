package ru.itmo.base

import kotlin.math.sign

interface DrawingApi {
    fun drawCircle(p: Point, r: Double = VERTEX_RADIUS)

    fun drawLine(x: Point, y: Point)

    fun drawGraph()
}

class Point(
    _x: Double,
    _y: Double
) {
    val x = _x
        get() = field * WIDTH / 2 + WIDTH / 2 + (-1 * field.sign) * (VERTEX_RADIUS + PADDING)

    val y = _y
        get() = field * HEIGHT / 2 + HEIGHT / 2 + (-1 * field.sign) * (VERTEX_RADIUS + PADDING)
}

const val WIDTH = 1200
const val HEIGHT = 800
const val PADDING = 2
const val VERTEX_RADIUS = 20.0
