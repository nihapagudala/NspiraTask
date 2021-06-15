package cgg.gov.`in`.nspiratask.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cgg.gov.`in`.nspiratask.R
import cgg.gov.`in`.nspiratask.utils.AppConstants
import cgg.gov.`in`.nspiratask.utils.Progress

class WebviewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var progress: Progress? = null
    private var isLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val sharedPref = getSharedPreferences(
            AppConstants.TASK, Context.MODE_PRIVATE
        )
        val url: String? = sharedPref.getString(AppConstants.URL, "")

        webView = findViewById(R.id.webview)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }
        url?.let { webView.loadUrl(it) }

        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                setProgressDialogVisibility(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                setProgressDialogVisibility(false)
                super.onPageFinished(view, url)
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                showToast(errorMessage)
//                infoTV.text = errorMessage
                setProgressDialogVisibility(false)
                super.onReceivedError(view, request, error)
            }
        }

    }

    private fun setProgressDialogVisibility(visible: Boolean) {
        if (visible) progress = Progress(this, R.string.please_wait, cancelable = true)
        progress?.apply { if (visible) show() else dismiss() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}