package com.kody.daawatstaff.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CountryModel() : Serializable{

    @SerializedName("id")
    var id : String = ""

    @SerializedName("name")
    var name : String = ""

    @SerializedName("code")
    var code : String = ""


}
