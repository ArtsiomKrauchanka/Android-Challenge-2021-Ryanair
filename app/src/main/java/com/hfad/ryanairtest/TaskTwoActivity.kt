package com.hfad.ryanairtest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hfad.ryanairtest.db.MyDBManager
import kotlinx.android.synthetic.main.activity_task_one.*
import kotlinx.android.synthetic.main.activity_task_two.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.*

class TaskTwoActivity : AppCompatActivity() {

    private val myDBManager = MyDBManager(this)
    private lateinit var lineList: MutableList<Line>
    private lateinit var rectangleList: MutableList<Rectangle>
    private lateinit var circleList: MutableList<Circle>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_two)

        GlobalScope.launch(context = Dispatchers.IO) {
            onDBLoadStart()
            try {
                initFun()
                onCalculationStart()
                countLength()
                countSurfaceArea()
                countFigures()
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

    @SuppressLint("SetTextI18n")
    private suspend fun onDBLoadStart() {
        withContext(context = Dispatchers.Main) {
            LengthResult.text = "loading DB.."
            SurfaceAreaResult.text = "loading DB.."
            LineCountResult.text = "loading DB.."
            CiercleCountResult.text = "loading DB.."
            RectangleCountResult.text = "loading DB.."
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun onCalculationStart() {
        withContext(context = Dispatchers.Main) {
            LengthResult.text = "Calculating.."
            SurfaceAreaResult.text = "Calculating.."
            LineCountResult.text = "Calculating.."
            CiercleCountResult.text = "Calculating.."
            RectangleCountResult.text = "Calculating.."
        }
    }

    private suspend fun countLength() {

        var length = 0.0

        //length of circle 2PI*R
        val twoPI = 2 * PI // don't need do this hundred times
        circleList.forEach {
            if (!it.isWrong) {
                length += twoPI * it.radius
            }

        }

        //length of line is sum of length between all points
        //sqrt((X2-X1)^2+(Y2-Y1)^2)  --- distance
        lineList.forEach {
            if (!it.isWrong) {
                for (i in 0..(it.points.size - 2)) {
                    length += sqrt(
                        (it.points[i + 1].first - it.points[i].first).toDouble().pow(2)
                                + (it.points[i + 1].second - it.points[i].second).toDouble().pow(2)
                    )
                }
            }
        }

        //length of rectangle is ((|X2-X1|) + (|Y2-Y1|))*2
        rectangleList.forEach {
            if (!it.isWrong) {
                length += (abs(it.pointTwo.first - it.pointOne.first) +
                        abs(it.pointTwo.second - it.pointOne.second)) * 2
            }
        }

        withContext(context = Dispatchers.Main) {
            LengthResult.text = length.toString()
        }
    }

    private suspend fun countSurfaceArea() {

        var area = 0.0

        //area of circle PI*R^2
        circleList.forEach {
            if (!it.isWrong) {
                area += PI * (it.radius.toDouble()).pow(2)
            }

        }


        //area of rectangle is ((|X2-X1|) * (|Y2-Y1|))
        rectangleList.forEach {
            if (!it.isWrong) {
                area += (abs(it.pointTwo.first - it.pointOne.first) *
                        abs(it.pointTwo.second - it.pointOne.second))
            }
        }

        withContext(context = Dispatchers.Main) {
            SurfaceAreaResult.text = area.toString()
        }

    }

    private suspend fun countFigures() {

        withContext(context = Dispatchers.Main) {
            LineCountResult.text = lineList.filterNot { line -> line.isWrong }.size.toString()
            CiercleCountResult.text = circleList.filterNot { circle -> circle.isWrong }.size.toString()
            RectangleCountResult.text =
                rectangleList.filterNot { rectangle -> rectangle.isWrong }.size.toString()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDB()
    }


}