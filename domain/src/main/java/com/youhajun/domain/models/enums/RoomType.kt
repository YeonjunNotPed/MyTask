package com.youhajun.domain.models.enums

enum class RoomType(val type: String) {
    ONE_TO_ONE("one_to_one"), ONE_TO_MANY("one_to_many");

    companion object {
        fun typeOf(value: String): RoomType {
            return values().find {
                it.type == value
            } ?: ONE_TO_ONE
        }
    }
}