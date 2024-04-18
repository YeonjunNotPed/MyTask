package com.youhajun.model_ui.types.store

enum class PurchaseType(val type: String) {
    IN_APP("onetime"), IN_APP_MULTIPLE("onetime_multiple"), SUBS("subscribe");

    companion object {
        fun typeOf(value: String): PurchaseType {
            return entries.find {
                it.type == value
            } ?: IN_APP
        }
    }
}