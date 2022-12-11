package ru.itmo.graph

import ru.itmo.base.DrawingApi
import ru.itmo.base.Point
import java.io.BufferedReader
import kotlin.math.cos
import kotlin.math.sin

abstract class Graph(
    protected val drawingApi: DrawingApi
) {
    protected lateinit var graph: List<List<Int>>

    fun drawGraph() {
        val size = graph.size
        val step = 2 * Math.PI / size

        val points = (0 until size)
            .associateWith {
                Point(
                    cos(step * it),
                    sin(step * it)
                ).also { p ->
                    drawingApi.drawCircle(p)
                }
            }

        createLines(size, points)

        drawingApi.drawGraph()
    }

    fun readGraph(reader: BufferedReader) {
        graph = reader
            .lines()
            .map {
                it.split(" ")
                    .map { e -> e.toInt() }
            }
            .toList()
    }

    protected abstract fun createLines(size: Int, points: Map<Int, Point>)
}
