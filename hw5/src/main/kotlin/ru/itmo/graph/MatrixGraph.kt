package ru.itmo.graph

import ru.itmo.base.DrawingApi
import ru.itmo.base.Point

class MatrixGraph(
    drawingApi: DrawingApi
) : Graph(drawingApi) {
    override fun createLines(size: Int, points: Map<Int, Point>) {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (graph[i][j] == 1) {
                    drawingApi.drawLine(points[i]!!, points[j]!!)
                }
            }
        }
    }
}
