package com.example.spectroscopy_app

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spectroscopy_app.connection.TurnLightMeasurementOff
import com.example.spectroscopy_app.connection.TurnLightMeasurementOn

class DataActivity : AppCompatActivity() {
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
    }
}