package com.kody.daawatcustomer.api

import com.kody.daawatcustomer.api.responce.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @Multipart
    @POST("login")
    fun login(
        @PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<LoginResponce>


    //@Multipart
    @GET("country_list")
    fun country_list(
        //@PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CountryResponce>

    @Multipart
    @POST("qr_verify")
    fun qr_verify(
        @PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CommonResponce>

    @Multipart
    @POST("add_guest")
    fun add_guest(
        @PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CommonResponce>

    @Multipart
    @POST("contactus")
    fun contactus(
        @PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CommonResponce>

    //@Multipart
    @GET("page/all")
    fun page_all(
        //@PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<FAQTermsResponce>

    //@Multipart
    @GET("logout")
    fun logout(
        //@PartMap partMap : Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CommonResponce>




}