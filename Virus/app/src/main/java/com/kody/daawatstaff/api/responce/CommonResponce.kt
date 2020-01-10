package com.kody.daawatcustomer.api.responce

import com.google.gson.annotations.SerializedName



open class CommonResponce {

    @SerializedName("success")
    var success : String = ""

    @SerializedName("status")
    var status : String = ""

    @SerializedName("message")
    var message : String = ""

}