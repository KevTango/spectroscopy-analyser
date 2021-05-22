package com.example.spectroscopy_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spectroscopy_app.connection.Constant

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencing
        val btnClickMe = findViewById<Button>(R.id.connect_button)
        val ip = findViewById<EditText>(R.id.ip_address) // IP address

        // set on-click listener
        btnClickMe.setOnClickListener {
            Constant.ipAddress = ip.text.toString() // Converts IP address to string
            Toast.makeText(this@MainActivity, "Connecting to ${Constant.ipAddress}", Toast.LENGTH_SHORT).show() // Shows IP address as toast

        }
    }
}

