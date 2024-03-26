package com.youhajun.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ResourceProviderUtil @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun string(@StringRes stringRes: Int, vararg args: Any): String = context.getString(stringRes, *args)
}