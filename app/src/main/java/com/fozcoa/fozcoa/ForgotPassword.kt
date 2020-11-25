package com.fozcoa.fozcoa

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.buttonLogin
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {

    var sharedPreferences : SharedPreferences? = null
    private val url = "https://app.ecoa.pt/api/user/change_pass.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        buttonRecover.setOnClickListener(View.OnClickListener {
            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(applicationContext)

            try {
                val email = emailTextField.text.toString()
                Log.d("email", email)
                val userId = sharedPreferences!!.getInt("userId", 0)
                postObject.put("userId", userId)
                postObject.put("email", email)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, url, postObject,
                Response.Listener { response ->

                    if (response.has("message")) {
                        Toast.makeText(applicationContext, "Email de recuperação enviado.", Toast.LENGTH_LONG).show()
                        this.finish()
                    } else {
                          Toast.makeText(this, "Ação impossível verifique a sua ligação", Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)
            true
        })

        buttonLogin.setOnClickListener(View.OnClickListener {
            this.finish()
        })

    }
}
