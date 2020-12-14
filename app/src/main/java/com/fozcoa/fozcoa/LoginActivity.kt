package com.fozcoa.fozcoa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity() {

    private val url = "https://app.ecoa.pt/api/user/login.php"
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

        forgotPassword.setOnClickListener(View.OnClickListener {
            val intentRecover = Intent(applicationContext, ForgotPassword::class.java)
            startActivity(intentRecover)
        })

    }

    fun loginRequest(email : String, password: String){

        val checkLogin = checkLoginInfo(email, password)

        if(checkLogin != "" ){
            Snackbar.make(buttonLogin, checkLogin, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.white))
                .setTextColor(resources.getColor(R.color.black))
                .show()
          //  Toast.makeText(applicationContext, checkLogin, Toast.LENGTH_LONG).show()
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

                    if(response.has("message")){
                        Snackbar.make(buttonLogin, "Dados incorretos", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.white))
                            .setTextColor(resources.getColor(R.color.black))
                            .show()
                        //Toast.makeText(applicationContext, "Dados incorretos.", Toast.LENGTH_LONG).show()
                    } else {
                        val jsonObject = JSONObject(response.toString())

                        val userId = jsonObject.getInt("id")
                        val name = jsonObject.getString("name")
                        val userEmail = jsonObject.getString("email")
                        val photo_url = jsonObject.getString("photo_url")
                        val blocked = jsonObject.getInt("blocked")
                        val admin = jsonObject.getInt("admin")

                        sharedPreferences!!.edit().putInt("userId", userId).apply()
                        sharedPreferences!!.edit().putString("name", name).apply()
                        sharedPreferences!!.edit().putString("age", "").apply()
                        sharedPreferences!!.edit().putInt("admin", admin).apply()
                        sharedPreferences!!.edit().putString("email", userEmail).apply()
                        sharedPreferences!!.edit().putString("photo_url", photo_url).apply()
                        sharedPreferences!!.edit().putInt("blocked", blocked).apply()

                        val intentMenu = Intent(applicationContext, MenuActivity::class.java)
                        startActivity(intentMenu)

                    }
                  //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)
        }

    }

    fun checkLoginInfo(email : String, password: String) : String{

        if(!email.isEmailValid()){
            return "Email incorreto"
        }else if(password.length < 6) {
            return "Password tem que ter mais que 6 caracteres"
        }

        return ""
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


}
