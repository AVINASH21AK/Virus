package com.kody.daawatstaff.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.kody.daawatcustomer.api.ApiFunction
import com.kody.daawatcustomer.api.responce.CommonResponce
import com.kody.daawatstaff.utils.App
import com.kody.daawatstaff.R
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActHome : BaseActivity(), QRCodeReaderView.OnQRCodeReadListener {

    var strTAG : String = "ActHome"

    private val REQUEST = 112
    lateinit var qrCodeReaderView: QRCodeReaderView

    lateinit var homebtmsheet_edtQRCode: TextInputEditText
    lateinit var btnApplyBottomAddNumber: AppCompatButton
    lateinit var btmsheet_isSuccess_btn: AppCompatButton
    lateinit var frameLay_QrIsSuccess: FrameLayout
    lateinit var btmsheet_isSuccess_img: ImageView
    lateinit var btmsheet_isSuccess_tv: TextView
    lateinit var setting_bottom_sheet_touch_outside_sheet:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewGroup.inflate(this, R.layout.act_home, base_llSubMainContainer)
        baseShowLightStatusBarTheme(true)
        base_tvTitle.visibility = View.GONE
        base_ivBack.visibility = View.GONE
        base_ivHomeMenu.visibility = View.VISIBLE
        base_ivAddGuestHome.visibility = View.VISIBLE
        base_tvTitleHome.visibility = View.VISIBLE



        checkPermission()
        initialize()
        clickEvenet()



    }

    override fun onResume() {
        super.onResume()
        App.strCurrentScreen = getString(R.string.label_home)
        qrCodeReaderView.startCamera()
    }

    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("TAG", "IN if Build.Version.sdk_int")
            val permission = arrayOf(
                android.Manifest.permission.CAMERA
            )

            if (!hasPermissions(this, *permission)) {
                Log.d("TAG", "@@@ IN IF hasPermissions")
                ActivityCompat.requestPermissions(this, permission, REQUEST)
            } else {
                Log.d("TAG", "@@@ IN ELSE hasPermissions")
            }
        }
        else
        {

        }
    }

    private fun initialize(){
        qrCodeReaderView = findViewById(R.id.qrdecoderview) as QRCodeReaderView

        homebtmsheet_edtQRCode = findViewById(R.id.homebtmsheet_edtQRCode) as TextInputEditText
        btnApplyBottomAddNumber = findViewById(R.id.btnApplyBottomAddNumber) as AppCompatButton
        btmsheet_isSuccess_btn = findViewById(R.id.btmsheet_isSuccess_btn) as AppCompatButton
        btmsheet_isSuccess_tv=findViewById(R.id.btmsheet_isSuccess_tv) as TextView
        btmsheet_isSuccess_img=findViewById(R.id.btmsheet_isSuccess_img) as ImageView
        frameLay_QrIsSuccess=findViewById(R.id.frameLay_QrIsSuccess) as FrameLayout
        setting_bottom_sheet_touch_outside_sheet=findViewById(R.id.setting_bottom_sheet_touch_outside_sheet) as View

        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setFrontCamera();
        qrCodeReaderView.setBackCamera();
    }

    private fun clickEvenet(){


        //-- Close QR Code Result and Open QRCode IsSuccess Bottom Sheet
        btnApplyBottomAddNumber.setOnClickListener(View.OnClickListener {

            if(homebtmsheet_edtQRCode.text != null && homebtmsheet_edtQRCode.text.toString().length>0){
                apiQRVerify(this, homebtmsheet_edtQRCode.text.toString())
            }else{
                App.showSnackBar(homebtmsheet_edtQRCode, "Please enter valid QR Code")
            }


            /*var i1 = Intent(this, ActVerification::class.java)
            startActivity(i1)*/

        })

        //-- Only Close Bottom Sheet
        btmsheet_isSuccess_btn.setOnClickListener(View.OnClickListener {
            setting_bottom_sheet_touch_outside_sheet.performClick()
        })




        //-- 2. Bottom sheet when slide close outside view
        var bottomSheetCallback_IsSuccess = object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(slideOffset == 0f && setting_bottom_sheet_touch_outside_sheet.visibility == View.VISIBLE){
                    setting_bottom_sheet_touch_outside_sheet.performClick()
                }
            }

        }
        BottomSheetBehavior.from(frameLay_QrIsSuccess).setBottomSheetCallback(bottomSheetCallback_IsSuccess)




        //-- Close all Bottom Sheet
        setting_bottom_sheet_touch_outside_sheet.setOnClickListener(View.OnClickListener{

            qrCodeReaderView.visibility = View.GONE
            qrCodeReaderView.visibility = View.VISIBLE
            qrCodeReaderView.startCamera()


            setting_bottom_sheet_touch_outside_sheet.visibility=View.GONE
            BottomSheetBehavior.from(frameLay_QrIsSuccess).setState(BottomSheetBehavior.STATE_COLLAPSED)

        })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST -> {
                var allPermGranted = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(this, "Permissions not granted: " + permissions[i], Toast.LENGTH_LONG).show()

                        Log.d("ActHome","Permissions not granted: " + permissions[i])
                        App.showSnackBar(base_tvTitle,"Permissions not granted: "  )
                        allPermGranted = false
                        //finish()
                        break
                    }
                }
                if (allPermGranted)
                {

                    qrCodeReaderView.visibility = View.GONE
                    qrCodeReaderView.visibility = View.VISIBLE

                    //qrCodeReaderView.startCamera()

                    Log.d("TAG", "@@@ PERMISSIONS Granted")
                } else
                {
                    Log.d("TAG", "@@@ PERMISSIONS Denied")
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show()
                    finishAffinity()

                }
            }
        }


    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }



    //-- QR Code Scanner
    override fun onQRCodeRead(strOTP: String, points: Array<PointF>) {

        qrCodeReaderView.stopCamera()
        apiQRVerify(this, strOTP)
    }



    override fun onPause() {
        super.onPause()
        qrCodeReaderView.stopCamera()
    }


    override fun onBackPressed() {

        if(base_viewOutside.visibility == View.VISIBLE)
        {
            base_nevigationMenuList.visibility = View.GONE
            BottomSheetBehavior.from(frameLay_QrIsSuccess).setState(BottomSheetBehavior.STATE_COLLAPSED)
            base_viewOutside.visibility = View.GONE
        }
        else {
            super.onBackPressed()
        }
    }



    private fun apiQRVerify(context : Context, strOTP: String){

        if(!customProgressDialog.isShowing()){
            customProgressDialog.show();
        }


        var hashMap : HashMap<String, RequestBody> = HashMap()
        hashMap.put(ApiFunction.PARM_QR_CODE, App.createPartFromString(strOTP))

        val call = App.getRetrofitApiService().qr_verify(hashMap)

        //-- Print Responce
        var apiParameter : String = ""
        for (entry in hashMap) {
            val key = entry.key
            val value = entry.value
            apiParameter = apiParameter + "\n" + (key+ " : " + App.bodyToString(value))
        }
        App.showLog(strTAG, "${ApiFunction.OP_QR_CODE} : $apiParameter")


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
                            setting_bottom_sheet_touch_outside_sheet.visibility=View.VISIBLE
                            BottomSheetBehavior.from(frameLay_QrIsSuccess).setState(BottomSheetBehavior.STATE_EXPANDED)

                            btmsheet_isSuccess_tv.setText(commonResponce.message)
                            btmsheet_isSuccess_img.setImageDrawable(resources.getDrawable(R.drawable.success))

                            //-- Invite image
                            /*Glide
                                .with(context)
                                .load(commonResponce)
                                .fitCenter()
                                .placeholder(R.mipmap.ic_launcher)
                                .into(home_ivInvite)*/
                        }
                        else if (commonResponce.status.equals(ApiFunction.status201)) {
                            setting_bottom_sheet_touch_outside_sheet.visibility=View.VISIBLE
                            BottomSheetBehavior.from(frameLay_QrIsSuccess).setState(BottomSheetBehavior.STATE_EXPANDED)

                            btmsheet_isSuccess_tv.setText(commonResponce.message)
                            btmsheet_isSuccess_img.setImageDrawable(resources.getDrawable(R.drawable.close_red))

                        }
                        else if (commonResponce.status.equals(ApiFunction.status401)) {
                            App.showSnackBar(btnApplyBottomAddNumber, "${commonResponce.message}")

                            Handler().postDelayed(Runnable {
                                LogoutFinishAllActivity()
                            },1000)
                        }
                        else if (commonResponce.message != null && commonResponce.message.length>0) {
                            App.showSnackBar(btnApplyBottomAddNumber, "${commonResponce.message}")
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