package com.fozcoa.fozcoa

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_menu.profile_image
import java.util.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        var sharedPreferences : SharedPreferences? = null
        sharedPreferences = getSharedPreferences("ecoa", Context.MODE_PRIVATE)

        if(sharedPreferences!!.getString("photo_url", "") != ""){
            val photo_url = sharedPreferences!!.getString("photo_url", "")
            Glide
                .with(applicationContext)
                .load(photo_url)
                .centerCrop()
                .placeholder(R.drawable.profile_default)
                .into(profile_image);
        }

        profile_image.setOnClickListener(View.OnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, profile_image)
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

        experiences.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            intentExperiencias.putExtra("fragmentNumber", 0);
            startActivity(intentExperiencias);
        }

        miradouros.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            intentExperiencias.putExtra("fragmentNumber", 1);
            startActivity(intentExperiencias);
        }

        videos.setOnClickListener {
            val intentExperiencias = Intent(applicationContext, MainActivity::class.java);
            intentExperiencias.putExtra("fragmentNumber", 2);
            startActivity(intentExperiencias);
        }

    }

    fun logout(){
        val goLogin = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(goLogin)
    }



}
