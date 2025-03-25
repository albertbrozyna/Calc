package com.example.test

import android.os.Bundle
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SimpleCalc : AppCompatActivity() {

    private val hsv :HorizontalScrollView by lazy { findViewById(R.id.hss1) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simple_calc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var operator: String

        val display: TextView by lazy {
            findViewById(R.id.display)
        }

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

        val bksp: Button by lazy {
            findViewById(R.id.id_bksp)
        }

        val clear: Button by lazy {
            findViewById(R.id.id_clear)
        }

        val changeSign: Button by lazy {
            findViewById(R.id.id_change_sign)
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
                var newNumber = number
                //If 0 we need to watch out
                if (button.text == "0" && number.length > 1 && !isOperator(number.last())) {
                    newNumber += '0'
                } else if (number == "0" && button.text == "0") {
                    newNumber = "0"
                } else if (number == "0") {
                    newNumber = button.text.toString()
                } else {
                    newNumber += button.text
                }

                display.text = newNumber
                updateScreenToRight()
            }
        }

        //Listener for dot

        dot.setOnClickListener {
            val number = display.text.toString()
            val newNumber =
                if (number.last() != '.' && !isNumberContainDot(number) && !isOperator(number.last())) "$number." else number
            display.text = newNumber
            updateScreenToRight()
        }

        //Listener for backspace
        bksp.setOnClickListener {
            val number = display.text.toString()
            var newNumber = number.substring(0, number.length - 1)

            if (number.length == 2 && number.first() == '-') {
                newNumber = "0"

            }

            if (number.length == 1) {
                newNumber = "0"
            }

            display.text = newNumber
        }

        //Listener for clear
        clear.setOnClickListener {
            display.text = "0"
        }

        //Listener for change sign
        changeSign.setOnClickListener {
            val number = display.text.toString()
            var newNumber: String

            var operatorIndex = -1
            //Checking if number already contains two digits
            if (containOperatorAndTwoDigits(number)) {
                operatorIndex = findOperatorIndex(number)
            }

            //If first char is minuis then we need to add one to index
            //Checking at what number we should change -

            if (operatorIndex == -1) {
                //It is first number
                //Checking if number already contains minus
                newNumber = if (number.first() == '-') {
                    number.substring(1, number.length)
                } else {
                    "-$number"
                }

            } else {
                //It is second number
                //Checking if number already contains minus

                newNumber = if (number[operatorIndex + 1] == '-') {
                    //Change for plus
                    number.substring(0, operatorIndex + 1) + number.substring(operatorIndex + 2)
                } else {
                    number.substring(
                        0,
                        operatorIndex + 1
                    ) + '-' + number.substring(operatorIndex + 1)
                }
            }
            if (number == "0") {
                newNumber = "0"
            }

            display.text = newNumber
            updateScreenToRight()
        }

        //Listener for equals
        equals.setOnClickListener {
            val number = display.text.toString()
            var newNumber = number

            if (containOperatorAndTwoDigits(number)) {
                newNumber = calculateExpression(number)
            }

            display.text = newNumber
            updateScreenToLeft()
        }

        //Listeners for operating buttons
        operatingButtons.forEach { button ->
            button.setOnClickListener {
                val number = display.text.toString()
                var newNumber = number

                if (containOperatorAndTwoDigits(number)) {
                    newNumber = calculateExpression(number)
                }

                operator = button.text.toString()
                //If the operator is last char then replace operator
                if (isOperator(newNumber.last())) {
                    newNumber = newNumber.dropLast(1) + operator
                } else {
                    newNumber += operator
                }

                display.text = newNumber
                updateScreenToRight()
            }
        }
    }

    private fun findOperatorIndex(s: String): Int {
        //Founding operator index
        var operatorIndex = 0
        var add = 0
        //If minus is first
        if (s.first() == '-') {
            add = 1
        }

        val sNew = if (s.first() == '-') s.substring(1, s.length) else s

        var found = false
        for (c in sNew) {
            if (isOperator(c)) {
                found = true
                break
            }
            operatorIndex++
        }

        if (!found) {
            return -1 //Index not found
        }
        return operatorIndex + add
    }


    private fun isNumberContainDot(s: String): Boolean {

        val operatorIndex = findOperatorIndex(s)

        if (operatorIndex == -1) {
            //It is first number
            return s.contains('.')
        } else {
            //It is second number
            val subs = s.substring(operatorIndex, s.length)

            return subs.contains('.')
        }
    }


    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '/' || c == '*'
    }

    private fun containOperatorAndTwoDigits(s: String): Boolean {
        //Finding operator index
        val operatorIndex = findOperatorIndex(s)

        //No operator, return false
        if (operatorIndex == -1) {
            return false
        }

        //Checking if operator is not last element
        if (!isOperator(s.last()) && s.last() != '.') {
            return true
        }

        return false
    }


    private fun calculateExpression(e: String): String {
        val operatorIndex = findOperatorIndex(e)

        //Splitting numbers by this index

        val firstNumber = e.substring(0, operatorIndex)
        val operator = e[operatorIndex]
        val secondNumber = e.substring(operatorIndex + 1)

        var firstNumberDb = firstNumber.toDoubleOrNull();

        if(firstNumberDb == null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            return "0"
        }
        val secondNumberDb = secondNumber.toDoubleOrNull()

        if(secondNumberDb == null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            return "0"
        }

        //Zero dividing
        if (operator == '/' && secondNumberDb == 0.0){
            Toast.makeText(this, "Error - division by zero", Toast.LENGTH_SHORT).show()
            return "0"
        }

        //Calculating
        when (operator) {
            '+' -> firstNumberDb += secondNumberDb
            '-' -> firstNumberDb -= secondNumberDb
            '*' -> firstNumberDb *= secondNumberDb
            '/' -> firstNumberDb /= secondNumberDb
        }

        //Deleting zeros from the end and dot if they are
        var sumStr = firstNumberDb.toString()
        if (sumStr.length > 1 || sumStr.first() != '0') {
            while (sumStr.last() == '0') {
                sumStr = sumStr.substring(0, sumStr.length - 1)
            }
        }

        //Delete the dot
        sumStr = deleteZerosAndTrim(sumStr)

        if(sumStr == "Infinity"){
            Toast.makeText(this, "Infinity", Toast.LENGTH_SHORT).show()
        }

        return sumStr
    }


    private fun deleteZerosAndTrim(s: String): String {
        var str = s
        if (str.length > 1 || str.first() != '0') {
            while (str.last() == '0') {
                str = str.substring(0, str.length - 1)
            }
        }

        //Delete the dot
        if (str.last() == '.') {
            str = str.substring(0, str.length - 1)
        }

        if (str == "-0") {
            str = "0"
        }

        //Cutting to 5 decimal places

        val dotIndex = str.indexOf('.')
        if (dotIndex != -1 && !str.contains('E')) {
            if (str.length > dotIndex + 5) {
                str = str.substring(0, dotIndex + 5)
            }
        }

        return str
    }

    private fun updateScreenToRight() {
        hsv.post {
            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }
    }

    private fun updateScreenToLeft() {
        hsv.post {
            hsv.fullScroll(HorizontalScrollView.FOCUS_LEFT)
        }
    }

}