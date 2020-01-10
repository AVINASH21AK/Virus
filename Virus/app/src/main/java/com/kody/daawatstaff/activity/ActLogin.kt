package com.kody.daawatstaff.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.CountryResponce
import com.kody.daawatcustomer.api.responce.LoginResponce
import com.kody.daawatstaff.R
import com.kody.daawatstaff.api.model.CountryModel
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.utils.Constant
import com.orhanobut.hawk.Hawk
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActLogin : BaseActivity(){

    var strTAG : String = "ActLogin"

    lateinit var login_tvForgetPass : AppCompatTextView
    lateinit var login_edtCode :  TextInputEditText
    lateinit var login_edtMobile :  TextInputEditText
    lateinit var login_edtPassword : TextInputEditText
    lateinit var login_btnLogin : AppCompatButton
    lateinit var login_spn : Spinner

    var strArrCountry = arrayListOf<String>()
    var strCountryCode : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewGroup.inflate(this, R.layout.act_login, base_llSubMainContainer)
        base_rlToolbar.visibility = View.GONE

        initialize()
        clickEvent()

        login_edtMobile.setText("9409322191")
        login_edtPassword.setText("39836163")

    }

    override fun onResume() {
        super.onResume()

        if(App.arrCountry == null || App.arrCountry.size == 0){
            apiCountry()
        }else{
            setData()
        }
    }

    private fun initialize(){

        login_spn = findViewById(R.id.login_spn) as Spinner
        login_tvForgetPass = findViewById(R.id.login_tvForgetPass) as AppCompatTextView
        login_edtCode = findViewById(R.id.login_edtCode) as TextInputEditText
        login_edtMobile = findViewById(R.id.login_edtMobile) as TextInputEditText
        login_edtPassword = findViewById(R.id.login_edtPassword) as TextInputEditText
        login_btnLogin = findViewById(R.id.login_btnLogin) as AppCompatButton

        if(App.arrCountry == null || App.arrCountry.size == 0){
            apiCountry()
        }else{
            setData()
        }
    }

    private fun clickEvent(){
        login_tvForgetPass.setOnClickListener(View.OnClickListener {
            val goToForgotPass = Intent(this, ActForgotPassword::class.java)
            startActivity(goToForgotPass)
        })

        login_btnLogin.setOnClickListener(View.OnClickListener {
            val strMobile = login_edtMobile.text.toString().trim()
            val strPass = login_edtPassword.text.toString()

            if(checkValidation(strMobile, strPass))
            {
                apiLogin(this, strMobile, strPass)

            }
        })

        login_edtCode.setOnClickListener(View.OnClickListener {
            login_spn.performClick()
        })



        login_spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                login_spn.setSelection(position)

                for(i in 0..App.arrCountry.size-1){
                    if(App.arrCountry[i].name == strArrCountry[position]){
                        strCountryCode = App.arrCountry[i].code
                        login_edtCode.setText(strCountryCode)
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //login_spn.setSelection(0)
            }
        }

    }

    private fun checkValidation(strMobile : String, strPass : String): Boolean {



        if(strMobile.isNullOrBlank() || strMobile.isBlank() || strMobile.isEmpty())
        {
            App.showSnackBar(login_btnLogin, "Please Enter Your Mobile Number")
            return false
        }
        else if(strPass.isNullOrBlank() || strPass.isBlank() || strPass.isEmpty())
        {
            App.showSnackBar(login_btnLogin, "Please Enter Your Password")
            return false
        }
        else if(strMobile.length < 10)
        {
            App.showSnackBar(login_btnLogin, "Enter Valid Mobile Number")
            return false
        }
        else if(strPass.length < 8)
        {
            App.showSnackBar(login_btnLogin, "Password should be minimum 8 characters long")
            return false
        }
        else{
            return true
        }
    }

    private fun setData(){

        if(App.arrCountry != null && App.arrCountry.size>0){
            for(i in 0..App.arrCountry.size-1){
                strArrCountry.add((App.arrCountry[i].name))
            }
        }

        val categoryAdapter= ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strArrCountry)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        login_spn.setAdapter(categoryAdapter)

        strCountryCode = App.arrCountry[0].code
        login_edtCode.setText(strCountryCode)

    }

    private fun apiCountry(){

        App.arrCountry = ArrayList()

        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }


        val call = App.getRetrofitApiService().country_list()


        call.enqueue(object : Callback<CountryResponce> {
            override fun onResponse(call: Call<CountryResponce>, response: Response<CountryResponce>){

                customProgressDialog.dismiss()

                if (response.code() == 200) {

                    val countryResponce = response.body()

                    if (countryResponce == null) {

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

                        if (countryResponce.status.equals(ApiFunction.status200)) {

                            if(countryResponce.arrCountry != null && countryResponce.arrCountry.size>0){
                                App.arrCountry.clear()
                                App.arrCountry.addAll(countryResponce.arrCountry)

                                setData()
                            }

                        }

                    }


                }
                else{
                    App.showLog(strTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }


            }

            override fun onFailure(call: Call<CountryResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                App.showLog(strTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })

    }




    private fun apiLogin(context : Context, strMobile : String, strPass : String) {

        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }

        var hashMap : HashMap<String, RequestBody> = HashMap()
        hashMap.put(ApiFunction.PARM_COUNTRY_CODE, App.createPartFromString(strCountryCode))
        hashMap.put(ApiFunction.PARM_PHONE_NUMBER, App.createPartFromString(strMobile))
        hashMap.put(ApiFunction.PARM_PASSWORD, App.createPartFromString(strPass))
        hashMap.put(ApiFunction.PARM_DEVICE_NAME, App.createPartFromString(Hawk.get(Constant.sharedPref_DEVICE_NAME)))
        hashMap.put(ApiFunction.PARM_MODEL_NAME, App.createPartFromString(Hawk.get(Constant.sharedPref_MODEL_NAME)))
        hashMap.put(ApiFunction.PARM_DEVICE_TOKEN, App.createPartFromString(Hawk.get(Constant.sharedPref_DEVICE_TOKEN)))
        hashMap.put(ApiFunction.PARM_OS_VERSION, App.createPartFromString(Hawk.get(Constant.sharedPref_OS_VERSION)))
        hashMap.put(ApiFunction.PARM_UUID, App.createPartFromString(Hawk.get(Constant.sharedPref_UUID)))
        hashMap.put(ApiFunction.PARM_IP, App.createPartFromString(Hawk.get(Constant.sharedPref_IP)))
        hashMap.put(ApiFunction.PARM_DEVICE_TYPE, App.createPartFromString(App.apiDeviceType))


        val call = App.getRetrofitApiService().login(hashMap)


        //-- Print Responce
        var apiParameter : String = ""
        for (entry in hashMap) {
            val key = entry.key
            val value = entry.value
            apiParameter = apiParameter + "\n" + (key+ " : " + App.bodyToString(value))
        }
        App.showLog(strTAG, "${ApiFunction.OP_LOGIN} : $apiParameter")

        call.enqueue(object : Callback<LoginResponce> {
            override fun onResponse(call: Call<LoginResponce>, response: Response<LoginResponce>) {
                customProgressDialog.dismiss()

                if (response.code() == 200) {

                    val loginResponce = response.body()

                    if (loginResponce == null) {

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
                        App.showLogResponce("OUTPUT ${ApiFunction.OP_LOGIN}", Gson().toJson(response.body()))

                        if (loginResponce.status.equals(ApiFunction.status200)) {
                            Hawk.put(Constant.sharedPref_ISLOGIN, true)
                            Hawk.put(Constant.sharedPref_TOKEN, loginResponce.data.token)
                            Hawk.put(Constant.sharedPref_USERID, loginResponce.data.id)
                            Hawk.put(Constant.sharedPref_USER_NAME, loginResponce.data.name)
                            Hawk.put(Constant.sharedPref_USER_PHONENUMBER, loginResponce.data.phone_number)
                            Hawk.put(Constant.sharedPref_USER_STATUS, loginResponce.data.status)
                            Hawk.put(Constant.sharedPref_USER_TYPE, loginResponce.data.user_type)

                            var goToChangePass = Intent(context, ActHome::class.java)
                            startActivity(goToChangePass)
                            finishAffinity()
                        }
                        else if (loginResponce.message != null && loginResponce.message.length>0) {
                            App.showSnackBar(login_btnLogin, "${loginResponce.message}")
                        }
                    }


                }
                else{
                    App.showLog(strTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }
            }

            override fun onFailure(call: Call<LoginResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                App.showSnackBar(login_btnLogin, getString(R.string.error_SomethingWentWrong))
                App.showLog(strTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })

    }


}