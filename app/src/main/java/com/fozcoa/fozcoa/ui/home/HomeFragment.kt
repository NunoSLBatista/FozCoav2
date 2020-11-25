package com.fozcoa.fozcoa.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.*
import com.fozcoa.fozcoa.adapters.ExperienciaListAdapter
import com.fozcoa.fozcoa.models.Experiencia
import com.fozcoa.fozcoa.models.ImageGallery
import com.fozcoa.fozcoa.models.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.profile_image
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment(), ExperienciaListAdapter.OnActionListener {

    private lateinit var homeViewModel: HomeViewModel

    internal var experienciaListArray = ArrayList<Experiencia>()
    private val URL = "https://app.ecoa.pt/api/experiencia/read.php"
    internal var request: StringRequest? = null
    var current = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    fun logout(){
        val goLogin = Intent(context, SettingsActivity::class.java)
        startActivity(goLogin)
    }

    override fun onDestroy() {
        super.onDestroy()
        print("destroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        print(current)

        if(current < 1){

        this.current = current + 1

        var sharedPreferences : SharedPreferences? = null
        sharedPreferences = this.activity!!.getSharedPreferences("ecoa", Context.MODE_PRIVATE)


        if(sharedPreferences!!.getString("photo_url", "") != ""){
            val photo_url = sharedPreferences!!.getString("photo_url", "")
            Glide
                .with(context!!)
                .load(photo_url)
                .centerCrop()
                .placeholder(R.drawable.profile_default)
                .into(profile_image);
        }

        profile_image.setOnClickListener(View.OnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, profile_image)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.logout ->
                        logout()
                }
                true
            })
            popupMenu.show()
        })


        val requestQueue: RequestQueue

        requestQueue = Volley.newRequestQueue(context)

        request = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->

                if (response.length > 0) {

                    try {

                        Log.d("responseExperiencia", response)

                        experienciaListArray.clear()

                        val jsonObject = JSONObject(response)

                        val jsonArray = jsonObject.getJSONArray("records")

                        for (i in 0 until jsonArray.length()) {

                            val experienciaObj = jsonArray.getJSONObject(i)
                            val galleryArray = experienciaObj.getJSONArray("images")
                            val arrayGallery = ArrayList<ImageGallery>()

                            for(j in 0 until galleryArray.length()){
                                val galleryObj = galleryArray.getJSONObject(j)
                                val id = galleryObj.getInt("id")
                                val url = galleryObj.getString("url")
                                val type = galleryObj.getString("type")
                                val userId = galleryObj.getInt("userId")
                                val name = galleryObj.getString("name")
                                val user_photo = galleryObj.getString("user_photo")
                                val user = User(userId, name, "", "",null, user_photo, 0)
                                arrayGallery.add(ImageGallery(id, url, type, user))
                            }

                            val nome = experienciaObj.getString("name")
                            val description = experienciaObj.getString("description")
                            val id = experienciaObj.getInt("id")
                            val urlImage = experienciaObj.getString("photo_url")
                            val userId = experienciaObj.getInt("userID")

                            experienciaListArray.add(Experiencia(id, nome, urlImage, description, arrayGallery, userId))
                        }

                        if(experienciaListArray != null && experienciaListArray.size > 0){
                            val adapterExperiencia = ExperienciaListAdapter(context!!, experienciaListArray, this)
                            view.experienciaListView.layoutManager =
                                LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
                            view.experienciaListView.adapter = adapterExperiencia
                        }

                        loadingPanelExperiencias.visibility = View.GONE

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }, Response.ErrorListener {
                Log.d("error volley", it.toString())

            }) {
        }

        requestQueue.add<String>(request)

        }
    }

    override fun onResume() {
        super.onResume()
        ((activity as AppCompatActivity).supportActionBar?.hide())
    }

    override fun startActivity(context: Context, experiencia: Experiencia) {
        val intentDetailExperiencia = Intent(context, ExperienciaDetail::class.java)
        intentDetailExperiencia.putExtra("experiencia", experiencia)
        startActivity(intentDetailExperiencia)
    }


}