package com.fozcoa.fozcoa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.settings_activity.goBack
import org.json.JSONException
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {

    var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreferences= getSharedPreferences("ecoa", Context.MODE_PRIVATE)


        goBack.setOnClickListener(View.OnClickListener {
            this.finish()
        })

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            bindPreferencesSummaryToValue(findPreference("logout")!!)
            bindPreferencesSummaryToValue(findPreference("deleteAcc")!!)
   //         Log.d("ss", findPreference("logout")!!)

        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            val context = MainApplication.applicationContext()
            sharedPreferences =   context.getSharedPreferences("ecoa", Context.MODE_PRIVATE)
            return if(preference!!.key == "editProfile"){
               val intent = Intent(context, ProfileActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
                true
                }else if(preference.key == "logout"){
                    sharedPreferences!!.edit().clear().apply()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent)
                    true
                }else if(preference.key == "deleteAcc") {
                    val postObject = JSONObject()
                    val queue = Volley.newRequestQueue(context)

                    try {
                        val userId = sharedPreferences!!.getInt("userId", 0)
                        val email = sharedPreferences!!.getString("email", "")
                        postObject.put("email", email)
                        postObject.put("userId", userId)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    val objRequest = JsonObjectRequest(
                        Request.Method.POST, url, postObject,
                        Response.Listener { response ->

                            if (response.has("message")) {
                                Snackbar.make(goBack, "Email enviado com sucesso", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(resources.getColor(R.color.themeRegister))
                                    .setTextColor(resources.getColor(R.color.white))
                                    .show()
                            } else {
                                //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                            }
                        },
                        Response.ErrorListener { error ->
                            Log.e("OnError", error.toString())
                        })

                    queue.add(objRequest)
                    true
                }else if(preference.key == "changePassword"){
                    val postObject = JSONObject()
                    val queue = Volley.newRequestQueue(context)

                    try {
                        val userId = sharedPreferences!!.getInt("userId", 0)
                        val email = sharedPreferences!!.getString("email", "")
                        postObject.put("userId", userId)
                        postObject.put("email", email)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    val objRequest = JsonObjectRequest(
                        Request.Method.POST, url2, postObject,
                        Response.Listener { response ->

                            if (response.has("message")) {
                                Toast.makeText(context, "Email enviado", Toast.LENGTH_LONG).show()
                            } else {
                                //  Toast.makeText(this@LoginActivity, response.toString(), Toast.LENGTH_LONG).show()
                            }
                        },
                        Response.ErrorListener { error ->
                            Log.e("OnError", error.toString())
                        })

                    queue.add(objRequest)
                    true
                    true
                }else false
        }
    }

    companion object {

        private val url = "https://app.ecoa.pt/api/user/delete.php"
        private val url2 = "https://app.ecoa.pt/api/user/change_pass.php"
        internal var request: StringRequest? = null
        var sharedPreferences : SharedPreferences? = null


        private val sBindPreferencesSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            true
        }

        private fun bindPreferencesSummaryToValue(preference: Preference){
            preference.onPreferenceChangeListener = sBindPreferencesSummaryToValueListener

            //sBindPreferencesSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.context).getString(preference.key, ""))
        }

    }

}