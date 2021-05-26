package com.hfad.ryanairtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hfad.ryanairtest.db.MyDBManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private val myDBManager = MyDBManager(this)
    var dbStatus = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TaskOneText.setOnClickListener { taskOneOnClickListener() }
        TaskTwoText.setOnClickListener { TaskTwoOnClickListener() }
        TaskThreeText.setOnClickListener { TaskThreeOnClickListener() }

        myDBManager.openDB()
        if (myDBManager.isEmptyDB()) {
            fillDB("Android Challenge 2021.csv")
        } else {
            dbStatus = "full"
        }
    }


    private fun taskOneOnClickListener() {
        if (dbStatus == "empty") {
            Toast.makeText(applicationContext, "Wait for DB filling firstly", Toast.LENGTH_SHORT)
                .show()
        }
        val int = Intent(this, TaskOneActivity::class.java)
        startActivity(int)
    }

    fun TaskTwoOnClickListener() {
        val int = Intent(this, TaskTwoActivity::class.java)
        startActivity(int)
    }

    fun TaskThreeOnClickListener() {
        val int = Intent(this, TaskThreeActivity::class.java)
        startActivity(int)
    }

    @SuppressLint("SetTextI18n")
    fun fillDB(assetName: String) {


        GlobalScope.launch(Dispatchers.IO) {
            try {

                withContext(context = Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Start filling data", Toast.LENGTH_SHORT)
                        .show()

                    TaskOneText.text = "Loading DB..."
                    TaskTwoText.text = "Loading DB..."
                    TaskThreeText.text = "Loading DB..."

                    TaskOneText.isEnabled = false
                    TaskTwoText.isEnabled = false
                    TaskThreeText.isEnabled = false
                }


                val myinput = InputStreamReader(assets.open(assetName))
                val reader = BufferedReader(myinput)

                var line: String?

                var counter = 1 //just for checking

                val problemRecordList = mutableListOf<Int>() // list of incorrect figures

                while (reader.readLine().also { line = it } != null) {
                    val row: List<String> = line!!.split("\"")
                    try {
                        val splittedRow = row[0].split(";")
                        val id = splittedRow[0].toInt()
                        val shape = splittedRow[1]
                        val shapeData: String = if (row.size == 1) {
                            splittedRow[2]
                        } else {
                            row[1]
                        }
                        myDBManager.insertToDB(id, shape, shapeData)
                    } catch (e: Exception) {
                        problemRecordList.add(counter)
                    }
                    if ((counter % 10000) == 0) {
                        withContext(context = Dispatchers.Main) {
                            Log.i("Load", counter.toString())//just for checking
                        }

                    }

                    counter += 1

                }

                withContext(context = Dispatchers.Main) {

                    Log.i("Problem", problemRecordList.toString()) //info about incorrect data

                    dbStatus = "full"
                    TaskOneText.text = "Task 1"
                    TaskTwoText.text = "Task 2"
                    TaskThreeText.text = "Task 3"

                    TaskOneText.isEnabled = true
                    TaskTwoText.isEnabled = true
                    TaskThreeText.isEnabled = true
                }


            } catch (e: Exception) {
                Log.i("Error", e.toString())
            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDB()
    }


}