package com.example.ggwp_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


class PatchNotesFragment: Fragment() {

    private val viewModel: SummonerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_patch_notes_screen, container, false)

        val myWebView = layout.findViewById<WebView>(R.id.patchNotesView)

        myWebView.webViewClient = WebViewClient()
        
        webViewSetup(myWebView)
        return layout
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(myWebView: WebView) {
        myWebView.apply {
            loadUrl("https://na.leagueoflegends.com/en-us/news/tags/patch-notes")
            settings.javaScriptEnabled = true
        }
    }

}