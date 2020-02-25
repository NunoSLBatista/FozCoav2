package com.example.fozcoa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.adapters.MiradouroListAdapter
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.VolleyError
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest



class LoginActivity : AppCompatActivity() {

    private val url = "http://app.ecoa.pt/api/user/login.php"
    internal var request: StringRequest? = null

    var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        if(sharedPreferences!!.getString("name", "") != ""){

            val goMenu = Intent(applicationContext, MenuActivity::class.java)
            startActivity(goMenu)

        }

        buttonRegister.setOnClickListener {
            val intentCreate = Intent(applicationContext, RegisterActivity::class.java);
            startActivity(intentCreate);
        }

        buttonLogin.setOnClickListener {
            loginRequest(emailTextField.text.toString(), passwordTextField.text.toString())
        }

    }

    fun loginRequest(email : String, password: String){

        if(!checkLoginInfo(email, password)){
            Toast.makeText(applicationContext, "Dados incorretos.", Toast.LENGTH_LONG).show()
        } else {
            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(this)

            try {
                //historyObject.put("id","1");
                postObject.put("email", email)
                postObject.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(Request.Method.POST, url, postObject,
                Response.Listener { response ->
                    Log.e("LoginActivity", "OnResponse: $response")

                    val jsonObject = JSONObject(response.toString())

                    val userId = jsonObject.getInt("id")
                    val name = jsonObject.getString("name")
                    val userEmail = jsonObject.getString("email")

                    sharedPreferences!!.edit().putInt("userId", userId).apply()
                    sharedPreferences!!.edit().putString("name", name).apply()
                    sharedPreferences!!.edit().putString("email", userEmail).apply()

                    val intentMenu = Intent(applicationContext, MenuActivity::class.java)
                    startActivity(intentMenu)

                  //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)
        }

    }

    fun checkLoginInfo(email : String, password: String) : Boolean{

        if(email.isEmailValid() && password.length > 5){
            return true
        }

        return false
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


}
