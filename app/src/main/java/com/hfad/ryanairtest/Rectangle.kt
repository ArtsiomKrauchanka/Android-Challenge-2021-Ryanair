package com.hfad.ryanairtest

class Rectangle(val id: Int, val shape: String, data: String) {

    lateinit var pointOne: Pair<Int, Int>
    lateinit var pointTwo: Pair<Int, Int>
    var isWrong = false

    init {
        try {
            val stringPoints = data.split(";")

            if (stringPoints.size != 2) {
                isWrong = true
            }

            val stringOneXY = stringPoints[0].split("-")
            val stringTwoXY = stringPoints[1].split("-")

            pointOne = Pair(stringOneXY[0].toInt(), stringOneXY[1].toInt())
            pointTwo = Pair(stringTwoXY[0].toInt(), stringTwoXY[1].toInt())

            checkIfWrong()

        } catch (e: Exception) {
            isWrong = true
        }

    }

    private fun checkIfWrong() {

        //also last comparison here checks if it is a line

        if (pointOne.first < 0
            || pointOne.second < 0
            || pointOne.first > 10000
            || pointOne.second > 10000
            || pointTwo.first < 0
            || pointTwo.second < 0
            || pointTwo.first > 10000
            || pointTwo.second > 10000
            || pointOne.first == pointTwo.first
            || pointOne.second == pointTwo.second
        ) {
            isWrong = true
        }
    }
}