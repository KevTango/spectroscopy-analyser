package com.example.spectroscopy_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spectroscopy_app.connection.Constant
import com.example.spectroscopy_app.connection.RequestData
import com.example.spectroscopy_app.connection.TurnLightMeasurementOff
import com.example.spectroscopy_app.connection.TurnLightMeasurementOn
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Runnable
import java.util.*

// TODO: Make separate classes for the functions
@Suppress("DEPRECATION")
class DataActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val delay =
        1000L // Delay timer of 1s (due to time needed to parse JSON which can crash app)
    private val dataSetLineWidth = 2f // Adjusts the line width of graph
    private val textSize = 12f // Adjusts the text size of graph
    private var tag = ""

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val toggle = findViewById<Switch>(R.id.toggle_switch)

        // Checks if the toggled LED switch has be changed
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

        // Initialise handler to repeat code
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            getCurrentData()
        }
        getCurrentData()
    }

    // Get data from JSON using retrofit and coroutines
    private fun getCurrentData() {
        val api = Retrofit.Builder()
            .baseUrl("http://${Constant.ipAddress}")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RequestData::class.java)

        // Run coroutine on i/o outside of main thread
        // TODO: Improve coroutine by making it non-blocking to instantly toggle LED
        GlobalScope.launch(Dispatchers.Default) {
            val response = api.getSensorData().awaitResponse()
            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d(tag, data.datastream.toString()) // Sending log output

                // Run coroutine on main thread
                withContext(Dispatchers.Main) {
                    Constant.dataArray = data.datastream
                    plotGraph()
                    handler.postDelayed(runnable, delay) // Handler to run code repeatedly
                }
            }
        }
    }

    // Plot graph on screen
    private fun plotGraph() {
        val lineChart = findViewById<LineChart>(R.id.lineChartView)
        val sensorData: MutableList<Entry> = ArrayList() // Adding data points

        sensorData.add(Entry(410f, Constant.dataArray[0].toFloat()))
        sensorData.add(Entry(435f, Constant.dataArray[1].toFloat()))
        sensorData.add(Entry(460f, Constant.dataArray[2].toFloat()))
        sensorData.add(Entry(485f, Constant.dataArray[3].toFloat()))
        sensorData.add(Entry(510f, Constant.dataArray[4].toFloat()))
        sensorData.add(Entry(535f, Constant.dataArray[5].toFloat()))
        sensorData.add(Entry(560f, Constant.dataArray[6].toFloat()))
        sensorData.add(Entry(585f, Constant.dataArray[7].toFloat()))
        sensorData.add(Entry(610f, Constant.dataArray[8].toFloat()))
        sensorData.add(Entry(645f, Constant.dataArray[9].toFloat()))
        sensorData.add(Entry(680f, Constant.dataArray[10].toFloat()))
        sensorData.add(Entry(705f, Constant.dataArray[11].toFloat()))
        sensorData.add(Entry(730f, Constant.dataArray[12].toFloat()))
        sensorData.add(Entry(760f, Constant.dataArray[13].toFloat()))
        sensorData.add(Entry(810f, Constant.dataArray[14].toFloat()))
        sensorData.add(Entry(860f, Constant.dataArray[15].toFloat()))
        sensorData.add(Entry(900f, Constant.dataArray[16].toFloat()))
        sensorData.add(Entry(940f, Constant.dataArray[17].toFloat()))

        val dataSet = LineDataSet(sensorData, "Spectroscopy Sensor Data")
        val data = LineData(dataSet)

        // Create MarkerView
        val markerView = DataMarkerView(applicationContext, R.layout.marker_view)
        lineChart.markerView = markerView
        lineChart.setTouchEnabled(true)

        // Adjusting the line chart options
        lineChart.legend.isEnabled = false // Remove legend
        lineChart.description.text = "Frequency (nm)" // Adds 'x-axis description'
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // Set x-axis label to the bottom
        lineChart.axisRight.isEnabled = false // Removes right y-axis label
        dataSet.color = R.color.black // Sets line to be black
        dataSet.lineWidth = dataSetLineWidth // Adjusts line width
        dataSet.valueTextSize = textSize // Adjusts text size

        // Show graph
        lineChart.data = data
        lineChart.invalidate()
    }
}