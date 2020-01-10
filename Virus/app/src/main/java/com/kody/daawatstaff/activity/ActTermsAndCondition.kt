package com.kody.daawatstaff.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.FAQTermsResponce
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActTermsAndCondition :BaseActivity(){

    var strTAG : String = "ActTermsAndCondition"

    lateinit var webViewTerms : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.activity_terms_and_condition, base_llSubMainContainer)

        baseShowLightStatusBarTheme(true)
        base_tvTitle.text = getString(R.string.label_terms_and_condition)
        base_ivBack.visibility = View.GONE
        base_ivHomeMenu.visibility = View.VISIBLE

        initialize()
        apiPageAll()
    }

    override fun onResume() {
        super.onResume()
        App.strCurrentScreen = getString(R.string.label_terms_and_condition)
    }

    private fun initialize(){
        webViewTerms = findViewById(R.id.webViewTermsAndCondition) as WebView


        webViewTerms.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

    }

    private fun apiPageAll(){

        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }

        val call = App.getRetrofitApiService().page_all()


        call.enqueue(object : Callback<FAQTermsResponce> {
            override fun onResponse(call: Call<FAQTermsResponce>, response: Response<FAQTermsResponce>){

                customProgressDialog.dismiss()

                if (response.code() == 200) {

                    val privacyTermsResponce = response.body()

                    if (privacyTermsResponce == null) {

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
                        App.showLogResponce("OUTPUT ${ApiFunction.OP_PAGE_ALL}", Gson().toJson(response.body()))

                        if (privacyTermsResponce.status.equals(ApiFunction.status200)) {
                            webViewTerms.loadUrl("${privacyTermsResponce.data.terms}")
                        }
                        else if (privacyTermsResponce.status.equals(ApiFunction.status401)) {
                            App.showSnackBar(webViewTerms, "${privacyTermsResponce.message}")

                            Handler().postDelayed(Runnable {
                                LogoutFinishAllActivity()
                            },1000)
                        }
                        else if (privacyTermsResponce.message != null && privacyTermsResponce.message.length>0) {
                            App.showSnackBar(webViewTerms, "${privacyTermsResponce.message}")
                        }

                    }


                }
                else{
                    App.showLog(strTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }


            }

            override fun onFailure(call: Call<FAQTermsResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                App.showLog(strTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })

    }

}