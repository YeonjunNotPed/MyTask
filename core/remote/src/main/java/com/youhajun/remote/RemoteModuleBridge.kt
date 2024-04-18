package com.youhajun.remote

interface RemoteModuleBridge {
    fun fetchAccessToken(): String?
    fun fetchAndSaveNewToken(): String?
}