package com.kody.daawatstaff.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.CommonResponce
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R
import com.kody.daawatstaff.utils.Constant
import com.orhanobut.hawk.Hawk
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActContactUs : BaseActivity(){

    var strTAG : String = "ActContactUs"

    lateinit var contactus_edtName : TextInputEditText
    lateinit var contactus_edtEmail : TextInputEditText
    lateinit var contactus_edtMobile : TextInputEditText
    lateinit var contactus_edtMessage : TextInputEditText
    lateinit var contactus_btnSumbit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.act_contactus,base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_tvTitle.text = getString(R.string.label_support_us)
        base_ivBack.visibility = View.GONE
        base_ivHomeMenu.visibility = View.VISIBLE


        initialize()
        clickEvent()

    }

    override fun onResume() {
        super.onResume()
        App.strCurrentScreen = getString(R.string.label_support_us)
    }


    private fun initialize(){
        contactus_edtName = findViewById(R.id.contactus_edtName) as TextInputEditText
        contactus_edtEmail = findViewById(R.id.contactus_edtEmail) as TextInputEditText
        contactus_edtMobile = findViewById(R.id.contactus_edtMobile) as TextInputEditText
        contactus_edtMessage = findViewById(R.id.contactus_edtMessage) as TextInputEditText
        contactus_btnSumbit = findViewById(R.id.contactus_btnSumbit) as Button

        if(Hawk.contains(Constant.sharedPref_USER_NAME)){
            contactus_edtName.setText("${Hawk.get(Constant.sharedPref_USER_NAME, "")}")
            contactus_edtName.isEnabled = false
        }

        if(Hawk.contains(Constant.sharedPref_USER_PHONENUMBER)){
            contactus_edtMobile.setText("${Hawk.get(Constant.sharedPref_USER_PHONENUMBER, "")}")
            contactus_edtMobile.isEnabled = false
        }
    }

    private fun clickEvent(){


        contactus_btnSumbit.setOnClickListener(View.OnClickListener {
            var strName : String = contactus_edtName.text.toString().trim()
            var strMobile : String = contactus_edtMobile.text.toString().trim()
            var strEmail : String = contactus_edtEmail.text.toString().trim()
            var strDescription : String = contactus_edtMessage.text.toString().trim()

            if(checkValidation(strName, strEmail, strDescription)){
                apiContactUs(strName, strEmail, strDescription, strMobile)
            }
        })





    }

    private fun checkValidation(strName: String, strEmail: String, strDescription: String): Boolean {

        if(strName.isNullOrBlank() || strName.isEmpty() || strName.isBlank())
        {
            App.showSnackBar(contactus_btnSumbit, "Please Enter Your Name")
            return false
        }
        else if(strEmail.isNullOrBlank() || strEmail.isEmpty() || strName.isBlank())
        {
            App.showSnackBar(contactus_btnSumbit, "Please Enter EmailId")
            return false
        }
        else if(strDescription.isNullOrBlank() || strDescription.isEmpty() || strDescription.isBlank())
        {
            App.showSnackBar(contactus_btnSumbit, "Please Enter Your Message")
            return false
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
        {
            App.showSnackBar(contactus_btnSumbit, "Please Enter Valid EmailId")
            return false
        }
        else{
            return true
        }

    }


    private fun apiContactUs(strName : String, strEmail : String, strDescription : String, strMobile : String){


        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }


        var hashMap : HashMap<String, RequestBody> = HashMap()
        hashMap.put(ApiFunction.PARM_NAME, App.createPartFromString(strName))
        hashMap.put(ApiFunction.PARM_EMAIL, App.createPartFromString(strEmail))
        hashMap.put(ApiFunction.PARM_MESSAGE, App.createPartFromString(strDescription))
        //hashMap.put(ApiFunction.PARM_COUNTRY_CODE, App.createPartFromString(strCountryCode))
        hashMap.put(ApiFunction.PARM_PHONE_NUMBER, App.createPartFromString(strMobile))

        val call = App.getRetrofitApiService().contactus(hashMap)

        //-- Print Responce
        var apiParameter : String = ""
        for (entry in hashMap) {
            val key = entry.key
            val value = entry.value
            apiParameter = apiParameter + "\n" + (key+ " : " + App.bodyToString(value))
        }
        App.showLog(strTAG, "${ApiFunction.OP_CONTACTUS} : $apiParameter")


        call.enqueue(object : Callback<CommonResponce> {
            override fun onResponse(call: Call<CommonResponce>, response: Response<CommonResponce>){

                customProgressDialog.dismiss()

                if (response.code() == 200) {

                    val commonResponce = response.body()

                    if (commonResponce == null) {

                        val responseBody = response.errorBody()

                        if (responseBody != null) {
                            try {
                                App.showLog(strTAG, "{------ Api Resonponce Body Null ----------}")
                                App.showLogResponce(strTAG,"${responseBody.string()}")

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    else{
                        App.showLog(strTAG,"----------------------")
                        App.showLogResponce("OUTPUT ${ApiFunction.OP_COUNTRY_LIST}", Gson().toJson(response.body()))

                        if (commonResponce.status.equals(ApiFunction.status200)) {
                            App.showSnackBar(contactus_btnSumbit, "${commonResponce.message}")

                            contactus_edtEmail.setText("")
                            contactus_edtMessage.setText("")

                        }
                        else if (commonResponce.status.equals(ApiFunction.status401)) {
                            App.showSnackBar(contactus_btnSumbit, "${commonResponce.message}")

                            Handler().postDelayed(Runnable {
                                LogoutFinishAllActivity()
                            },1000)
                        }
                        else if (commonResponce.message != null && commonResponce.message.length>0) {
                            App.showSnackBar(contactus_btnSumbit, "${commonResponce.message}")
                        }

                    }


                }
                else{
                    App.showLog(strTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }


            }

            override fun onFailure(call: Call<CommonResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                App.showLog(strTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })

    }



}