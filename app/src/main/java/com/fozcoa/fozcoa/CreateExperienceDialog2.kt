package com.fozcoa.fozcoa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fozcoa.fozcoa.models.ImageGallery
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fozcoa.fozcoa.adapters.ScrollGalleryAdapter
import com.fozcoa.fozcoa.models.Experiencia
import com.fozcoa.fozcoa.models.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.upload_experiencia_modal.nextStep
import kotlinx.android.synthetic.main.upload_experiencia_modal2.*
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CreateExperienceDialog2 (var arrayListImages : ArrayList<Bitmap>) : BottomSheetDialogFragment(), ScrollGalleryAdapter.OnActionListener {

    private var fragmentView: View? = null
    var postID = 0
    var experienciaDetail : Experiencia? = null

    private val URL = "https://app.ecoa.pt/api/galleryItem/create_experience.php"
    internal var request: StringRequest? = null
    var sharedPreferences : SharedPreferences? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.upload_experiencia_modal2, container, false)
        return fragmentView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("teste", "state")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingPanelUpload.isVisible = false

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
            loadingPanelUpload.isVisible = true
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
                    if(response.length() > 0){
                        try {
                            val jsonArray = response.getJSONArray("records")

                            for (i in 0 until jsonArray.length()) {
                                val galleryObj = jsonArray.getJSONObject(i)
                                val id = 6
                                val url = galleryObj.getString("url")
                                val type = galleryObj.getString("type")
                                val userId = galleryObj.getInt("userId")
                                val name = ""
                                val user_photo = ""
                                val user = User(userId, name, "", "",null, user_photo, 0)
                                experienciaDetail!!.listImages.add(ImageGallery(id, url, type, user))
                            }
                            loadingPanelUpload.isVisible = false
                            Toast.makeText(context, "Submeteste uma nova experiência", Toast.LENGTH_LONG).show()
                            val intentMenu = Intent(context, ExperienciaDetail::class.java)
                            intentMenu.putExtra("experiencia", experienciaDetail)
                            this.dismiss()
                            startActivity(intentMenu)
                        }catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                },
                Response.ErrorListener { error ->
                    if (error is NetworkError) {
                    } else if (error is ServerError) {
                        Toast.makeText(getContext(),
                            "Oops. sever error!",
                            Toast.LENGTH_LONG).show();
                    } else if (error is AuthFailureError) {
                        Toast.makeText(getContext(),
                            "Oops. auth error!",
                            Toast.LENGTH_LONG).show();
                    } else if (error is ParseError) {
                        Toast.makeText(getContext(),
                            error.toString(),
                            Toast.LENGTH_LONG).show();
                    } else if (error is NoConnectionError) {
                        Toast.makeText(getContext(),
                            "Oops. nocconnection error!",
                            Toast.LENGTH_LONG).show();
                    } else if (error is TimeoutError) {
                        Toast.makeText(getContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                    }
                })


            queue.add(objRequest)

        }

    }



    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val imageBytes = baos.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }

    override fun startActivity(context: Context, bitmap: Bitmap) {

    }


}