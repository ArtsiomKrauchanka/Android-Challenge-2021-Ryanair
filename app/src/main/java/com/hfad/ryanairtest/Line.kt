package com.hfad.ryanairtest

class Line(val id: Int, val shape: String, data: String) {

    var points: MutableList<Pair<Int, Int>> = mutableListOf()
    var isWrong = false

    init {
        try {
            val stringPoints = data.split(";")
            stringPoints.forEach {
                val stringXY = it.split("-")
                val x = stringXY[0].toInt()
                val y = stringXY[1].toInt()
                points.add(Pair(x, y))
            }
            checkIfWrong()
        } catch (e: Exception) {
            isWrong = true
        }

    }

    private fun checkIfWrong() {
        points.forEach {
            if (it.first < 0
                || it.second < 0
                || it.first > 10000
                || it.second > 10000
            ) {
                isWrong = true
            }
        }
    }
}