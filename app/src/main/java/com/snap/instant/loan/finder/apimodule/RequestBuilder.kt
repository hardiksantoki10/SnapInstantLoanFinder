package com.snap.instant.loan.finder.apimodule


import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


object RequestBuilder {


    //"Q0ZzNHFZMjl0TFBIZGluRjFiM1JsY3k1amNtVmhkRzl5TG0xaGEyVnlMbkYxYjNScFpua3ZWEIG3RTNPMko5"
    private fun getSavedToken(): String {
        return Hawk.get(UserLoginDetail.REMEMBER_TOKEN, "")
    }

    fun getCommonParametersHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            this[PARAMS.TOKEN] = getSavedToken()
        }
    }

    private fun getCommonParametersHashMapRequestBody(): HashMap<String, RequestBody> {
        val hashMap = HashMap<String, RequestBody>()
        hashMap[PARAMS.TOKEN] = toRequestBody(getSavedToken())
        return hashMap
    }


    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }



    object PARAMS {
        var LIMIT = "limit"
        var G_ID = "g_id"
        var G_TOKEN_ID = "g_token_id"
        var G_DISPLAY_NAME = "g_display_name"
        var G_EMAIL = "g_email"
        var G_GIVEN_NAME = "g_given_name"
        var G_FAMILY_NAME = "g_family_name"
        var FAVOURITE_DATA = "favourite_data"
        var G_PHOTO = "g_photo"
        var PAGE = "page"
        var ORDER_BY = "order_by"
        var ORDER_BY_TYPE = "order_by_type"
        var CHILD_LIMIT = "child_limit"
        var WITH_CHILD = "with_child"
        var WHERE = "where"
        var WITH = "with"
        var DAY = "day"
        var IS_FAVOURITE = "is_favourite"
        var WITH_CONTENT = "with_content"
        var ORDER_BY_ARRAY = "order_by_array"
        var USERLOGIN_ID = "userlogin_id"
        var LIKE_ONLY = "like_only"
        var TEMPLATE_ID = "template_id"
        var TEMPLATE_NAME = "template_name"
        var APP_DB_ID = "app_db_id"
        var SERVER_ID = "server_id"
        var IS_BLANK = "is_blank"
        var IS_EDITED = "is_edited"
        var PREVIEW = "preview"
        var ZIP = "zip"
        var USER_LOGIN = "user_login"
        var TOKEN = "token"
        var VERSION = "version"
        var UNIQUE_TOKEN = "unique_token"
        var OLD_UNIQUE_TOKEN = "old_unique_token"
        var LAST_OPENED_AT = "last_opened_at"
        var PURCHASE_DATA = "purchase_data"
        var PURCHASES = "purchases"
        var COUNTRY_NAME = "country_name"
        var COUNTRY_CODE = "country_code"
        var STATE_CODE = "state_code"
        var STATE_NAME = "state_name"
        var CITY_NAME = "city_name"
        var APP_VERSION = "app_version"
        var OS_VERSION = "os_version"
        var MANUFACTURER = "manufacturer"
        var MODEL = "model"
        var BRAND = "brand"
        var DATA_SIZE = "data_size"
        var ONESIGNAL_PLAYER_ID = "onesignal_player_id"
        var LANGUAGE_CODE = "language_code"
        var LANGUAGE_NAME = "language_name"
        var APP_LANGUAGE_NAME = "app_language_name"
        var APP_LANGUAGE_CODE = "app_language_code"
        var SDK_VERSION = "sdk_version"
        var PRO = "pro"
        var RESOLUTION = "resolution"
        var RAM = "ram"
        var FREE_MEMORY = "free_memory"

        var MODULES = "modules"
        var STATUS = "status"
        var PRIMARYGRAPHICSCATEGORY_ID = "primarygraphicscategory_id"
        var PRIMARYBACKGROUNDCATEGORY_ID = "primarybackgroundcategory_id"
        var ID = "id"
        var SCHEDULED = "scheduled"
        var FEATURED = "featured"
        var PURCHASE_ONLY = "purchase_only"
        var PRODUCT_ID = "product_id"
        var ORDER_ID = "order_id"
        var PURCHASE_TIME = "purchase_time"
        var AUTO_RENEW = "auto_renew"

        var BY = "by"
        var TYPE = "type"
        var DELETED_AT = "deleted_at"
    }

    object ApiEndPoints {
        var LOGIN = "login"
        var REGISTER = "register"
    }
}