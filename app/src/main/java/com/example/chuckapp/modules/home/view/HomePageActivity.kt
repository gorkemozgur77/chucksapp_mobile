package com.example.chuckapp.modules.home.view

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.chuckapp.R
import com.example.chuckapp.modules.home.service.HomeClient
import com.example.chuckapp.modules.home.view.appBarNavigation.AddFriendPage
import com.example.chuckapp.modules.home.view.bottomNavigation.AccountFragment
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment1
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment2
import com.example.chuckapp.modules.home.view.bottomNavigation.Bottomfragment3
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var apiclient : HomeClient

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
        topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addFriend -> NavigateToAddfriend()
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

    private fun NavigateToAddfriend(){
        val intent = Intent(baseContext, AddFriendPage::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

//    fun signOut(context: Context,view : View){
//        apiclient = HomeClient()
//        val sessionManager = SessionManager(context)
//        apiclient.getHomeApiService(context).signOut(SignOutRequest(Constants.PLATFORM_NAME)).enqueue(object : retrofit2.Callback<LoginResponse>{
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                println(response.body())
//                println(response.errorBody()?.string())
//                if (response.isSuccessful){
//                    println("cikis yapildi")
//                    sessionManager.deleteTokens()
//                    val intent = Intent(baseContext, AuthActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                println(t.cause)
//                println(t.message)
//                print(t.stackTrace)
//            }
//        })
//    }
//
//    fun getInfo(context: Context){
//
//        apiclient = HomeClient()
//        apiclient.getHomeApiService(context).getProfile().enqueue(object :retrofit2.Callback<MeResponse>{
//            override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
//                if (response.isSuccessful){
//                    textView3.text = response.body()?.user?.isim.toString() + " " + response.body()?.user?.soyisim.toString()
//                    textView2.text = response.body()?.user?.eposta.toString()
//                }
//            }
//
//            override fun onFailure(call: Call<MeResponse>, t: Throwable) {
//
//            }
//
//        })
//    }

}


