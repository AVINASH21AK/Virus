package com.kody.daawatstaff.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.CommonResponce
import com.kody.daawatcustomer.api.responce.CountryResponce
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R
import com.kody.daawatstaff.api.model.CountryModel
import com.kody.daawatstaff.utils.Constant
import com.kody.daawatstaff.utils.CustomProgressDialog
import com.orhanobut.hawk.Hawk
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class BaseActivity : AppCompatActivity() {

    var strBaseTAG : String = "BaseActivity"

    lateinit var base_ivBack: ImageView
    lateinit var base_tvTitle: TextView
    lateinit var base_rlToolbar: RelativeLayout
    lateinit var base_llSubMainContainer: LinearLayout
    lateinit var base_flMain:FrameLayout
    lateinit var base_ivHomeMenu: ImageView
    lateinit var base_ivAddGuestHome: ImageView
    lateinit var base_tvTitleHome : TextView
    lateinit var base_nevigationMenuList : RelativeLayout
    lateinit var base_closeHome : AppCompatImageView
    lateinit var base_Home : AppCompatTextView
    lateinit var base_ChooseLang: AppCompatTextView
    lateinit var base_FAQ: AppCompatTextView
    lateinit var base_Terms: AppCompatTextView
    lateinit var base_ContactUS: AppCompatTextView
    lateinit var base_ChangePassword: AppCompatTextView
    lateinit var base_Logout: AppCompatTextView
    lateinit var base_viewOutside: View
    lateinit var baseframeLay_ChangeLang: FrameLayout
    lateinit var baseBottomLang_tvArabic: AppCompatTextView
    lateinit var baseBottomLang_tvEnglish: AppCompatTextView
    lateinit var btmsheet_changeLangDone: AppCompatButton
    lateinit var customProgressDialog : CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_base)

        Hawk.init(this).build();
        baseInitialize()
        baseClickEvent()


    }

    fun baseInitialize(){
        customProgressDialog = CustomProgressDialog(this);


        base_tvTitle = findViewById(R.id.base_tvTitle) as TextView
        base_ivBack = findViewById(R.id.base_ivBack) as ImageView
        base_rlToolbar = findViewById(R.id.base_rlToolbar) as RelativeLayout
        base_llSubMainContainer = findViewById(R.id.base_llSubMainContainer) as LinearLayout
        base_flMain=findViewById(R.id.base_flMain) as FrameLayout
        base_ivHomeMenu = findViewById(R.id.base_ivHomeMenu) as ImageView
        base_ivAddGuestHome = findViewById(R.id.base_ivAddGuestHomeMenu) as ImageView
        base_tvTitleHome = findViewById(R.id.base_tvTitleHome) as TextView
        base_nevigationMenuList = findViewById(R.id.navigationMenuListBaseActivity) as RelativeLayout
        base_closeHome = findViewById(R.id.imageViewCloseBaseActivity) as AppCompatImageView
        base_Home = findViewById(R.id.textViewHomeBaseActivity) as AppCompatTextView
        base_ChooseLang = findViewById(R.id.textViewChangeLanguageBaseActivity) as AppCompatTextView
        base_FAQ = findViewById(R.id.textViewFaqBaseActivity) as AppCompatTextView
        base_Terms = findViewById(R.id.textViewTermsAndConditionsBaseActivity) as AppCompatTextView
        base_ContactUS = findViewById(R.id.textViewContactUsBaseActivity) as AppCompatTextView
        base_ChangePassword = findViewById(R.id.textViewChangePassword) as AppCompatTextView
        base_Logout = findViewById(R.id.textViewLogoutBaseActivity) as AppCompatTextView
        base_viewOutside = findViewById(R.id.base_viewOutside) as View
        baseframeLay_ChangeLang=findViewById(R.id.baseframeLay_ChangeLang) as FrameLayout
        baseBottomLang_tvArabic=findViewById(R.id.baseBottomLang_tvArabic) as AppCompatTextView
        baseBottomLang_tvEnglish=findViewById(R.id.baseBottomLang_tvEnglish) as AppCompatTextView
        btmsheet_changeLangDone=findViewById(R.id.btmsheet_changeLangDone) as AppCompatButton



        baseBottomLang_tvArabic.setText(getString(R.string.arabic1) +" "+ getString(R.string.arabic2))


        //by default english is selected
        baseBottomLang_tvEnglish.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)


    }



    fun baseClickEvent(){


        //-- start --  Bottom sheet when slide close outside view
        var bottomSheetCallback_ChangeLang = object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(slideOffset == 0f && base_viewOutside.visibility == View.VISIBLE){
                    base_viewOutside.performClick()
                }
            }

        }
        BottomSheetBehavior.from(baseframeLay_ChangeLang).setBottomSheetCallback(bottomSheetCallback_ChangeLang)


        //-- Close all Bottom Sheet
        base_viewOutside.setOnClickListener(View.OnClickListener{

            base_viewOutside.visibility = View.GONE
            BottomSheetBehavior.from(baseframeLay_ChangeLang).setState(BottomSheetBehavior.STATE_COLLAPSED)

            if(base_nevigationMenuList.visibility==View.VISIBLE)
            {
                base_nevigationMenuList.visibility = View.GONE
            }

        })

        btmsheet_changeLangDone.setOnClickListener(View.OnClickListener {
            base_viewOutside.performClick()
        })
        //-- end --  Bottom sheet when slide close outside view



        //select arabic
        baseBottomLang_tvArabic.setOnClickListener(View.OnClickListener {
            baseBottomLang_tvArabic.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)
            baseBottomLang_tvEnglish.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

        })

        //select english
        baseBottomLang_tvEnglish.setOnClickListener(View.OnClickListener {
            baseBottomLang_tvEnglish.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_true,0)
            baseBottomLang_tvArabic.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

        })




        base_ivBack.setOnClickListener(View.OnClickListener {
           onBackPressed()
        })


        base_ivAddGuestHome.setOnClickListener(View.OnClickListener {
            val goToAddGuest = Intent(this,ActAddGuest::class.java)
            startActivity(goToAddGuest)
        })



        base_ivHomeMenu.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.VISIBLE
            base_viewOutside.visibility = View.VISIBLE
        })


        base_closeHome.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE
        })

        base_Home.setOnClickListener (View.OnClickListener {

            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            if(App.strCurrentScreen != getString(R.string.label_home)){
                val goToChangeLang = Intent(this, ActHome::class.java)
                startActivity(goToChangeLang)
                finishAffinity()
            }

        })

        base_ChooseLang.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            //base_viewOutside.visibility = View.GONE

            BottomSheetBehavior.from(baseframeLay_ChangeLang).setState(BottomSheetBehavior.STATE_EXPANDED)

        })

        base_FAQ.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            if(App.strCurrentScreen != getString(R.string.label_faq)){
                val goToFaq= Intent(this,ActFAQ::class.java)
                startActivity(goToFaq)
                finishAffinity()
            }

        })

        base_Terms.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            if(App.strCurrentScreen != getString(R.string.label_terms_and_condition)){
                val goToTerms = Intent(this,ActTermsAndCondition::class.java)
                startActivity(goToTerms)
                finishAffinity()
            }
        })


        base_ContactUS.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            if(App.strCurrentScreen != getString(R.string.label_support_us)){
                val goToContactUs = Intent(this,ActContactUs::class.java)
                startActivity(goToContactUs)
                finishAffinity()
            }

        })

        base_ChangePassword.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            if(App.strCurrentScreen != getString(R.string.label_change_password)){
                val goToContactUs = Intent(this,ActChangePass::class.java)
                startActivity(goToContactUs)
                finishAffinity()
            }



        })


        base_Logout.setOnClickListener(View.OnClickListener {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.label_logout)
            builder.setMessage(R.string.dialog_logout)

            builder.setPositiveButton("Yes"){dialog, which ->
                base_nevigationMenuList.visibility = View.GONE
                base_viewOutside.visibility = View.GONE

                apiLogout()
            }

            builder.setNegativeButton("No"){dialog, which ->

            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        })


        base_flMain.setOnClickListener(View.OnClickListener {

            if(base_nevigationMenuList.visibility==View.VISIBLE)
            {
                base_nevigationMenuList.visibility = View.GONE
                base_viewOutside.visibility = View.GONE

            }


        })


    }

    //-- Use Style or Below https://stackoverflow.com/questions/27623627/how-can-i-make-the-status-bar-white-with-black-icons
    fun baseShowLightStatusBarTheme(showLightTheme : Boolean){
        if(showLightTheme){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }




    //-- APi Logout
    private fun apiLogout(){

        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }


        val call = App.getRetrofitApiService().logout()



        call.enqueue(object : Callback<CommonResponce> {
            override fun onResponse(call: Call<CommonResponce>, response: Response<CommonResponce>){

                customProgressDialog.dismiss()

                if (response.code() == 200) {

                    val commonResponce = response.body()

                    if (commonResponce == null) {

                        val responseBody = response.errorBody()

                        if (responseBody != null) {
                            try {
                                App.showLog(strBaseTAG, "{------ Api Resonponce Body Null ----------}")
                                App.showLogResponce(strBaseTAG,"${responseBody.string()}")

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    else{
                        App.showLog(strBaseTAG,"----------------------")
                        App.showLogResponce("OUTPUT ${ApiFunction.OP_LOGOUT}", Gson().toJson(response.body()))

                        if (commonResponce.message != null && commonResponce.message.length>0) {
                            App.showSnackBar(base_tvTitle, "${commonResponce.message}")
                        }

                        LogoutFinishAllActivity()

                    }


                }
                else{
                    App.showLog(strBaseTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }


                LogoutFinishAllActivity()

            }

            override fun onFailure(call: Call<CommonResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                LogoutFinishAllActivity()
                App.showLog(strBaseTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })

    }



    //-- Api Country
    fun checkCountryData(showProgress : Boolean) : ArrayList<CountryModel>{
        if(App.arrCountry == null || App.arrCountry.size == 0){
            return apiCountryBase(showProgress)
        }else{
            return App.arrCountry
        }
    }
    private fun apiCountryBase(showProgress : Boolean): ArrayList<CountryModel> {

        App.arrCountry = ArrayList()

        if(!customProgressDialog.isShowing() && showProgress){
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
                                App.showLog(strBaseTAG, "{------ Api Resonponce Body Null ----------}")
                                App.showLogResponce(strBaseTAG,"${responseBody.string()}")

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    else{
                        App.showLog(strBaseTAG,"----------------------")
                        App.showLogResponce("OUTPUT ${ApiFunction.OP_COUNTRY_LIST}", Gson().toJson(response.body()))

                        if (countryResponce.status.equals(ApiFunction.status200)) {

                            if(countryResponce.arrCountry != null && countryResponce.arrCountry.size>0){
                                App.arrCountry.clear()
                                App.arrCountry.addAll(countryResponce.arrCountry)
                            }

                        }

                    }


                }
                else{
                    App.showLog(strBaseTAG, "------------- Server Issue response.code() : ${response.code()} -------------")
                }


            }

            override fun onFailure(call: Call<CountryResponce>, t: Throwable) {
                customProgressDialog.dismiss()
                App.showLog(strBaseTAG, "------------- Api onFailure : ${t.message} -------------")
            }
        })


        return  App.arrCountry

    }


    protected fun LogoutFinishAllActivity() {
        try {

            //--- GET ALL DEVICE RELATED INFO
            var strDEVICE_NAME: String = Hawk.get(Constant.sharedPref_DEVICE_NAME, "")
            var strMODEL_NAME: String = Hawk.get(Constant.sharedPref_MODEL_NAME, "")
            var strDEVICE_TOKEN: String = Hawk.get(Constant.sharedPref_DEVICE_TOKEN, "")
            var strOS_VERSION: String = Hawk.get(Constant.sharedPref_OS_VERSION, "")
            var strUUID: String = Hawk.get(Constant.sharedPref_UUID, "")
            var strIP: String = Hawk.get(Constant.sharedPref_IP, "")


            //-- Clear all shared Preference values
            Hawk.deleteAll()

            //-- SET ALL DEVICE RELATED INFO
            Hawk.put(Constant.sharedPref_ISLOGIN, false)
            Hawk.put(Constant.sharedPref_DEVICE_NAME, strDEVICE_NAME)
            Hawk.put(Constant.sharedPref_MODEL_NAME, strMODEL_NAME)
            Hawk.put(Constant.sharedPref_DEVICE_TOKEN, strDEVICE_TOKEN)
            Hawk.put(Constant.sharedPref_OS_VERSION, strOS_VERSION)
            Hawk.put(Constant.sharedPref_UUID, strUUID)
            Hawk.put(Constant.sharedPref_IP, strIP)


            Handler().postDelayed(Runnable {
                val i1 = Intent(this, ActLogin::class.java)
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                finish()
                startActivity(i1)
            }, 350)


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




        override fun onBackPressed() {

        if(base_viewOutside.visibility == View.VISIBLE)
        {
            base_nevigationMenuList.visibility = View.GONE
            base_viewOutside.visibility = View.GONE
            BottomSheetBehavior.from(baseframeLay_ChangeLang).setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        else {
            super.onBackPressed()
        }
    }

}