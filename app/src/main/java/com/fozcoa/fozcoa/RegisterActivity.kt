package com.fozcoa.fozcoa

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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {


    private val url = "https://app.ecoa.pt/api/user/create.php"
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
            Log.d("teste", "inside1")
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
        Log.d("teste", "inside")
        if(!checkRegisterInfo(email, name, password)){



        } else {
            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(this)

            try {
                //historyObject.put("id","1");
                postObject.put("email", email)
                postObject.put("password", password)
                postObject.put("name", name)
                postObject.put("photo_url", getStringImage(bitmap!!))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, url, postObject,
                Response.Listener { response ->
                    val responseObject = JSONObject(response.toString())
                    val message = responseObject.getString("message")
                    if(message.contains("created")){
                        Snackbar.make(goBack, "Conta criada com sucesso", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.themeRegister))
                            .setTextColor(resources.getColor(R.color.white))
                            .show()
                        //Toast.makeText(applicationContext, "Conta criada com sucesso.", Toast.LENGTH_LONG).show()
                        val intentLogin= Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intentLogin)

                    } else if (message.contains("email")){
                        Snackbar.make(goBack, "O email já está em uso.", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.themeRegister))
                            .setTextColor(resources.getColor(R.color.white))
                            .show()
                        //Toast.makeText(applicationContext, "O email já está em uso.", Toast.LENGTH_LONG).show()

                    } else {
                        Snackbar.make(goBack, "Tente novamente.", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.themeRegister))
                            .setTextColor(resources.getColor(R.color.white))
                            .show()
                      //  Toast.makeText(applicationContext, "Tente novamente", Toast.LENGTH_LONG).show()
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
            Snackbar.make(goBack, "O formato do email esta errado", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.themeRegister))
                .setTextColor(resources.getColor(R.color.white))
                .show()
            //Toast.makeText(applicationContext, "O formato do email esta errado.", Toast.LENGTH_LONG).show()
            return false
        } else if(password.length < 6){
            Snackbar.make(goBack, "A password tem que ter 6 caractéres", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.themeRegister))
                .setTextColor(resources.getColor(R.color.white))
                .show()
            //Toast.makeText(applicationContext, "A password tem que ter 6 caractéres", Toast.LENGTH_LONG).show()
            return false
        } else if (bitmap == null){
            Snackbar.make(goBack, "A imagem não foi escolhida", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.themeRegister))
                .setTextColor(resources.getColor(R.color.white))
                .show()
            //Toast.makeText(applicationContext, "A imagem não foi escolhida.", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage

    }


    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


}
