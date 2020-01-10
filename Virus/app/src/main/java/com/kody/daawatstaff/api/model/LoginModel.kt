package com.kody.daawatstaff.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class LoginModel() : Serializable{

    @SerializedName("token")
    var token : String = ""

    @SerializedName("id")
    var id : String = ""

    @SerializedName("name")
    var name : String = ""

    @SerializedName("phone_number")
    var phone_number : String = ""

    @SerializedName("status")
    var status : String = ""

    @SerializedName("user_type")
    var user_type : String = ""

}
