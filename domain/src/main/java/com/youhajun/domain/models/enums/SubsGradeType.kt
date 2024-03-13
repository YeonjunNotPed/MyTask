package com.youhajun.domain.models.enums

enum class SubsGradeType(val type: String) {
    NONE("none"), GOLD("gold"), SILVER("silver"), BRONZE("bronze");
    companion object {
        fun typeOf(value: String): SubsGradeType {
            return values().find {
                it.type == value
            } ?: NONE
        }
    }
}