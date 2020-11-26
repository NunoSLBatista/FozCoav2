package com.fozcoa.fozcoa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fozcoa.fozcoa.adapters.ScrollGalleryAdapter
import com.fozcoa.fozcoa.models.Experiencia
import com.fozcoa.fozcoa.models.ImageGallery
import com.fozcoa.fozcoa.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.vr.cardboard.ThreadUtils
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.upload_experiencia_modal.*
import kotlinx.android.synthetic.main.upload_experiencia_modal.nextStep
import kotlinx.android.synthetic.main.upload_experiencia_modal2.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File


class CreateExperienceDialog2 (var arrayListImages : ArrayList<Bitmap>, var videoList : ArrayList<String>, var listener : OnActionListener) : BottomSheetDialogFragment(), ScrollGalleryAdapter.OnActionListener {

    private var fragmentView: View? = null
    var postID = 0
    var experienciaDetail : Experiencia? = null

    private val URL = "https://app.ecoa.pt/api/galleryItem/create_experience.php"
    private val URL_VIDEO = "https://app.ecoa.pt/api/upload/upload_video.php"
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
        itemAdapter.setList(arrayListImages, videoList)
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
            var videoNames = ArrayList<String>()
            loadingPanelUpload.isVisible = true
            Thread(Runnable {

                for(i in 0 until videoList.size) {
                    val responseObject = JSONObject(UploadUtility().uploadFile(File(videoList.get(i))));
                    val message = responseObject.getString("message");
                    if(!message.equals("error")) {
                        videoNames.add(message);
                    }
                }

                try {

                    postObject.put("postId", postID)
                    postObject.put("typePost", "experiencia")
                    postObject.put("userId", sharedPreferences!!.getInt("userId", 0))

                    val imageJson = JSONArray()
                    val videoJson = JSONArray()

                    for(i in 0 until arrayListImages.size){
                        imageJson.put(getStringImage(arrayListImages.get(i)))
                    }

                    for(i in 0 until videoNames.size){
                        videoJson.put(videoNames.get(i))
                    }


                    postObject.put("imagesArray", imageJson)
                    postObject.put("videosArray", videoJson)

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
                                ThreadUtils.runOnUiThread(Thread(Runnable {
                                    loadingPanelUpload.isVisible = false

                                    //Toast.makeText(context, "Submeteste uma nova experiência", Toast.LENGTH_LONG).show()
                                    this.dismiss()
                                    this.listener.startActivity(context!!, experienciaDetail!!)
                                }))
                            }catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }

                    },
                    Response.ErrorListener { error ->
                        ThreadUtils.runOnUiThread(Thread(Runnable {
                        if (error is NetworkError) {
                            } else if (error is ServerError) {
                                Snackbar.make(parentView2, "Oops. sever error!", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()

                            } else if (error is AuthFailureError) {
                                Snackbar.make(parentView2, "Oops. auth error!", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()
                            } else if (error is ParseError) {
                                Snackbar.make(parentView2, error.toString(), Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()
                            } else if (error is NoConnectionError) {
                                Snackbar.make(parentView2, "Oops. nocconnection error!", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()

                            } else if (error is TimeoutError) {
                                Snackbar.make(parentView2, "Oops. Timeout error!", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()
                            }
                        }))
                    })
                queue.add(objRequest)

            }).start()


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

    interface OnActionListener {
        fun startActivity(context : Context, experienciaItem : Experiencia)
    }
}