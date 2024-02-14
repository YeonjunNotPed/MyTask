package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.screens.LoginScreen

fun NavGraphBuilder.loginNav(navController: NavHostController) {
    composable(MyTaskDestination.Login.route) {
        LoginScreen()
    }
}