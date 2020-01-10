package com.kody.daawatstaff.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.kody.daawatstaff.R

class ActForgotPassword : BaseActivity(){

    lateinit var edtMobile : TextInputEditText
    lateinit var btnSubmit :AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewGroup.inflate(this, R.layout.act_forgotpass, base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_ivBack.visibility = View.VISIBLE
        base_ivHomeMenu.visibility = View.GONE
        base_tvTitle.text = getString(R.string.label_forgot_password)

        initialize()
        clickEvent()

    }

    private fun initialize(){

        edtMobile = findViewById(R.id.editTextMobileForgotPass) as TextInputEditText
        btnSubmit = findViewById(R.id.btnSubmitForgotPass) as AppCompatButton

    }

    private fun clickEvent(){

        btnSubmit.setOnClickListener(View.OnClickListener {
            if(isValidData())
            {
                //App.showSnackBar(btnSubmit, "Check your email, we have send you reset link")
                //App.showSnackBar(btnSubmit, "OTP send to your mobile number")

                Handler().postDelayed(Runnable {
                    finish()
                },1000)
            }
        })

    }

    private fun isValidData(): Boolean {
        var validForgot = true

        val strMobile : String

        strMobile = edtMobile.text.toString().trim()

        if(strMobile.isNullOrBlank() || strMobile.isBlank() || strMobile.isEmpty())
        {
            edtMobile.setError("Please Enter Your Mobile Number")
            validForgot = false
        }
        else if(strMobile.length < 10)
        {
            edtMobile.setError("Enter Valid Mobile Number")
            validForgot = false
        }
        else
        {
            validForgot = true
        }

        return validForgot
    }
}
