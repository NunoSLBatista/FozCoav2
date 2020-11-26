package com.fozcoa.fozcoa

import android.app.Activity
import android.app.DatePickerDialog
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
import android.widget.DatePicker
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.goBack
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class ProfileActivity : AppCompatActivity() {


    private val url = "https://app.ecoa.pt/api/user/edit.php"
    internal var request: StringRequest? = null
    var sharedPreferences : SharedPreferences? = null
    val PICK_IMAGE = 1
    var bitmap : Bitmap? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        val url = sharedPreferences!!.getString("photo_url", "")
        val name = sharedPreferences!!.getString("name", "")
        val age = sharedPreferences!!.getString("age", "--/--/----")

        Glide
            .with(applicationContext)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.bg_miradouro_1)
            .into(profile_image_edit);

        nameEditTextEdit.setText(name)
        ageEditText.setText(age.toString())

        goBack.setOnClickListener {
            this.finish()
        }

        profile_image_edit.setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, PICK_IMAGE)
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        ageEditText.setOnFocusChangeListener { view, b ->  run{
                if(b) {
                    DatePickerDialog(
                        this@ProfileActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }


        editAccount.setOnClickListener {
            val name = nameEditTextEdit.text.toString()
            val age = ageEditText.text.toString()
            editUser(name, age)
        }

    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        ageEditText!!.setText(sdf.format(cal.getTime()))
        window.decorView.clearFocus();
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
            profile_image_edit.setImageBitmap(bitmap)

        }
    }

    fun editUser(name: String, age : String){

            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(this)
            sharedPreferences!!.edit().putString("age", age).apply()

            try {
                postObject.put("userId", sharedPreferences!!.getInt("userId", 0))
                postObject.put("name", name)
                postObject.put("age", age)
                if(bitmap != null){
                    postObject.put("photo_url", getStringImage(bitmap!!))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, url, postObject,
                Response.Listener { response ->
                    val responseObject = JSONObject(response.toString())
                    val message = responseObject.getString("message")
                    if(message != "wrong"){
                        Log.d("url", message)
                        Snackbar.make(goBack, "Conta editada com sucesso", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.themeRegister))
                            .setTextColor(resources.getColor(R.color.white))
                            .show()
                        if(bitmap != null){
                            sharedPreferences!!.edit().putString("photo_url", message).apply()
                        }
                        sharedPreferences!!.edit().putString("name", name).apply()
                        //val intentLogin= Intent(applicationContext, MainActivity::class.java)
                        //startActivity(intentLogin)

                    } else {
                        Snackbar.make(goBack, "Tente novamente.", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.themeRegister))
                            .setTextColor(resources.getColor(R.color.white))
                            .show()
                    }
                    
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)

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
