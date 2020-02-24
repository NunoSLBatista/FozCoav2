package com.example.fozcoa.ui.notifications

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
import com.example.fozcoa.MiradouroDetail
import com.example.fozcoa.R
import com.example.fozcoa.adapters.MiradouroListAdapter
import com.example.fozcoa.adapters.VideosListAdapter
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.Videos
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.miradourosListView
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.videosListView
import org.json.JSONException
import org.json.JSONObject

class NotificationsFragment : Fragment(), VideosListAdapter.OnActionListener {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private var videosList = ArrayList<Videos>()
    private val URL = "http://app.ecoa.pt/api/video/read.php"
    internal var request: StringRequest? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)


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

                        videosList.clear()

                        val jsonObject = JSONObject(response)

                        val jsonArray = jsonObject.getJSONArray("records")

                        for (i in 0 until jsonArray.length()) {

                            val videoObj = jsonArray.getJSONObject(i)
                            val nome = videoObj.getString("name")
                            val id = videoObj.getInt("id")
                            val url = videoObj.getString("url")

                            videosList.add(Videos(id, nome, url))
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

    override fun startActivity(context: Context, video: Videos) {
        /*val intentDetailMiradouro = Intent(context, MiradouroDetail::class.java)
        intentDetailMiradouro.putExtra("video", miradouro)
        startActivity(intentDetailMiradouro)*/
    }

}