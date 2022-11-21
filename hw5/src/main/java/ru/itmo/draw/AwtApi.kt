package ru.itmo.draw

import ru.itmo.base.DrawingApi
import ru.itmo.base.HEIGHT
import ru.itmo.base.Point
import ru.itmo.base.WIDTH
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

class AwtApi : DrawingApi {

    override fun drawCircle(p: Point, r: Double) {
        circles.add(Ellipse2D.Double(p.x - r, p.y -r, 2 * r, 2 * r))
    }

    override fun drawLine(x: Point, y: Point) {
        lines.add(Line2D.Double(x.x, x.y, y.x, y.y))
    }

    override fun drawGraph() {
        addWindowListener(
            object : WindowAdapter() {
                override fun windowClosed(e: WindowEvent?) {
                    exitProcess(0)
                }
            }
        )

        setSize(WIDTH, HEIGHT)
        isVisible = true
    }

    private companion object : Frame("JavaAWT Graph visualization") {
        val circles = mutableListOf<Ellipse2D>()
        val lines = mutableListOf<Line2D>()

        override fun paint(g: Graphics?) {
            val g2D = g as Graphics2D

            circles.forEach(g2D::fill)
            lines.forEach(g2D::draw)
        }
    }
}