package com.kody.daawatstaff.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class FAQTermsModel() : Serializable{

    @SerializedName("terms")
    var terms : String = ""

    @SerializedName("faq")
    var faq : String = ""

}
