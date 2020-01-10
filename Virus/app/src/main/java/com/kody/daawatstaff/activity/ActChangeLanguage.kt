package com.kody.daawatstaff.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.kody.daawatstaff.R

class ActChangeLanguage : BaseActivity(){

    lateinit var txtvwEng : AppCompatTextView
    lateinit var txtvwArabic : AppCompatTextView
    lateinit var btnNext : AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.act_changelang, base_llSubMainContainer)
        base_rlToolbar.visibility = View.GONE

        initialize()
        clickEvent()

    }



     private fun initialize(){
         txtvwEng = findViewById(R.id.textViewEnglish) as AppCompatTextView
         txtvwArabic = findViewById(R.id.textViewArabic) as AppCompatTextView
         btnNext = findViewById(R.id.btnNextChangeLanguage) as AppCompatButton

         txtvwArabic.setText(getString(R.string.arabic1) +" "+ getString(R.string.arabic2))


         //by default english is selected
         txtvwEng.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)

     }

     private fun clickEvent(){

         //select arabic
         txtvwArabic.setOnClickListener(View.OnClickListener {
             txtvwArabic.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)
             txtvwEng.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

         })

         //select english
         txtvwEng.setOnClickListener(View.OnClickListener {
             txtvwEng.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)
             txtvwArabic.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

         })

         //next to login
         btnNext.setOnClickListener(View.OnClickListener {
             val goToLogin = Intent(this, ActLogin::class.java)
             startActivity(goToLogin)

         })

     }
}