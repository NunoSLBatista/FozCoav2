package com.fozcoa.fozcoa.ui.notifications

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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
import com.fozcoa.fozcoa.adapters.VideosListAdapter
import com.fozcoa.fozcoa.models.Videos
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.videosListView
import org.json.JSONException
import org.json.JSONObject
import java.net.URI

class NotificationsFragment : Fragment(), VideosListAdapter.OnActionListener {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private var videosList = ArrayList<Videos>()
    private val URL = "https://app.ecoa.pt/api/video/read.php"
    internal var request: StringRequest? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)


        return root
    }

    fun logout(){
        val goLogin = Intent(context, SettingsActivity::class.java)
        startActivity(goLogin)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sharedPreferences : SharedPreferences? = null
        sharedPreferences = this.activity!!.getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        if(sharedPreferences!!.getString("photo_url", "") != ""){
            val photo_url = sharedPreferences!!.getString("photo_url", "")
            Glide
                .with(context!!)
                .load(photo_url)
                .centerCrop()
                .placeholder(R.drawable.profile_default)
                .into(profile_image_video);
        }

        profile_image_video.setOnClickListener(View.OnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, profile_image_video)
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

                        videosList.clear()

                        val jsonObject = JSONObject(response)

                        val jsonArray = jsonObject.getJSONArray("records")

                        for (i in 0 until jsonArray.length()) {

                            val videoObj = jsonArray.getJSONObject(i)
                            val nome = videoObj.getString("name")
                            val id = videoObj.getInt("id")
                            val url = videoObj.getString("url")
                            val uri = Uri.parse(url)

                            videosList.add(Videos(id, nome, uri))
                        }

                        val adapterVideo = VideosListAdapter(context!!, videosList, this)
                        view.videosListView.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
                        view.videosListView.adapter = adapterVideo



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

    override fun startActivity(context: Context, videos: Videos) {
        val intentDetailVideo = Intent(context, VideoPlayerActivity::class.java)
        intentDetailVideo.putExtra("video", videos)
        startActivity(intentDetailVideo)
    }

}