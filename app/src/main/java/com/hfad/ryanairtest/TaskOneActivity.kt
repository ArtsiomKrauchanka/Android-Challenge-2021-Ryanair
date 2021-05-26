package com.hfad.ryanairtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hfad.ryanairtest.db.MyDBManager
import kotlinx.android.synthetic.main.activity_task_one.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskOneActivity : AppCompatActivity() {

    private val myDBManager = MyDBManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_one)

        showWrongIDs()

    }

    @SuppressLint("SetTextI18n")
    fun showWrongIDs() {

        val wrongIDList = mutableListOf<Int>()


        GlobalScope.launch(Dispatchers.IO) {
            try {

                onDBLoadStart()

                myDBManager.openDB()
                val lineList = myDBManager.readDBLines()
                val rectangleList = myDBManager.readDBRectangle()
                val circleList = myDBManager.readDBCircles()

                onCalculatingStart()

                lineList.forEach {
                    if (it.isWrong) {
                        wrongIDList.add(it.id)
                    }
                }
                rectangleList.forEach {
                    if (it.isWrong) {
                        wrongIDList.add(it.id)
                    }
                }
                circleList.forEach {
                    if (it.isWrong) {
                        wrongIDList.add(it.id)
                    }
                }

            } catch (e: Exception) {
                Log.i("Error", e.toString())
            }


            displayResults(wrongIDList)
        }


    }


    private suspend fun displayResults(wrongIDList: MutableList<Int>) {
        wrongIDList.sort() //for better representation
        withContext(context = Dispatchers.Main) {
            Log.i("Result", wrongIDList.toString())
            IDText.text = wrongIDList.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun onDBLoadStart() {
        withContext(context = Dispatchers.Main) {
            IDText.text = "Loading DB..."
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun onCalculatingStart() {
        withContext(context = Dispatchers.Main) {
            IDText.text = "Calculating..."
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDB()
    }


}