package com.example.fozcoa.ui.dashboard

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fozcoa.MiradouroDetail
import com.example.fozcoa.R
import com.example.fozcoa.adapters.MiradouroListAdapter
import com.example.fozcoa.models.Miradouro
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.miradourosListView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DashboardFragment : Fragment(), MiradouroListAdapter.OnActionListener {

    private lateinit var dashboardViewModel: DashboardViewModel

    internal var miradouroListArray = ArrayList<Miradouro>()
    private val URL = "http://app.ecoa.pt/api/miradouro/read.php"
    internal var request: StringRequest? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Fragment Miradouro", "teste")

        val requestQueue: RequestQueue

        requestQueue = Volley.newRequestQueue(context)

        request = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->

                Log.d("Response Miradouro", response)

                if (response.length > 0) {

                    try {

                        miradouroListArray.clear()

                        val jsonObject = JSONObject(response)

                        val jsonArray = jsonObject.getJSONArray("records")

                        for (i in 0 until jsonArray.length()) {

                            val hotelObj = jsonArray.getJSONObject(i)
                            val nome = hotelObj.getString("name")
                            val description = hotelObj.getString("description")
                            val id = hotelObj.getInt("id")
                            val urlImage = hotelObj.getString("photo_url")

                            miradouroListArray.add(Miradouro(id, nome, urlImage, description, ArrayList()))
                        }

                        val adapterMiradouro = MiradouroListAdapter(context!!, miradouroListArray, this)
                        view.miradourosListView.adapter = adapterMiradouro



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


    override fun startActivity(context: Context, miradouro: Miradouro) {
        val intentDetailMiradouro = Intent(context, MiradouroDetail::class.java)
        // intentDetailMiradouro.putExtra("miradouro", miradouro)
        startActivity(intentDetailMiradouro)
    }

}