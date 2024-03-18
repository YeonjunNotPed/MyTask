package com.youhajun.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.youhajun.data.models.entity.EntityTable
import com.youhajun.data.models.entity.gpt.GptRoleEntity
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