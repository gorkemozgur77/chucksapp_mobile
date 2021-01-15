package com.example.chuckapp.modules.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.chuckapp.R
import com.example.chuckapp.modules.home.view.bottomNavigation.AccountFragment
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment1
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment2
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment3
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_account.*
import java.util.*
import kotlin.concurrent.schedule

class HomePageActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        home_page_bottomNavigationViewId.background = null
        home_page_bottomNavigationViewId.menu.getItem(2).isEnabled = false
        makeCurrentFragment(Bottomfragment1())

        home_page_bottomNavigationViewId.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.message -> makeCurrentFragment(Bottomfragment1())
                R.id.feed -> makeCurrentFragment(Bottomfragment2())
                R.id.call -> makeCurrentFragment(Bottomfragment3())
                R.id.account -> makeCurrentFragment(AccountFragment())
            }
            true
        }
        home_page_bottomNavigationViewId.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.message -> print("")
                R.id.feed -> print("")
                R.id.call -> print("")
                R.id.account -> print("")
            }
            true
        }
    }

     fun toggleBottomAppBar(show: Boolean) {
        val transition: Transition = Fade()
        transition.duration = 100
        transition.addTarget(home_page_bottomAppbarId)
        TransitionManager.beginDelayedTransition(homepageLayId, transition)
         if (show) {
             home_page_bottomAppbarId.visibility = View.VISIBLE
             fabId.show()
         }
         else {
             home_page_bottomAppbarId.visibility = View.GONE
             fabId.hide()
         }
    }

    private fun makeCurrentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayId,fragment)
            commit()
        }


    }
}


