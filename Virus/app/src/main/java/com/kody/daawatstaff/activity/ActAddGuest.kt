package com.kody.daawatstaff.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.CommonResponce
import com.kody.daawatcustomer.api.responce.CountryResponce
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActAddGuest : BaseActivity(){

    var strTAG : String = "ActAddGuest"

    lateinit var addguest_edtName :  TextInputEditText
    lateinit var addguest_edtCode :  TextInputEditText
    lateinit var addguest_edtMobile : TextInputEditText
    lateinit var addguest_spn :  Spinner
    lateinit var addguest_btnAddGuest : AppCompatButton

    var strArrCountry = arrayListOf<String>()
    var strCountryCode : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.activity_add_guest, base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_tvTitle.text = getString(R.string.label_add_guest)
        base_ivBack.visibility = View.VISIBLE
        base_ivHomeMenu.visibility = View.GONE

        initialize()
        clickEvent()

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
        addguest_edtName = findViewById(R.id.addguest_edtName) as TextInputEditText
        addguest_edtCode = findViewById(R.id.addguest_edtCode) as TextInputEditText
        addguest_spn = findViewById(R.id.addguest_spn) as Spinner
        addguest_edtMobile = findViewById(R.id.addguest_edtMobile) as TextInputEditText
        addguest_btnAddGuest = findViewById(R.id.addguest_btnAddGuest)

        if(App.arrCountry == null || App.arrCountry.size == 0){
            apiCountry()
        }else{
            setData()
        }
    }

    private fun clickEvent(){
        addguest_btnAddGuest.setOnClickListener(View.OnClickListener {

            val strName = addguest_edtName.text.toString().trim()
            val strMobile = addguest_edtMobile.text.toString().trim()

            if(checkValidation(strName, strMobile))
            {
                apiAddGuest(strName, strMobile)
            }
        })


        addguest_edtCode.setOnClickListener(View.OnClickListener {
            addguest_spn.performClick()
        })



        addguest_spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                addguest_spn.setSelection(position)

                for(i in 0..App.arrCountry.size-1){
                    if(App.arrCountry[i].name == strArrCountry[position]){
                        strCountryCode = App.arrCountry[i].code
                        addguest_edtCode.setText(strCountryCode)
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //login_spn.setSelection(0)
            }
        }
    }

    private fun checkValidation(strName : String, strMobile : String): Boolean {


        if(strName.isNullOrBlank() || strName.isEmpty() || strName.isBlank())
        {
            App.showSnackBar(addguest_btnAddGuest, "Please Enter Name")
            return false
        }
        else if(strMobile.isNullOrBlank() || strMobile.isEmpty() || strMobile.isBlank())
        {
            App.showSnackBar(addguest_btnAddGuest, "Please Enter Mobile Number")
            return false
        }
        else if(strMobile.length < 10)
        {
            App.showSnackBar(addguest_btnAddGuest, "Enter Valid Mobile Number")
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
        addguest_spn.setAdapter(categoryAdapter)

        strCountryCode = App.arrCountry[0].code
        addguest_edtCode.setText(strCountryCode)

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

    private fun apiAddGuest(strName : String, strMobile : String){


        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }


        var hashMap : HashMap<String, RequestBody> = HashMap()
        hashMap.put(ApiFunction.PARM_NAME, App.createPartFromString(strName))
        hashMap.put(ApiFunction.PARM_COUNTRY_CODE, App.createPartFromString(strCountryCode))
        hashMap.put(ApiFunction.PARM_PHONE_NUMBER, App.createPartFromString(strMobile))

        val call = App.getRetrofitApiService().add_guest(hashMap)

        //-- Print Responce
        var apiParameter : String = ""
        for (entry in hashMap) {
            val key = entry.key
            val value = entry.value
            apiParameter = apiParameter + "\n" + (key+ " : " + App.bodyToString(value))
        }
        App.showLog(strTAG, "${ApiFunction.OP_ADD_GUEST} : $apiParameter")


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
                            App.showSnackBar(addguest_btnAddGuest, "${commonResponce.message}")
                        }
                        else if (commonResponce.status.equals(ApiFunction.status401)) {
                            App.showSnackBar(addguest_btnAddGuest, "${commonResponce.message}")

                            Handler().postDelayed(Runnable {
                                LogoutFinishAllActivity()
                            },1000)
                        }
                        else if (commonResponce.message != null && commonResponce.message.length>0) {
                            App.showSnackBar(addguest_btnAddGuest, "${commonResponce.message}")
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