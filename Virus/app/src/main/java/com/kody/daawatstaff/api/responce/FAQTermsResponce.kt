package com.kody.daawatcustomer.api.responce

import com.google.gson.annotations.SerializedName
import com.kody.daawatstaff.api.model.FAQTermsModel

class FAQTermsResponce : CommonResponce() {

    @SerializedName("data")
    var data = FAQTermsModel()

}