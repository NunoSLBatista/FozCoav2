package com.example.fozcoa

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        goBack.setOnClickListener {
            this.finish()
        }


        createAccount.setOnClickListener {
            val intentMain = Intent(applicationContext, MenuActivity::class.java);
            startActivity(intentMain);
        }

    }
}
