package com.example.fozcoa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fozcoa.adapters.GalleryListAdapter
import com.example.fozcoa.adapters.UploadListAdapter
import com.example.fozcoa.models.ImageGallery
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_experiencia_modal.*
import android.content.Intent
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.util.Base64
import android.util.Log
import androidx.core.app.NotificationCompat.getExtras
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream
import android.provider.MediaStore
import android.R.attr.data
import java.io.IOException
import android.R.attr.data
import androidx.core.app.NotificationCompat.getExtras
import android.graphics.BitmapFactory
import android.R.attr.data
import android.R.attr.data
import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.adapters.ScrollGalleryAdapter
import kotlinx.android.synthetic.main.upload_experiencia_modal.nextStep
import kotlinx.android.synthetic.main.upload_experiencia_modal2.*
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CreateExperienceDialog2 (var arrayListImages : ArrayList<Bitmap>) : BottomSheetDialogFragment(), ScrollGalleryAdapter.OnActionListener {

    private var fragmentView: View? = null
    var postID = 0

    private val URL = "http://app.ecoa.pt/api/galleryItem/create_experience.php"
    internal var request: StringRequest? = null
    var sharedPreferences : SharedPreferences? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.upload_experiencia_modal2, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences= activity!!.getSharedPreferences("ecoa", Context.MODE_PRIVATE)
        val itemAdapter = ScrollGalleryAdapter(context!!, this)
        picker.setSlideOnFling(true);
        picker.adapter = itemAdapter
        itemAdapter.setList(arrayListImages)
        picker.setOffscreenItems(10)
        picker.scrollToPosition(1)
        picker.setItemTransformer(  
            ScaleTransformer.Builder()
                .setMinScale(0.6f)
                .build()
        )


        nextStep.setOnClickListener {

            val postObject = JSONObject()
            val queue = Volley.newRequestQueue(context)

            try {

                postObject.put("postId", postID)
                postObject.put("typePost", "experiencia")
                postObject.put("userId", sharedPreferences!!.getInt("userId", 0))

                val imageJson = JSONArray()

                for(i in 0 until arrayListImages.size){
                    imageJson.put(getStringImage(arrayListImages.get(i)))
                }

                postObject.put("imagesArray", imageJson)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val objRequest = JsonObjectRequest(
                Request.Method.POST, URL, postObject,
                Response.Listener { response ->
                    Log.e("LoginActivity", "OnResponse: $response")

                   // val jsonObject = JSONObject(response.toString())

                    val intentMenu = Intent(context, ExperienciaDetail::class.java)
                    intentMenu.putExtra("postID", postID)
                    startActivity(intentMenu)

                    //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    Log.e("OnError", error.toString())
                })

            queue.add(objRequest)

        }


    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage

    }


    override fun startActivity(context: Context, bitmap: Bitmap) {

    }


}