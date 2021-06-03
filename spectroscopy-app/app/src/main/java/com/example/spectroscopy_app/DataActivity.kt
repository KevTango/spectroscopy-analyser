package com.example.spectroscopy_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spectroscopy_app.connection.Constant
import com.example.spectroscopy_app.connection.RequestData
import com.example.spectroscopy_app.connection.TurnLightMeasurementOff
import com.example.spectroscopy_app.connection.TurnLightMeasurementOn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class DataActivity : AppCompatActivity() {

    private var TAG = ""
    private var variabled = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                getCurrentData()
                mainHandler.postDelayed(this, 1000)
            }
        })

        val toggle = findViewById<Switch>(R.id.toggle_switch)

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val lightOn = TurnLightMeasurementOn()
                if (lightOn.turnLightMeasurementOn() == 0) {
                    Toast.makeText(
                        this@DataActivity,
                        "Cannot turn on LED",
                        Toast.LENGTH_SHORT
                    ).show() // Error toast
                }
            } else {
                val lightOff = TurnLightMeasurementOff()
                if (lightOff.turnLightMeasurementOff() == 0) {
                    Toast.makeText(
                        this@DataActivity,
                        "Cannot turn off LED",
                        Toast.LENGTH_SHORT
                    ).show() // Error toast
                }
            }
        }
    }

    private fun getCurrentData() {
        val testText = findViewById<TextView>(R.id.testText)
        val api =  Retrofit.Builder()
            .baseUrl("http://${Constant.ipAddress}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RequestData::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getSensorData().awaitResponse()
            if(response.isSuccessful) {
                val data = response.body()!!
                Log.d(TAG, data.datastream.toString())

                withContext(Dispatchers.Main) {
                    //testText.text = data.datastream.toString()
                    variabled += 1
                    testText.text = variabled.toString()
                }
            }
        }
    }
}