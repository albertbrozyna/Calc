package com.example.test

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.HorizontalScrollView
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

        val display: TextView by lazy {
            findViewById(R.id.display)
        }

        var operator = ""

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
                if(button.text == "0" && number.length > 1 &&!isOperator(number.last())){
                    newNumber += '0'
                }else if(number == "0" && button.text == "0"){
                    newNumber = "0"
                }else if(number == "0"){
                    newNumber = button.text.toString()
                }else{
                    newNumber += button.text
                }

                display.text = newNumber
            }
        }

        //Listener for dot

        dot.setOnClickListener {
            val number = display.text.toString()
            val newNumber = if (number.last() != '.' && !isNumberContainDot(number) && !isOperator(number.last())) number + "." else number
            display.text = newNumber
        }

        //Listener for backspace
        bksp.setOnClickListener {
            val number = display.text.toString()
            var newNumber = number.substring(0, number.length - 1)

            if(number.length == 2 && number.first() == '-'){
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
            var newNumber = "0"

            var operatorIndex = -1
            //Checking if number already contains two digits
            if(containOperatorAndTwoDigits(number)){
                operatorIndex = findOperatorIndex(number)
            }

            //If first char is minuis then we need to add one to index
            //Checking at what number we should change -

            if(operatorIndex == -1){
                //It is first number
                //Checking if number already contains minus
                if(number.first() == '-'){
                    newNumber = number.substring(1,number.length)
                }else{
                    newNumber = '-' + number
                }

            }else{
                //It is second number
                //Checking if number already contains minus

                if(number[operatorIndex + 1] == '-'){
                    //Change for plus
                    newNumber = number.substring(0,operatorIndex + 1) + number.substring(operatorIndex + 2)
                }else{
                    newNumber = number.substring(0,operatorIndex + 1) + '-' + number.substring(operatorIndex + 1)
                }
            }
            if(number == "0"){
                newNumber = "0"
            }

            display.text = newNumber
        }

        //Listener for equals
        equals.setOnClickListener {
        var number = display.text.toString()
        var newNumber = number

        if (containOperatorAndTwoDigits(number)) {
            newNumber = calculateExpression(number)
        }

        display.text = newNumber
    }


        //Listeners for operating buttons
        operatingButtons.forEach{
            button ->
                button.setOnClickListener{
                    var number = display.text.toString()
                    var newNumber = number

                    if(containOperatorAndTwoDigits(number)){
                        newNumber = calculateExpression(number)
                    }

                    operator = button.text.toString()
                    //If the operator is last char then replace operator
                    if(isOperator(newNumber.last())){
                        newNumber = newNumber.dropLast(1) + operator
                    }else{
                        newNumber += operator
                    }

                    display.text = newNumber
                }
        }


    }

    fun findOperatorIndex(s : String) : Int{
        //Founding operator index
        var operatorIndex = 0
        var add = 0
        //If minus is first
        if(s.first() == '-'){
            add = 1
        }

        val s_new =  if(s.first() == '-') s.substring(1,s.length) else s

        var found = false
        for(c in s_new){
            if(isOperator(c)){
                found = true
                break
            }
            operatorIndex++
        }

        if(!found){
            return -1;  //Index not found
        }
        return operatorIndex + add
    }


    fun isNumberContainDot(s :String ):Boolean{

        val operatorIndex = findOperatorIndex(s)

        if(operatorIndex == -1){
            //It is first number
            return s.contains('.')
        }else{
            //It is second number
            val subs = s.substring(operatorIndex,s.length)

            return subs.contains('.')
        }
    }


    fun isOperator(c : Char ) : Boolean{
        return c == '+' || c == '-' || c == '/' || c == '*'
    }

    fun containOperatorAndTwoDigits(s : String) : Boolean{
        //Finding operator index
        val operatorIndex = findOperatorIndex(s)

        //No operator, return false
        if(operatorIndex == -1){
            return false
        }

        //Checking if operator is not last element
        if(!isOperator(s.last() ) && s.last() != '.'){
            return true
        }

        return false
    }

    fun calculateExpression(e : String) : String{
        val operatorIndex = findOperatorIndex(e)

        //Splitting numbers by this index

        val firstNumber = e.substring(0,operatorIndex)
        val operator = e[operatorIndex]
        val secondNumber = e.substring(operatorIndex + 1)

        var firstNumberDb = firstNumber.toDoubleOrNull() ?: return "Error"
        val secondNumberDb = secondNumber.toDoubleOrNull() ?: return "Error"
        //Zero dividing
        if (operator == '/' && secondNumberDb == 0.0) return "Error"

        //Calculating
        when (operator){
            '+' -> firstNumberDb += secondNumberDb
            '-' -> firstNumberDb -= secondNumberDb
            '*' -> firstNumberDb *= secondNumberDb
            '/' -> firstNumberDb /= secondNumberDb
        }

        //Deleting zeros from the end and dot if they are
        var sum_str = firstNumberDb.toString()
        if(sum_str.length > 1 || sum_str.first() != '0'){
            while (sum_str.last() == '0'){
                sum_str = sum_str.substring(0,sum_str.length - 1)
            }
        }

        //Delete the dot
        if(sum_str.last() == '.'){
            sum_str = sum_str.substring(0,sum_str.length - 1)
        }

        if(sum_str == "-0"){
            sum_str = "0"
        }

        return sum_str
    }

}