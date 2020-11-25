package com.fozcoa.fozcoa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fozcoa.fozcoa.adapters.UploadListAdapter
import com.fozcoa.fozcoa.models.ImageGallery
import android.graphics.Bitmap
import android.util.Log
import android.content.SharedPreferences
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.models.Experiencia
import com.fozcoa.fozcoa.models.Miradouro
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_photo.*
import org.json.JSONObject


class DetailPhotoDialog () : DialogFragment(), UploadListAdapter.OnActionListener {

    private var fragmentView: View? = null
    var galleryItem: ImageGallery? = null
    private val URL = "https://app.ecoa.pt/api/user/read.php"
    private val URL2 = "https://app.ecoa.pt/api/experiencia/delete.php"
    private val URL3 = "https://app.ecoa.pt/api/user/block.php"
    internal var request: StringRequest? = null
    internal var request2: StringRequest? = null
    var userId : Int = 0
    var experienciaDetail : Experiencia? = null
    var miradouroDetail : Miradouro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       fragmentView = inflater.inflate(R.layout.detail_photo, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.with(context).load(galleryItem!!.url).into(detailPhoto)

        var sharedPreferences : SharedPreferences? = null
        sharedPreferences = this.activity!!.getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        userId = sharedPreferences.getInt("userId", 0)
        val admin = sharedPreferences.getInt("admin", 0)

        if (userId == galleryItem!!.user.id || admin > 0){
            deletePost.visibility = View.VISIBLE
        }

        deletePost.setOnClickListener(View.OnClickListener {

            if(admin > 0){
                val popupMenu: PopupMenu = PopupMenu(context, deletePost)
                popupMenu.menuInflater.inflate(R.menu.admin,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.deletePost -> deleteExperiencia()
                        R.id.blockUser -> blockUser()

                    }
                    true
                })
                popupMenu.show()
            }else {
                val popupMenu: PopupMenu = PopupMenu(context, deletePost)
                popupMenu.menuInflater.inflate(R.menu.more,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.deletePost -> deleteExperiencia()

                    }
                    true
                })
                popupMenu.show()
            }
        })

        userName.setText(galleryItem!!.user.name)
        Glide
            .with(context!!)
            .load(galleryItem!!.user.url)
            .centerCrop()
            .placeholder(R.drawable.bg_miradouro_1)
            .into(profile_image2);


    }

    override fun startActivity(context: Context, bitmap: Bitmap) {

    }

    fun deleteExperiencia(){
        Snackbar.make(deletePost, "Experiência apagada", Snackbar.LENGTH_SHORT)
            .show()
        val postObject = JSONObject()
        postObject.put("id", galleryItem!!.id)

        val queue = Volley.newRequestQueue(context)


        val objRequest = JsonObjectRequest(
            Request.Method.POST, URL2, postObject,
            Response.Listener { response ->
                (activity as ExperienciaDetail).deletePost2(galleryItem!!)
                dismiss()
            },
            Response.ErrorListener { error ->
                Log.e("OnError", error.toString())
            })

        queue.add(objRequest)
    }

    fun blockUser(){
        Snackbar.make(deletePost, "Utilizador Bloqueado", Snackbar.LENGTH_SHORT)
            .show()
        val postObject = JSONObject()
        postObject.put("id", galleryItem!!.user.id)

        val queue = Volley.newRequestQueue(context)

        val objRequest = JsonObjectRequest(
            Request.Method.POST, URL3, postObject,
            Response.Listener { response ->
               // (activity as ExperienciaDetail).deletePost2(galleryItem!!)
                dismiss()
            },
            Response.ErrorListener { error ->
                Log.e("OnError", error.toString())
            })

        queue.add(objRequest)
    }


}