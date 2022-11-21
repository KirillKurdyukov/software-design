package ru.itmo.base

import java.io.BufferedReader

abstract class Graph(
    protected val drawingApi: DrawingApi
) {
    private lateinit var matrix: List<List<Int>>

    abstract fun drawGraph()

    fun readGraph(reader: BufferedReader) {
        matrix = reader
            .lines()
            .map {
                it.split(" ")
                    .map { e -> e.toInt() }
            }
            .toList()
    }
}