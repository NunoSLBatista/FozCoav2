package com.example.fozcoa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.models.User
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private val url = "http://app.ecoa.pt/api/user/create.php"
    internal var request: StringRequest? = null
    var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        goBack.setOnClickListener {
            this.finish()
        }

        createAccount.setOnClickListener {

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            registerUser(email, name, password)
        }

    }

    fun registerUser(email: String, name: String, password: String){

        if(!checkRegisterInfo(email, name, password)){

            Toast.makeText(applicationContext, "Dados incorretos.", Toast.LENGTH_LONG).show()

        } else {
            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(this)

            try {
                //historyObject.put("id","1");
                postObject.put("email", email)
                postObject.put("password", password)
                postObject.put("name", name)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, url, postObject,
                Response.Listener { response ->

                    val intentLogin= Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intentLogin)

                    //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)
        }

    }

    fun checkRegisterInfo(email : String, name: String, password: String) : Boolean{

        if(email.isEmailValid() && password.length > 5 && name.length > 5){
            return true
        }

        return false
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


}
