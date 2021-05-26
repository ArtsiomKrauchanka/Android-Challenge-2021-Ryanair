package com.hfad.ryanairtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hfad.ryanairtest.db.MyDBManager
import kotlinx.android.synthetic.main.activity_task_three.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt


class TaskThreeActivity : AppCompatActivity() {

    private val myDBManager = MyDBManager(this)
    private lateinit var lineList: MutableList<Line>
    private lateinit var rectangleList: MutableList<Rectangle>
    private lateinit var circleList: MutableList<Circle>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_three)


        GlobalScope.launch(context = Dispatchers.IO) {
            onDBLoadStart()
            try {
                initFun()
                onCalculationStart()
                countMeanLineLength()
                countCrossingLines()
                countMBRectangle()
            } catch (e: Exception) {
                Log.i("Error", e.toString())
            }

        }

    }

    @SuppressLint("SetTextI18n")
    fun initFun() {
        myDBManager.openDB()
        lineList = myDBManager.readDBLines()
        rectangleList = myDBManager.readDBRectangle()
        circleList = myDBManager.readDBCircles()

    }

    private suspend fun countMeanLineLength() {

        var sum = 0.0
        var count = 0

        //sqrt((X2-X1)^2+(Y2-Y1)^2) - distance between two points
        lineList.forEach {
            if (!it.isWrong) {
                var lengthOfOneLine = 0.0
                for (i in 0..(it.points.size - 2)) {
                    lengthOfOneLine += (sqrt(
                        (it.points[i + 1].first - it.points[i].first).toDouble().pow(2)
                                + (it.points[i + 1].second - it.points[i].second).toDouble().pow(2)
                    ))
                }
                sum += (lengthOfOneLine)
                count += 1
            }
        }

        withContext(context = Dispatchers.Main) {
            MeanLengthResult.text = (sum / count).toString()
        }

    }

    private fun isCrossing(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        x3: Int,
        y3: Int,
        x4: Int,
        y4: Int
    ): Boolean {

        //solving system of equations using determinant

        val denominator = (y4 - y3) * (x1 - x2) - (x4 - x3) * (y1 - y2) //determinant
        if (denominator == 0) {
            if ((x1 * y2 - x2 * y1) * (x4 - x3) - (x3 * y4 - x4 * y3) * (x2 - x1) == 0 && (x1 * y2 - x2 * y1) * (y4 - y3) - (x3 * y4 - x4 * y3) * (y2 - y1) == 0) {
                return true
            }
        } else { //Cramer

            val numeratorA = (x4 - x2) * (y4 - y3) - (x4 - x3) * (y4 - y2)
            val numeratorB = (x1 - x2) * (y4 - y2) - (x4 - x2) * (y1 - y2)
            val Ua = numeratorA / denominator
            val Ub = numeratorB / denominator

            if (Ua >= 0 && Ua <= 1 && Ub >= 0 && Ub <= 1) {
                return true
            }
        }
        return false
    }

    private suspend fun countCrossingLines() {
        var crossingCounter = 0
        lineList.forEach {
            if (!it.isWrong) {
                var isCrossingVal = false

                val simpleLines = mutableListOf<Array<Int>>()

                for (i in 0..(it.points.size - 2)) {
                    simpleLines.add(
                        arrayOf(
                            it.points[i].first, it.points[i].second,
                            it.points[i + 1].first, it.points[i + 1].second
                        )
                    )
                }

                for (i in 0 until simpleLines.size - 2) {
                    for (ii in i + 2 until simpleLines.size) {
                        val line1 = simpleLines[i]
                        val line2 = simpleLines[ii]
                        if (isCrossing(
                                line1[0], line1[1], line1[2], line1[3],
                                line2[0], line2[1], line2[2], line2[3]
                            )
                        ) {
                            isCrossingVal = true
                            break;
                        }
                    }
                }

                if (isCrossingVal) {
                    crossingCounter += 1
                }
            }
        }

        withContext(context = Dispatchers.Main) {
            CrossingLinesResult.text = crossingCounter.toString()
        }


    }

    @SuppressLint("SetTextI18n")
    private suspend fun countMBRectangle() {
        var maxX = -1
        var maxY = -1
        var minX = 10000
        var minY = 10000

        lineList.forEach {
            if (!it.isWrong) {
                it.points.forEach { point ->
                    when {
                        point.first > maxX -> maxX = point.first
                        point.first < minX -> minX = point.first
                        point.second > maxY -> maxY = point.second
                        point.second < minY -> minY = point.second
                    }
                }
            }
        }

        circleList.forEach {
            if (!it.isWrong) {
                when {
                    it.centerPoint.first + it.radius > maxX ->
                        maxX = it.centerPoint.first + it.radius
                    it.centerPoint.first - it.radius < minX ->
                        minX = it.centerPoint.first - it.radius
                    it.centerPoint.second + it.radius > maxY ->
                        maxY = it.centerPoint.second + it.radius
                    it.centerPoint.second - it.radius < minY ->
                        minY = it.centerPoint.second - it.radius
                }
            }
        }

        rectangleList.forEach {
            if (!it.isWrong) {
                when {

                    it.pointOne.first > maxX -> maxX = it.pointOne.first
                    it.pointOne.first < minX -> minX = it.pointOne.first
                    it.pointOne.second > maxY -> maxY = it.pointOne.second
                    it.pointOne.second < minY -> minY = it.pointOne.second

                    it.pointTwo.first > maxX -> maxX = it.pointTwo.first
                    it.pointTwo.first < minX -> minX = it.pointTwo.first
                    it.pointTwo.second > maxY -> maxY = it.pointTwo.second
                    it.pointTwo.second < minY -> minY = it.pointTwo.second
                }
            }
        }

        withContext(context = Dispatchers.Main) {
            MBRecResult.text = "($minX, $minY);($maxX, $maxY)"
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun onDBLoadStart() {
        withContext(context = Dispatchers.Main) {
            MeanLengthResult.text = "loading DB.."
            CrossingLinesResult.text = "loading DB.."
            MBRecResult.text = "loading DB.."
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun onCalculationStart() {
        withContext(context = Dispatchers.Main) {
            MeanLengthResult.text = "Calculating..."
            CrossingLinesResult.text = "Calculating..."
            MBRecResult.text = "Calculating..."
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDB()
    }
}