package com.example.test

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SimpleCalc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simple_calc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val sum = 0

        //Fetching buttons
        val buttonNumbers : Array<Button> by lazy {
            arrayOf(
                findViewById(R.id.id_digit_0),
                findViewById(R.id.id_digit_1),
                findViewById(R.id.id_digit_2),
                findViewById(R.id.id_digit_3),
                findViewById(R.id.id_digit_4),
                findViewById(R.id.id_digit_5),
                findViewById(R.id.id_digit_6),
                findViewById(R.id.id_digit_7),
                findViewById(R.id.id_digit_8),
                findViewById(R.id.id_digit_9),
            )
        }

        val operatingButtons : Array<Button> by lazy {
            arrayOf(
                findViewById(R.id.id_minus),
                findViewById(R.id.id_plus),
                findViewById(R.id.id_equals),
                findViewById(R.id.id_multiply),
                findViewById(R.id.id_divide),
                findViewById(R.id.id_bksp),
                findViewById(R.id.id_change_sign),
                findViewById(R.id.id_clear),
                findViewById(R.id.id_dot)
            )
        }


        val display : TextView by lazy {
            findViewById(R.id.display)
        }

        //Adding listeners to numbers

        buttonNumbers.forEach { button -> {
            button.setOnClickListener {
                val number = display.text.toString()
                val newNumber  = if (number == "0")
                    button.text
                else number + button.text
                display.text = newNumber
            }
        } }




    }
}