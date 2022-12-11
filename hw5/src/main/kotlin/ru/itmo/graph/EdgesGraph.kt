package ru.itmo.graph

import ru.itmo.base.DrawingApi
import ru.itmo.base.Point

class EdgesGraph(
    drawingApi: DrawingApi
) : Graph(drawingApi) {
    override fun createLines(size: Int, points: Map<Int, Point>) {
        graph.forEachIndexed { index, ints ->
            ints.forEach {
                drawingApi.drawLine(points[index]!!, points[it - 1]!!)
            }
        }
    }
}
