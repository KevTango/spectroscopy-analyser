package com.example.spectroscopy_app.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.example.spectroscopy_app.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

@SuppressLint("ViewConstructor")
class DataMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    // Adjusts MarkerView if it is out of the screen
    override fun draw(canvas: Canvas, posx: Float, posy: Float) {
        // Adding offset
        var posx = posx
        var posy = posy
        posx += getXOffset()
        posy += getYOffset()

        // 'Fixing' the MarkerView on screen
        if (posx < 75) posx = 75f
        if (posx > 725) posx = 725f

        // Translate to the correct position and draw
        canvas.translate(posx, posy)
        draw(canvas)
        canvas.translate(-posx, -posy)
    }

    // Callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        val count: TextView = findViewById(R.id.tvCount)
        val frequency: TextView = findViewById(R.id.tvFrequency)

        count.text = "Count: " + e.y
        frequency.text = "Frequency: " + e.x + "nm"
        super.refreshContent(e, highlight)
    }

    private fun getXOffset(): Int {
        // Centres the marker-view horizontally
        return -(width / 2)
    }

    private fun getYOffset(): Int {
        // Causes the marker-view to be above the selected value
        return -height
    }
}