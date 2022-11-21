package ru.itmo.draw

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.stage.Stage
import ru.itmo.base.DrawingApi
import ru.itmo.base.HEIGHT
import ru.itmo.base.Point
import ru.itmo.base.WIDTH

class JavaFXApi : DrawingApi {

    override fun drawCircle(p: Point, r: Double) {
        shapes.add(Circle(p.x, p.y, r))
    }

    override fun drawLine(x: Point, y: Point) {
        shapes.add(Line(x.x, x.y, y.x, y.y))
    }

    override fun drawGraph() {
        Application.launch(Companion::class.java)
    }

    private companion object : Application() {
        val shapes: MutableList<Shape> = mutableListOf()

        override fun start(primaryStage: Stage) {
            primaryStage.title = "JavaFX graph visualization"

            val root = Group()
            shapes.forEach(root.children::add)

            primaryStage.scene = Scene(root, WIDTH.toDouble(), HEIGHT.toDouble())
        }
    }
}