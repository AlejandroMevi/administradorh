package com.venturessoft.human.views.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.venturessoft.human.databinding.ActivityPaypalBinding
import com.venturessoft.human.services.URL
import java.util.*


class PayPalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaypalBinding
    private var price: String = ""
    private val INTENT_PRICE = "price"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaypalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        loadIntentData()
        loadWebView()
        listenButtons()
    }

    private fun loadIntentData() {
        if (intent != null) {
            price = (intent!!.getStringExtra(INTENT_PRICE))!!.replace(".", "%2E")
            if (price.contains(",")) {
                price = price.replace(",", "%2E")
            }
        }
    }

    private fun listenButtons() {
        binding.btnBackPaypal.setOnClickListener {
            finish()
        }
    }

    private fun loadWebView() {
        showLoadinAnimotion()
        binding.webViewPayPal.settings.javaScriptEnabled = true
        binding.webViewPayPal.requestFocus(View.FOCUSABLE)
        binding.webViewPayPal.settings.allowContentAccess = true
        binding.webViewPayPal.settings.domStorageEnabled = true
        binding.webViewPayPal.settings.useWideViewPort = true
        binding.webViewPayPal.settings.allowFileAccess = true
        binding.webViewPayPal.settings.blockNetworkLoads = false
        binding.webViewPayPal.settings.blockNetworkImage = false
        binding.webViewPayPal.settings.allowFileAccessFromFileURLs = true
        binding.webViewPayPal.settings.allowUniversalAccessFromFileURLs = true
        binding.webViewPayPal.settings.setNeedInitialFocus(true)
        CookieManager.getInstance().setAcceptCookie(true);
        binding.webViewPayPal.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url)
                if (url.equals("https://pagocancelado.com/")) {
                    val output = Intent()
                    output.putExtra("code", "pagocancelado")
                    setResult(RESULT_OK, output)
                    this@PayPalActivity.finish()
                }
                if (url!!.contains("https://pagoexitoso.com/?folio=")) {
                    showLoadinAnimotion()
                    val output = Intent()
                    val folio = uri.getQueryParameter("folio")
                    val date = uri.getQueryParameter("date")
                    output.putExtra("code", "pagoexitoso")
                    output.putExtra("folio", folio)
                    output.putExtra("date", date)
                    setResult(RESULT_OK, output)
                    this@PayPalActivity.finish()
                }
                if (url == "https://pagoerror.com") {
                    val output = Intent()
                    output.putExtra("code", "pagoerror")
                    setResult(RESULT_OK, output)
                    this@PayPalActivity.finish()
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                hideLoadinAnimotion()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }
        val urlpay = URL.URL_BASE_PAYPAL + price
        binding.webViewPayPal.clearCache(true)
        binding.webViewPayPal.loadUrl(urlpay)
    }

    private fun showLoadinAnimotion() {
        binding.loadingAnimationPaypal.root.visibility = View.VISIBLE
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoadinAnimotion() {
        binding.loadingAnimationPaypal.root.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}