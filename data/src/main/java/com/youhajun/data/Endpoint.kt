package com.youhajun.data

object Endpoint {

    object AUTH {
        private const val AUTH_URL = "/api/auth"
        const val GET_REFRESHED_TOKEN = "$AUTH_URL/refresh"
    }

    object SIGN {
        private const val SIGN_URL = "/api/sign"
        const val POST_LOGIN = "$SIGN_URL/login"
        const val POST_SOCIAL_LOGIN = "$SIGN_URL/social_login"
    }

    object STORE {
        private const val STORE_URL = "/api/store"
        const val GET_PURCHASE_ITEM_INFO = "$STORE_URL/item_info"
        const val POST_PURCHASE_VERIFY = "$STORE_URL/verify"
    }
}