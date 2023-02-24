package com.rivskyinc.eternalknight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        GlobalScope.launch {
            val remoteConfig = fetchRemoteConfig()
            val value = remoteConfig.getBoolean("access")
            Log.d("TAG_ACCESS", value.toString())

            if (value) {
                startActivity(Intent(this@LoadingActivity, WebViewActivity::class.java))
            } else {
                startActivity(Intent(this@LoadingActivity, GameStartActivity::class.java))
            }
        }

    }

    suspend fun fetchRemoteConfig(): FirebaseRemoteConfig {

        val remoteConfig = FirebaseRemoteConfig.getInstance()

        return withContext(Dispatchers.IO) {

            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 1
            }

            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remoteConfig.activate()

                }
            }.await()


            remoteConfig
        }
    }


    override fun onBackPressed() {

        super.onBackPressed()
        this.finishAffinity()
    }
}
