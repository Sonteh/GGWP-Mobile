package com.example.ggwp_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_start_screen.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false


//        val search: BottomNavigationItemView = findViewById(R.id.miHome)
//
//        search.setOnClickListener { view ->
//            view.findNavController().navigate(R.id.SearchScreenFragment)
//        }
        val search = StartScreenFragment()
        val mainStats = SearchScreenFragment()
        val charts = ChartsScreenFragment()
        val patchNotes = PatchNotesFragment()
        val matchHistory = MatchHistoryFragment()

        //makeCurrentFragment(search)

//        if (search.isVisible){
//            bottomNavigationView.visibility = View.GONE
//        }
//        else {
//            bottomNavigationView.visibility = View.VISIBLE
//        }
        val lej = findViewById<CoordinatorLayout>(R.id.navbarLayout)
        lej.visibility = View.GONE
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //R.id.fab -> makeCurrentFragment(search)
                R.id.miHome -> makeCurrentFragment(mainStats)
                R.id.miCos2 -> makeCurrentFragment(patchNotes)
                R.id.miSearch -> makeCurrentFragment(matchHistory)
                R.id.miCos3 -> makeCurrentFragment(charts)
            }
            true
        }
        val floating: FloatingActionButton = findViewById(R.id.fab)
        floating.setOnClickListener {
            makeCurrentFragment(search)
            lej.visibility = View.GONE
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
}