package com.fozcoa.fozcoa.ui.dashboard

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
import com.fozcoa.fozcoa.adapters.MiradouroListAdapter
import com.fozcoa.fozcoa.models.ImageGallery
import com.fozcoa.fozcoa.models.Miradouro
import com.fozcoa.fozcoa.models.User
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.miradourosListView
import org.json.JSONException
import org.json.JSONObject

class DashboardFragment : Fragment(), MiradouroListAdapter.OnActionListener {

    private lateinit var dashboardViewModel: DashboardViewModel

    internal var miradouroListArray = ArrayList<Miradouro>()
    private val URL = "https://app.ecoa.pt/api/miradouro/read.php"
    internal var request: StringRequest? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        return root
    }


    fun logout(){
        val goLogin = Intent(context, SettingsActivity::class.java)
        startActivity(goLogin)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(MainActivity.runApiMiradouro) {

            MainActivity.apiChangeMiradouro(false)
            var sharedPreferences: SharedPreferences? = null
            sharedPreferences = this.activity!!.getSharedPreferences("ecoa", Context.MODE_PRIVATE)

            if (sharedPreferences!!.getString("photo_url", "") != "") {
                val photo_url = sharedPreferences!!.getString("photo_url", "")
                Glide
                    .with(context!!)
                    .load(photo_url)
                    .centerCrop()
                    .placeholder(R.drawable.profile_default)
                    .into(profile_image);
                //  Picasso.with(context).load(photo_url).into(profile_image)
            }

            profile_image.setOnClickListener(View.OnClickListener {
                val popupMenu: PopupMenu = PopupMenu(context, profile_image)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
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

                            miradouroListArray.clear()

                            val jsonObject = JSONObject(response)

                            val jsonArray = jsonObject.getJSONArray("records")

                            for (i in 0 until jsonArray.length()) {

                                val miradouroObj = jsonArray.getJSONObject(i)
                                val galleryArray = miradouroObj.getJSONArray("images")

                                val arrayGallery = ArrayList<ImageGallery>()

                                for (j in 0 until galleryArray.length()) {
                                    val galleryObj = galleryArray.getJSONObject(j)
                                    val id = galleryObj.getInt("id")
                                    val url = galleryObj.getString("url")
                                    val type = galleryObj.getString("type")
                                    val userId = 0
                                    val name = ""
                                    val user_photo = ""
                                    val user = User(userId, name, "", "", null, user_photo, 0)
                                    arrayGallery.add(ImageGallery(id, url, type, user))
                                }

                                val nome = miradouroObj.getString("name")
                                val description = miradouroObj.getString("description")
                                val id = miradouroObj.getInt("id")
                                val urlImage = miradouroObj.getString("photo_url")

                                miradouroListArray.add(
                                    Miradouro(
                                        id,
                                        nome,
                                        urlImage,
                                        description,
                                        arrayGallery
                                    )
                                )
                            }

                            if (miradouroListArray.count() > 0) {
                                val adapterMiradouro =
                                    MiradouroListAdapter(context!!, miradouroListArray, this)
                                view.miradourosListView.layoutManager =
                                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                                view.miradourosListView.adapter = adapterMiradouro

                                loadingPanelMiradouros.visibility = View.GONE
                                MainActivity.apiChangeMiradouro(true)
                            }

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


    override fun startActivity(context: Context, miradouro: Miradouro) {
        val intentDetailMiradouro = Intent(context, MiradouroDetail::class.java)
        intentDetailMiradouro.putExtra("miradouro", miradouro)
        startActivity(intentDetailMiradouro)
    }

}