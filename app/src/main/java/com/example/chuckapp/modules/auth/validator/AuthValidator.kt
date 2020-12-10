package com.example.chuckapp.modules.auth.validator

import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

open class AuthValidator {
    val canNotEmpty = "Field can not be left empty."
    val invalidEmail = "Invalid Email"
    val invalidPassword= "Password can not be less than 4 characters"


    fun isEmpty(view: TextView): Boolean {
        return TextUtils.isEmpty( view.text.toString() )
    }

    fun errorAction(layout: TextInputLayout, message: String){
        layout.isErrorEnabled = true
        layout.error = message
    }

    fun isEmailValid(view: TextView): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher( view.text.toString() ).matches()
    }

    fun isPasswordValid(view: TextView): Boolean{
        return view.text.toString().length > 4
    }



    fun layoutEmptyErrorValidator(a : TextInputLayout, b : TextView): Boolean{
        if ( isEmpty(b) ) {
            errorAction(a, canNotEmpty)
            return true
        }
        return false
    }

    fun layoutEmailRegexValidator(layout: TextInputLayout, view: TextView) {
        if ( layoutEmptyErrorValidator(layout, view) ) {
            return
        }
        if( !isEmailValid(view) ){
            errorAction(layout, invalidEmail)
        }
    }

    fun layoutPasswordValidator(layout: TextInputLayout, view: TextView){
        if (layoutEmptyErrorValidator(layout, view))
            return
        if ( !isPasswordValid(view) )
            errorAction(layout, invalidPassword)


    }

}