package com.example.customview



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Mini Paint shows how to create a custom view and draw on its canvas.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mydraw:MyspidometrView
    private lateinit var current:EditText
    private lateinit var bos:TextView
    private lateinit var secbar:SeekBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mydraw=findViewById(R.id.speedometer)
        current=findViewById(R.id.currentSpeet)
        bos=findViewById(R.id.click)
        secbar=findViewById(R.id.seekbar)
        secbar.max=mydraw.getMaxSpeed()

        secbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mydraw.setCurrentSpeed(progress)
                current.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        bos.setOnClickListener {
            if (current.text.toString()!="") {
                val a = current.text.toString().toInt()
                mydraw.setCurrentSpeed(a)
                secbar.progress=a
            }
        }

//
//        secbar.max = 180
//        secbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                mydraw.setCurrentSpeed(progress)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//
//            }
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
//            }
//
//        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_maxspeet, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.speed180) {
            mydraw.setMaxSpeed(180)
            secbar.max=180
            Toast.makeText(this, "Max speed 180", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.speed220) {
            mydraw.setMaxSpeed(220)
            secbar.max=220
            Toast.makeText(this, "Max speed 220", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.speed280) {
            mydraw.setMaxSpeed(280)
            secbar.max=280
            Toast.makeText(this, "Max speed 280", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.speed360) {
            mydraw.setMaxSpeed(360)
            secbar.max=360
            Toast.makeText(this, "Max speed 360", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}