package com.example.ggwp_mobile

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

        //val patchNotes = layout.findViewById<WebView>(R.id.patchNotesView)

        //val patchnotes1: WebView = layout.findViewById(R.id.patchNotesView)

        val myWebView = layout.findViewById<WebView>(R.id.patchNotesView)
        //myWebView.loadUrl("http://www.example.com")
        myWebView.webViewClient = WebViewClient()
        
        webViewSetup(myWebView)
        return layout
    }

    private fun webViewSetup(myWebView: WebView) {
        myWebView.apply {
            loadUrl("https://na.leagueoflegends.com/en-us/news/tags/patch-notes")
            settings.javaScriptEnabled = true
        }
    }

}