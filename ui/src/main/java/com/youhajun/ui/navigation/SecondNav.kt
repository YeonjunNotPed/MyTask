package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.destinations.MyTaskDestination
import com.youhajun.ui.screens.SecondScreen

fun NavGraphBuilder.secondNav(navController: NavHostController) {
    composable(MyTaskDestination.Second.routeWithArg) {
        val firstArg = it.arguments?.getString(MyTaskDestination.Second.FIRST_ARG_KEY)
        val secondArg = it.arguments?.getString(MyTaskDestination.Second.SECOND_ARG_KEY)

        if (firstArg != null && secondArg != null) SecondScreen(firstArg, secondArg)
    }
}