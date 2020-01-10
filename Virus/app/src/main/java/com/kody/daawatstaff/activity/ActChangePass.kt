package com.kody.daawatstaff.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R

class ActChangePass : BaseActivity(){

    lateinit var edtOldPass : TextInputEditText
    lateinit var edtNewPass : TextInputEditText
    lateinit var edtConfirmPass : TextInputEditText
    lateinit var btnUpdate : AppCompatButton
    lateinit var strOldPass : String
    lateinit var strNewPass : String
    lateinit var strConfirmPass : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.activity_change_pass,base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_ivBack.visibility = View.GONE
        base_ivHomeMenu.visibility = View.VISIBLE
        base_tvTitle.text = getString(R.string.label_change_password)

        initialize()
        clickListner()

    }

    override fun onResume() {
        super.onResume()
        App.strCurrentScreen = getString(R.string.label_change_password)
    }


    private fun initialize() {
        edtOldPass = findViewById(R.id.edtOldPasswordChangePass) as TextInputEditText
        edtNewPass = findViewById(R.id.edtNewPasswordChangePass) as TextInputEditText
        edtConfirmPass = findViewById(R.id.edtConfirmPasswordChangePass) as TextInputEditText
        btnUpdate = findViewById(R.id.btnUpdateChangePass) as AppCompatButton

    }

    private fun clickListner() {
        btnUpdate.setOnClickListener(View.OnClickListener {


            if(isValidData())
            {
                val goToHome = Intent(this,ActHome::class.java)
                startActivity(goToHome)
                finishAffinity()
            }
        })

    }


    private fun isValidData(): Boolean {
        var validChangePass : Boolean

        validChangePass = true

        strOldPass = edtOldPass.text.toString()
        strNewPass = edtNewPass.text.toString()
        strConfirmPass = edtConfirmPass.text.toString()



        if(strOldPass.isNullOrBlank() || strOldPass.isBlank() || strOldPass.isEmpty()) {
            edtOldPass.setError("Please Enter Your Old Password")
            validChangePass = false
        }
       else if(strNewPass.isNullOrBlank() || strNewPass.isBlank() || strNewPass.isEmpty())
        {
            edtNewPass.setError("Please Enter Your New Password")
            validChangePass = false
        }
        else  if(strConfirmPass.isNullOrBlank() || strConfirmPass.isBlank() || strConfirmPass.isEmpty())
        {
            edtConfirmPass.setError("Please Enter Your Confirm Password")
            validChangePass = false
        }
        else if(strOldPass.length < 8)
        {
            edtOldPass.setError("Password should be minimum 8 characters long")
            validChangePass = false
        }
        else if(strNewPass.length < 8)
        {
            edtNewPass.setError("New Password should be minimum 8 characters long")
            validChangePass = false
        }
        else if(strConfirmPass.length < 8)
        {
            edtConfirmPass.setError("New Password should be minimum 8 characters long")
            validChangePass = false
        }
        else if(!strNewPass.equals(strConfirmPass))
        {
            edtConfirmPass.setError("New And Confirm Password must be same")
            validChangePass = false
        }
        else{
            validChangePass = true
        }

        return validChangePass
    }
}