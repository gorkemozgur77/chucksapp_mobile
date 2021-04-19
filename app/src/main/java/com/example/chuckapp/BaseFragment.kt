package com.example.chuckapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.chuckapp.util.ProgressDialog

open class BaseFragment : Fragment() {
    var progressBar: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBar = context?.let {
            ProgressDialog(it)
        }
    }

    fun showProgressBar() {
        progressBar?.show()
    }

    fun hideProgressBar() {
        progressBar?.hide()
    }
}