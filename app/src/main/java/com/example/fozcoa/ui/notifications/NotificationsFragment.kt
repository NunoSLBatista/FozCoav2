package com.example.fozcoa.ui.notifications

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
import com.example.fozcoa.adapters.VideosListAdapter
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.Videos
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment : Fragment(), VideosListAdapter.OnActionListener {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private var videosList = ArrayList<Videos>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val videosListAdapter: VideosListAdapter = VideosListAdapter(context!!, videosList, this)
        root.videosListView.adapter = videosListAdapter

        return root
    }

    override fun startActivity(context: Context, video: Videos) {
        val intentDetailMiradouro = Intent(context, MiradouroDetail::class.java)
        // intentDetailMiradouro.putExtra("miradouro", miradouro)
        startActivity(intentDetailMiradouro)
    }

}