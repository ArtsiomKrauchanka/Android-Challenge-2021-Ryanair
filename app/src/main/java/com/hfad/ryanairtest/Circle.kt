package com.hfad.ryanairtest

class Circle(val id: Int, val shape: String, data: String) {

    var centerPoint: Pair<Int, Int> = Pair(0, 0)
    var radius = 0
    var isWrong = false


    init {
        val stringPoints = data.split(";")
        if (stringPoints.size != 2) {
            isWrong = true
        } else {
            try {
                val centerStringXY = stringPoints[0].split("-")
                centerPoint = Pair(centerStringXY[0].toInt(), centerStringXY[0].toInt())
                radius = stringPoints[1].toInt()
                checkIfWrong()
            } catch (e: Exception) {
                isWrong = true
            }

        }

    }

    private fun checkIfWrong() {
        if (radius < 0
            || (centerPoint.first - radius) < 0
            || (centerPoint.second - radius) < 0
            || (centerPoint.first + radius) > 10000
            || (centerPoint.second + radius) > 10000
        ) {
            isWrong = true
        }
    }
}