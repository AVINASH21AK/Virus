package com.kody.daawatcustomer.api.responce

import com.google.gson.annotations.SerializedName
import com.kody.daawatstaff.api.model.LoginModel

class LoginResponce : CommonResponce() {

    @SerializedName("data")
    lateinit var data : LoginModel

}