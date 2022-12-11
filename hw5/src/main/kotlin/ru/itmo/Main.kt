package ru.itmo

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.stage.Stage
import ru.itmo.draw.AwtApi
import ru.itmo.draw.JavaFXApi
import ru.itmo.graph.EdgesGraph
import ru.itmo.graph.MatrixGraph
import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val resource = ClassLoader.getSystemResource("graph_edges.txt")

    val awtApi = AwtApi()
    val graph = EdgesGraph(awtApi)

    graph.readGraph(
        BufferedReader(
            InputStreamReader(resource.openStream())
        )
    )

    graph.drawGraph()
}
