package com.youhajun.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.youhajun.room.EntityTable
import com.youhajun.room.entity.gpt.GptRoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GptRoleDao {
    @Query("SELECT * FROM ${EntityTable.GPT_ROLE}")
    fun getAllRoles(): Flow<List<GptRoleEntity>>

    @Query("DELETE FROM ${EntityTable.GPT_ROLE} WHERE role = :role")
    suspend fun deleteRole(role: String)

    @Insert(onConflict = REPLACE)
    suspend fun insertRole(gptRoleEntity: GptRoleEntity)
}