package ru.itmo.base

interface DrawingApi {
    fun drawCircle(p: Point, r: Double)

    fun drawLine(x: Point, y: Point)

    fun drawGraph()
}

data class Point(
    val x: Double,
    val y: Double,
)

const val WIDTH = 600
const val HEIGHT = 800
const val VERTEX_RADIUS = 20