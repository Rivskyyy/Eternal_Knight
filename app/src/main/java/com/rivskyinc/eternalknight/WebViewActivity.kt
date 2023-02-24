package com.rivskyinc.eternalknight

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.rivskyinc.eternalknight.databinding.ActivityWebViewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

    GlobalScope.launch {
        getDataFromDB()
    }
//        val ref = FirebaseDatabase.getInstance().getReference("Data")
//
//        ref.child("url").get().addOnSuccessListener {
//            val loadUrl = it.value.toString()
//            binding.webView.loadUrl(loadUrl)
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }

        binding.webView.settings.javaScriptEnabled = true

        val userAgent = WebView(this).settings.userAgentString.replace("wv", "")

        binding.webView.settings.userAgentString = userAgent
        binding.webView.settings.loadWithOverviewMode = false
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccess = true

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)

        binding.webView.webChromeClient = object : WebChromeClient() {

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                webView: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(this@WebViewActivity)

                newWebView.settings.setSupportMultipleWindows(true)

                newWebView.settings.domStorageEnabled = true

                newWebView.settings.javaScriptEnabled = true

                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true

                newWebView.webChromeClient = this

                newWebView.settings.loadWithOverviewMode = false

                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {

                val type = "image/*"
                try {

                } catch (e: Exception) {

                    Toast.makeText(
                        applicationContext,
                        "Cannot Open File Chooser",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                return true
            }
        }
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    }
                }
            })

    }

    suspend fun getDataFromDB(){

        val ref = FirebaseDatabase.getInstance().getReference("Data")

        ref.child("url").get().addOnSuccessListener {
            val loadUrl = it.value.toString()
            binding.webView.loadUrl(loadUrl)
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }


}

