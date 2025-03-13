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
        val buttonNumbers: Array<Button> by lazy {
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

        val operatingButtons: Array<Button> by lazy {
            arrayOf(
                findViewById(R.id.id_minus),
                findViewById(R.id.id_plus),
                findViewById(R.id.id_multiply),
                findViewById(R.id.id_divide)
            )
        }
        //Functional buttons variables

        val dot: Button by lazy {
            findViewById(R.id.id_dot)
        }

        val bksp :Button by lazy{
            findViewById(R.id.id_bksp)
        }

        val clear : Button by lazy{
            findViewById(R.id.id_clear)
        }

        val changeSign : Button by lazy{
            findViewById(R.id.id_change_sign)
        }

        val display: TextView by lazy {
            findViewById(R.id.display)
        }

        val equals: TextView by lazy {
            findViewById(R.id.id_equals)
        }

        //Initializing display value
        display.text = "0"

        //Adding listeners to numbers

        buttonNumbers.forEach { button ->
            button.setOnClickListener {
                val number = display.text.toString()
                val newNumber = if (number == "0")
                    button.text
                else number + button.text
                display.text = newNumber
            }
        }

        //Listener for dot

        dot.setOnClickListener {
            val number = display.text.toString()
            val newNumber = number + "."
            display.text = newNumber
        }

        //Listener for backspace
        bksp.setOnClickListener {
            val number = display.text.toString()
            val newNumber = number.substring(0,number.length - 1)
            display.text = newNumber
        }

        //Listener for clear
        clear.setOnClickListener {
            display.text = "0"
        }

        //Listener for change sign
        changeSign.setOnClickListener {
            val number = display.text.toString()
            val newNumber = if(number.first() == '-') number.substring(1,number.length) else "-" + number

            display.text = newNumber
        }

        //Listener for equals



        var first_number = ""
        var operator = ""

        //Listeners for operating buttons
        operatingButtons.forEach{
            button ->
                first_number = display.text.toString()
                operator = button.text.toString()
                display.text = "0"
        }


    }
}