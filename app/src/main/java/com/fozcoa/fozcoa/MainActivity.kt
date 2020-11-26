package com.fozcoa.fozcoa

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.fozcoa.fozcoa.ui.dashboard.DashboardFragment
import com.fozcoa.fozcoa.ui.home.HomeFragment
import com.fozcoa.fozcoa.ui.notifications.NotificationsFragment
import com.google.android.gms.maps.model.Dash


class MainActivity : AppCompatActivity() {

    companion object {
        var navView : BottomNavigationView? = null
        var runApiExperience = true
        fun apiChange(running: Boolean) {
            runApiExperience = running
            if (!runApiExperience) {
                navView!!.menu.findItem(R.id.navigation_home).isEnabled = false;
            } else {
                navView!!.menu.findItem(R.id.navigation_home).isEnabled = true;
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        if (savedInstanceState == null) {
            if (intent.getIntExtra("fragmentNumber", 0) == 1) {
                val fm = this@MainActivity.getSupportFragmentManager()
                val ft = fm.beginTransaction()
                val fragment = DashboardFragment()
                navView!!.selectedItemId = R.id.navigation_dashboard

                if (fragment != null) {
                    // Replace current fragment by this new one
                    ft.replace(R.id.nav_host_fragment, fragment)
                    ft.commit()
                    navView!!.selectedItemId = R.id.navigation_dashboard
                }
            } else if (intent.getIntExtra("fragmentNumber", 0) == 2) {
                // Create new fragment and transaction
                val newFragment = NotificationsFragment()
                val transaction = supportFragmentManager.beginTransaction()
                navView!!.selectedItemId = R.id.navigation_notifications

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment)
                transaction.addToBackStack("navigation_home")

                // Commit the transaction
                transaction.commit()

            }
        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navView!!.setupWithNavController(navController)
        navView!!.menu.findItem(R.id.navigation_home).isEnabled = false;

    }

}
