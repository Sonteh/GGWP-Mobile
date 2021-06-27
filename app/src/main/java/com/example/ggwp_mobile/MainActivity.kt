package com.example.ggwp_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false


        val search = StartScreenFragment()
        val mainStats = SearchScreenFragment()
        val charts = ChartsScreenFragment()
        val patchNotes = PatchNotesFragment()
        val matchHistory = MatchHistoryFragment()


        val lej = findViewById<CoordinatorLayout>(R.id.navbarLayout)
        lej.visibility = View.GONE
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> makeCurrentFragment(mainStats)
                R.id.miCos2 -> makeCurrentFragment(patchNotes)
                R.id.miSearch -> makeCurrentFragment(matchHistory)
                R.id.miCos3 -> makeCurrentFragment(charts)
            }
            true
        }

        val floating: FloatingActionButton = findViewById(R.id.fab)

        floating.setOnClickListener {
            makeCurrentFragment(mainStats)
            lej.visibility = View.GONE
            makeCurrentFragment(search)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
}