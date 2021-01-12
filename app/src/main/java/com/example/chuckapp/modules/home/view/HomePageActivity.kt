package com.example.chuckapp.modules.home.view

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.signUp.MeResponse
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.modules.home.view.bottomNavigation.AccountFragment
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment1
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment2
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment3
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_account.*
import retrofit2.Call
import retrofit2.Response

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


    private fun makeCurrentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayId,fragment)
            commit()
        }


    }









}


