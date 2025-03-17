package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.menu1)
        val button1 = findViewById<Button>(R.id.menu2)
        val button2 = findViewById<Button>(R.id.menu3)
        val button3 = findViewById<Button>(R.id.menu4)
        button.setOnClickListener{
            val intent = Intent(this,SimpleCalc::class.java)
            startActivity(intent)
        }
        button1.setOnClickListener{
            val intent = Intent(this,AdvancedCalc::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener{
           val intent = Intent(this,About::class.java)
           startActivity(intent)
        }
        button3.setOnClickListener{
            finishAffinity()
        }

    }
}