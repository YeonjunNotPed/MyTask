package com.youhajun.model_ui.types.store

enum class SubsGradeType(val type: String) {
    NONE("none"), GOLD("gold"), SILVER("silver"), BRONZE("bronze");
    companion object {
        fun typeOf(value: String): SubsGradeType {
            return entries.find {
                it.type == value
            } ?: NONE
        }
    }
}