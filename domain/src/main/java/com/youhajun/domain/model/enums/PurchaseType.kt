package com.youhajun.domain.model.enums

enum class PurchaseType(val type: String) {
    IN_APP("onetime"), IN_APP_MULTIPLE("onetime_multiple"), SUBS("subscribe");

    companion object {
        fun typeOf(value: String): PurchaseType {
            return values().find {
                it.type == value
            } ?: IN_APP
        }
    }
}