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

    // declare a global variable FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    private var locationManager: LocationManager? = null

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

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

    //start location updates
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    Log.d("latitude", location.latitude.toString())
                    Log.d("longitude", location.longitude.toString())
                    val gcd : Geocoder = Geocoder(applicationContext, Locale.getDefault())
                    val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    if(addresses.size > 0){
                        Log.d("Country", addresses.get(0).countryName)
                    }
                }else {
                    Log.d("no", "location")
                }

            }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MenuActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


}
