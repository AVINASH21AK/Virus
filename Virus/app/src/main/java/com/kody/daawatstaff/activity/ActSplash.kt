package com.kody.daawatstaff.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.kody.daawatstaff.R
import com.kody.daawatstaff.utils.Constant
import com.orhanobut.hawk.Hawk

class ActSplash : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.act_splash, base_llSubMainContainer)
        base_rlToolbar.visibility = View.GONE

        setData()
        initialize()
        checkCountryData(false)


    }


    private fun setData(){

        if(!Hawk.contains(Constant.sharedPref_DEVICE_NAME) || Hawk.get(Constant.sharedPref_DEVICE_NAME, "").length == 0){
            Hawk.put(Constant.sharedPref_DEVICE_NAME, "")
        }

        if(!Hawk.contains(Constant.sharedPref_MODEL_NAME) || Hawk.get(Constant.sharedPref_MODEL_NAME, "").length == 0){
            Hawk.put(Constant.sharedPref_MODEL_NAME, "")
        }

        if(!Hawk.contains(Constant.sharedPref_DEVICE_TOKEN) || Hawk.get(Constant.sharedPref_DEVICE_TOKEN, "").length == 0){
            Hawk.put(Constant.sharedPref_DEVICE_TOKEN, "66EDDF532AE6124C1A4696B222B3D1F871483F3E0BA321991BD047A1A33360D2")
        }

        if(!Hawk.contains(Constant.sharedPref_OS_VERSION) || Hawk.get(Constant.sharedPref_OS_VERSION, "").length == 0){
            Hawk.put(Constant.sharedPref_OS_VERSION, "")
        }

        if(!Hawk.contains(Constant.sharedPref_UUID) || Hawk.get(Constant.sharedPref_UUID, "").length == 0){
            Hawk.put(Constant.sharedPref_UUID, "")
        }

        if(!Hawk.contains(Constant.sharedPref_IP) || Hawk.get(Constant.sharedPref_IP, "").length == 0){
            Hawk.put(Constant.sharedPref_IP, "")
        }

    }



    private fun initialize(){
        Handler().postDelayed(Runnable {

            if(Hawk.contains(Constant.sharedPref_ISLOGIN) && Hawk.get(Constant.sharedPref_ISLOGIN) &&
                Hawk.contains(Constant.sharedPref_TOKEN) && Hawk.get(Constant.sharedPref_TOKEN, "").length>0){
                val goToLogin = Intent(this, ActHome::class.java)
                startActivity(goToLogin)
                finish()
            }else{
                val goToLogin = Intent(this, ActChangeLanguage::class.java)
                startActivity(goToLogin)
                finish()
            }

        },3000)
    }


}
