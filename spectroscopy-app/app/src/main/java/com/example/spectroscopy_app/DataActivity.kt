package com.example.spectroscopy_app

import android.annotation.SuppressLint
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
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Runnable
import java.util.*

class DataActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val delay = 2000L
    private var TAG = ""

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

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

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            getCurrentData()
        }
        getCurrentData()
    }

    private fun getCurrentData() {
        val testTextd = findViewById<TextView>(R.id.testText)
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
                    Constant.dataArray = data.datastream
                    testTextd.text = Constant.dataArray[10]
                }
            }
        }
        handler.postDelayed(runnable,delay)
    }
}