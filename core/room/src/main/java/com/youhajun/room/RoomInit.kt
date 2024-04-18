package com.youhajun.room

import android.content.ContentValues
import com.youhajun.room.entity.gpt.GptRoleEntity

object RoomInit {
    const val DB_NAME = "database-my-task"

    fun initGptRoles(): List<ContentValues> {
        return listOf("의사", "변호사", "간호사", "교사", "정치인", "수학선생님", "영어선생님").map {
            ContentValues().apply {
                put(GptRoleEntity.COLUMN_NAME_ROLE, it)
            }
        }
    }
}