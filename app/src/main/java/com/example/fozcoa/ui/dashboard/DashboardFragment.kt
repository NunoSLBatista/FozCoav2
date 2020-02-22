package com.example.fozcoa.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fozcoa.MiradouroDetail
import com.example.fozcoa.R
import com.example.fozcoa.adapters.MiradouroListAdapter
import com.example.fozcoa.models.Miradouro
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.miradourosListView

class DashboardFragment : Fragment(), MiradouroListAdapter.OnActionListener {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var miradouroList = ArrayList<Miradouro>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val miradouroListAdapter: MiradouroListAdapter = MiradouroListAdapter(context!!, miradouroList, this)
        root.miradourosListView.adapter = miradouroListAdapter

        return root
    }


    override fun startActivity(context: Context, miradouro: Miradouro) {
        val intentDetailMiradouro = Intent(context, MiradouroDetail::class.java)
        // intentDetailMiradouro.putExtra("miradouro", miradouro)
        startActivity(intentDetailMiradouro)
    }

}