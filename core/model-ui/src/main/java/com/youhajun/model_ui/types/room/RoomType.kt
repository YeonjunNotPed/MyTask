package com.youhajun.model_ui.types.room

enum class RoomType(val type: String) {
    ONE_TO_ONE("one_to_one"), ONE_TO_MANY("one_to_many");

    companion object {
        fun typeOf(value: String): RoomType {
            return entries.find {
                it.type == value
            } ?: ONE_TO_ONE
        }
    }
}