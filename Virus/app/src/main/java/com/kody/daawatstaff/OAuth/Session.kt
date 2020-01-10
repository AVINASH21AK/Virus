package com.kody.daawatstaff.OAuth

interface Session {

     fun saveLogin(login: Boolean?)


     fun isLoggedIn(): Boolean

}