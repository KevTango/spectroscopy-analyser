package com.example.spectroscopy_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spectroscopy_app.connection.ConnectToIPAddress
import com.example.spectroscopy_app.connection.Constant


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencing
        val buttonConnect = findViewById<Button>(R.id.connect_button)
        val ip = findViewById<EditText>(R.id.ip_address) // IP address

        // Set on-click listener
        buttonConnect.setOnClickListener {
            val checkIP = ConnectToIPAddress()

            Constant.ipAddress = ip.text.toString() // Converts IP address to string

            when {
                Constant.ipAddress == "" -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Empty string detected, please re-enter IP address",
                        Toast.LENGTH_SHORT
                    ).show() // Error toast for empty string
                }
                checkIP.connectToIPAddress() == 1 -> {
                    // Goes to next screen
                    val intent = Intent(this, DataActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Cannot connect to ${Constant.ipAddress}, please check IP address",
                        Toast.LENGTH_SHORT
                    ).show() // Error toast
                }
            }
        }
    }
}
