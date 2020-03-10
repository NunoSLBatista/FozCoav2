package com.example.fozcoa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.adapters.UploadListAdapter
import com.example.fozcoa.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.upload_experiencia_modal.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {

    private val url = "http://app.ecoa.pt/api/user/create.php"
    internal var request: StringRequest? = null
    var sharedPreferences : SharedPreferences? = null
    val PICK_IMAGE = 1
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        goBack.setOnClickListener {
            this.finish()
        }

        profile_image.setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, PICK_IMAGE)
        }

        createAccount.setOnClickListener {

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            registerUser(email, name, password)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == 1) {
            val returnUri = data!!.getData()
            val bitmapImage =
                MediaStore.Images.Media.getBitmap(this.contentResolver, returnUri)

            // Do something with the bitmap
            this.bitmap = bitmapImage
            profile_image.setImageBitmap(bitmap)

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
                postObject.put("image", getStringImage(bitmap!!))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, url, postObject,
                Response.Listener { response ->


                    if(response.has("completed")){

                        val intentLogin= Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intentLogin)

                    } else if (response.has("email")){

                        Toast.makeText(applicationContext, "Email already in use.", Toast.LENGTH_LONG).show()

                    }
                    
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)
        }

    }

    fun checkRegisterInfo(email : String, name: String, password: String) : Boolean{

        if(!email.isEmailValid()){
            Toast.makeText(applicationContext, "Email format is wrong.", Toast.LENGTH_LONG).show()
            return false
        } else if(password.length < 6){
            Toast.makeText(applicationContext, "Password must have at least 6 characters", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage

    }


    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


}
