package com.youhajun.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProviderUtil @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun string(@StringRes stringRes: Int, vararg args: Any): String = context.getString(stringRes, *args)
}