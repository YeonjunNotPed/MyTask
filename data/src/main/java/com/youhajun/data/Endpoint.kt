package com.youhajun.data

object Endpoint {

    object ChatGpt {
        const val BASE_URL = "https://api.openai.com/v1/"
        const val CHAT_COMPLETIONS = "chat/completions"
    }

    object Gemini {
        const val BASE_URL = ""
    }

    object Socket {
        const val LIVE_ROOM = "LIVE_ROOM"
    }

    object Auth {
        private const val AUTH_URL = "/api/auth"
        const val GET_REFRESHED_TOKEN = "$AUTH_URL/refresh"
    }

    object Sign {
        private const val SIGN_URL = "/api/sign"
        const val POST_LOGIN = "$SIGN_URL/login"
        const val POST_SOCIAL_LOGIN = "$SIGN_URL/social_login"
    }

    object Store {
        private const val STORE_URL = "/api/store"
        const val GET_PURCHASE_ITEM_INFO = "$STORE_URL/item_info"
        const val POST_PURCHASE_VERIFY = "$STORE_URL/verify"
    }

    object Room {
        private const val ROOM_URL = "/api/room"
        const val GET_ROOM_PREVIEW_INFO = "$ROOM_URL/room_list"
    }

}