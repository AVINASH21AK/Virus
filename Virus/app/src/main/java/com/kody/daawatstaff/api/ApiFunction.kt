package com.kody.daawatcustomer.api


class ApiFunction {

    companion object{


        //var strBaseUrl : String = "http://192.168.1.28/dawat/public/api/staff/"
        var strBaseUrl : String = "http://daawat.kodytechnolab.co.in/public/api/staff/"

        var OP_LOGIN : String = "login"
        var OP_RESET_PASSWORD : String = "reset_password"
        var OP_INVITE : String = "invite"
        var OP_FORGOT_PASSWORD : String = "forgot_password"
        var OP_COUNTRY_LIST : String = "country_list"
        var OP_SEND_INVITE : String = "send_invite"
        var OP_INVITED_USERS : String = "invited_users"
        var OP_UNIVITE : String = "univite"
        var OP_QR_CODE : String = "qr_code"
        var OP_ADD_GUEST : String = "add_guest"
        var OP_CONTACTUS : String = "contactus"
        var OP_PAGE_ALL : String = "page_all"
        var OP_LOGOUT : String = "logout"


        //-- Api Success / Error
        var status200 = "200"
        var status201 = "201"
        var status202 = "202"
        var status401 = "401"


        //--- Parameters
        var PARM_NAME : String = "name"
        var PARM_EMAIL : String = "email"
        var PARM_PHONE_NUMBER : String = "phone_number"
        var PARM_COUNTRY_CODE : String = "country_code"
        var PARM_PASSWORD : String = "password"
        var PARM_CONFIRM_PASSWORD : String = "confirm_password"
        var PARM_MESSAGE : String = "message"
        var PARM_DEVICE_NAME : String = "device_name"
        var PARM_MODEL_NAME : String = "model_name"
        var PARM_DEVICE_TOKEN : String = "device_token"
        var PARM_OS_VERSION : String = "os_version"
        var PARM_UUID : String = "uuid"
        var PARM_IP : String = "ip"
        var PARM_DEVICE_TYPE : String = "device_type"
        var PARM_IS_FORWARD : String = "is_forward"
        var PARM_TOTAL_INVITES : String = "total_invites"
        var PARM_GUEST_ID : String = "guest_id"
        var PARM_QR_CODE : String = "qr_code"
    }

}