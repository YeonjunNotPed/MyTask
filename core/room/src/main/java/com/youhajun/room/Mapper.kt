package com.youhajun.room

interface ToEntity<out Entity, in D> {
    fun D.toEntity(): Entity
}

interface ToDto<in Entity, out D> {
    fun Entity.toDto(): D
}
