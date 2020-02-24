package com.example.fozcoa.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.ExperienciaDetail
import com.example.fozcoa.MiradouroDetail
import com.example.fozcoa.R
import com.example.fozcoa.adapters.ExperienciaListAdapter
import com.example.fozcoa.models.Experiencia
import com.example.fozcoa.models.ImageGallery
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment(), ExperienciaListAdapter.OnActionListener {

    private lateinit var homeViewModel: HomeViewModel

    internal var experienciaListArray = ArrayList<Experiencia>()
    private val URL = "http://app.ecoa.pt/api/experiencia/read.php"
    internal var request: StringRequest? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val requestQueue: RequestQueue

        requestQueue = Volley.newRequestQueue(context)

        request = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->

                if (response.length > 0) {

                    try {

                        Log.d("response", response)

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

                                arrayGallery.add(ImageGallery(id, url, type))
                            }

                            val nome = experienciaObj.getString("name")
                            val description = experienciaObj.getString("description")
                            val id = experienciaObj.getInt("id")
                            val urlImage = experienciaObj.getString("photo_url")
                            val userId = experienciaObj.getInt("userID")

                            experienciaListArray.add(Experiencia(id, nome, urlImage, description, arrayGallery, userId))
                        }

                        val adapterExperiencia = ExperienciaListAdapter(context!!, experienciaListArray, this)
                        view.experienciaListView.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
                        view.experienciaListView.adapter = adapterExperiencia



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