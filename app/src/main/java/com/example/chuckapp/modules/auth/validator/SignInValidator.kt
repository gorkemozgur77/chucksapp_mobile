package com.example.chuckapp.modules.auth.validator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import com.example.chuckapp.R

class SignInValidator : AuthValidator(){

    @SuppressLint("UseCompatLoadingForDrawables")
    fun wrongInfoAlertDialog(view: View){

        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Error")
        builder.setMessage("Email or password is wrong.")
        builder.setIcon(view.resources.getDrawable(R.drawable.ic_baseline_error_24))

        val alertDialog = builder.create()
        alertDialog.show()
    }


}