package com.example.fozcoa

import android.app.PendingIntent.getActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fozcoa.ui.dashboard.DashboardFragment
import com.example.fozcoa.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        if (savedInstanceState == null) {
            if (intent.getIntExtra("fragmentNumber", 0) == 1) {
                val fm = this@MainActivity.getSupportFragmentManager()
                val ft = fm.beginTransaction()
                val fragment = DashboardFragment()

                if (fragment != null) {
                    // Replace current fragment by this new one
                    ft.replace(R.id.nav_host_fragment, fragment)
                    ft.commit()

                }
            } else if (intent.getIntExtra("fragmentNumber", 0) == 2) {



                // Create new fragment and transaction
                val newFragment = NotificationsFragment()
                val transaction = supportFragmentManager.beginTransaction()

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
        navView.setupWithNavController(navController)

    }

}
