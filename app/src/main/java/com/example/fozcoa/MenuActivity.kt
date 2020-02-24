package com.example.fozcoa

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        experiences.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            startActivity(intentExperiencias);
        }

        miradouros.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            startActivity(intentExperiencias);
        }

        videos.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            startActivity(intentExperiencias);
        }

    }
}
