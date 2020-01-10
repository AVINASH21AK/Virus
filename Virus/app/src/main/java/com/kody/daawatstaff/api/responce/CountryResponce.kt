package com.kody.daawatcustomer.api.responce

import com.google.gson.annotations.SerializedName
import com.kody.daawatstaff.api.model.CountryModel

class CountryResponce : CommonResponce() {

    @SerializedName("data")
    var arrCountry : ArrayList<CountryModel> = ArrayList()

}