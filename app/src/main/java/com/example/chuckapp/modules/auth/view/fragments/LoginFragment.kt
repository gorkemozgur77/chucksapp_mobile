package com.example.chuckapp.modules.auth.view.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chuckapp.R
import com.example.chuckapp.model.requestModels.auth.LoginRequest
import com.example.chuckapp.model.requestModels.auth.LoginResponse
import com.example.chuckapp.service.ApiClient
import com.example.chuckapp.service.SessionManager
import com.example.chuckapp.modules.auth.util.AuthUtil
import com.example.chuckapp.util.Constants
import com.example.chuckapp.modules.home.HomePageActivity
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var apiClient: ApiClient
    private val authUtil = AuthUtil()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionmanager = context?.let { SessionManager(it) }
        textButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            Navigation.findNavController(it).navigate(action)
        }
        loginButton.setOnClickListener {
            signinViaMail(it.context)


        }

    }
    fun signinViaMail(context : Context){
        var sessionmanager = SessionManager(context)
        apiClient = ApiClient()
        apiClient.getAuthApiService().signInViaEmail(getInfo(context)).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    println(response.body())
                    response.body()!!.token?.PermToken?.let { sessionmanager.savePermToken(it) }
                    response.body()!!.token?.Token?.let { sessionmanager.saveAuthToken(it) }
                    val intent = Intent(context, HomePageActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else {
                    println(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("Servera baglanmadik")
            }


        })

    }


    fun getInfo(context: Context) : LoginRequest {

        val requestBody = LoginRequest(
                loginEmailId.text.toString(),
                loginSifreID.text.toString(),
                Constants.PLATFORM_NAME,
                authUtil.getIpAdress(context),
                authUtil.getMacAdress(context),
                authUtil.getDeviceName()
        )
        return  requestBody

    }

}