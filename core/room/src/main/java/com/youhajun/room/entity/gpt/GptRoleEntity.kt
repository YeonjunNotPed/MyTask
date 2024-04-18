package com.youhajun.room.entity.gpt

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youhajun.model_data.gpt.GptRoleEntityDto
import com.youhajun.room.EntityTable
import com.youhajun.room.ToDto
import com.youhajun.room.ToEntity

@Entity(tableName = EntityTable.GPT_ROLE)
data class GptRoleEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_ROLE) val role: String,
) {
    companion object : ToDto<GptRoleEntity, GptRoleEntityDto>, ToEntity<GptRoleEntity, GptRoleEntityDto> {
        const val COLUMN_NAME_ROLE = "role"

        override fun GptRoleEntity.toDto(): GptRoleEntityDto = GptRoleEntityDto(
            role
        )

        override fun GptRoleEntityDto.toEntity(): GptRoleEntity = GptRoleEntity(
            role
        )
    }
}