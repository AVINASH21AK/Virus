package com.kody.daawatstaff.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R

class ActVerification : BaseActivity(){

    lateinit var veriify_tvMobile : AppCompatTextView
    lateinit var veriify_btnSkip :AppCompatButton
    lateinit var veriify_btnVerify :AppCompatButton
    lateinit var veriify_editOTP :TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewGroup.inflate(this, R.layout.act_verification, base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_ivBack.visibility = View.VISIBLE
        base_ivHomeMenu.visibility = View.GONE
        base_tvTitle.text = getString(R.string.label_verification)

        initialize()
        clickEvent()

    }

    private fun initialize(){

        veriify_tvMobile = findViewById(R.id.veriify_tvMobile) as AppCompatTextView
        veriify_btnSkip = findViewById(R.id.veriify_btnSkip) as AppCompatButton
        veriify_btnVerify = findViewById(R.id.veriify_btnVerify) as AppCompatButton
        veriify_editOTP = findViewById(R.id.veriify_editOTP) as TextInputEditText

    }

    private fun clickEvent(){

        veriify_btnVerify.setOnClickListener(View.OnClickListener {
            if(isValidData())
            {
                App.showSnackBar(veriify_btnVerify, "Success")

                Handler().postDelayed(Runnable {
                    finish()
                },1000)
            }
        })

        veriify_btnSkip.setOnClickListener(View.OnClickListener {
            finish()
        })

    }

    private fun isValidData(): Boolean {
        var validForgot = true

        val strMobile : String

        strMobile = veriify_editOTP.text.toString().trim()

        if(strMobile.isNullOrBlank() || strMobile.isBlank() || strMobile.isEmpty())
        {
            veriify_editOTP.setError("Please Enter OTP")
            validForgot = false
        }
        else if(strMobile.length < 4)
        {
            veriify_editOTP.setError("Enter Valid OTP")
            validForgot = false
        }
        else
        {
            validForgot = true
        }

        return validForgot
    }
}
