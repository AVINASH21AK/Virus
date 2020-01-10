package com.kody.daawatstaff.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.LightingColorFilter
import android.view.KeyEvent
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.kody.daawatstaff.R


@SuppressLint("ResourceAsColor")
class CustomProgressDialog(context: Context) : Dialog(context) {

    internal var llMainBg: LinearLayout
    internal var mProgressBar: ProgressBar

    init {
        window!!.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)
        window!!.setBackgroundDrawableResource(R.drawable.prograss_bg) //temp removed
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progressbar)



        llMainBg = findViewById(R.id.llMainBg) as LinearLayout

        setCancelable(false)

        mProgressBar = findViewById(R.id.progressBar) as ProgressBar


        /*int mul = 0xFFFFFF00; //remove BLUE component
		int add = 0x0000FF00; //set GREEN full*/


        val mul = 0x00000000     //Transparent
        //int mul = 0xFFFF0000;   //remove BLUE & Green component
        val add = -0xBA820A     //0x00FF0000; -- Pure Red color
        //0xFF601421 -- dark red shade

        mProgressBar.indeterminateDrawable.colorFilter = LightingColorFilter(mul, add)

        setOnKeyListener { arg0, keyCode, event ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()

                (context as Activity).finish()
            }
            true
        }


    }

    fun setMessage(message: String) {}
}