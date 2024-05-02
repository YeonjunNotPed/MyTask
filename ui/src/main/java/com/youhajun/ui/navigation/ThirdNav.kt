package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.youhajun.ui.destinations.MyTaskDestination
import com.youhajun.ui.screens.ThirdScreen

fun NavGraphBuilder.thirdNav(navController: NavHostController) {
    composable(
        MyTaskDestination.Third.routeWithArg,
        arguments = listOf(navArgument(MyTaskDestination.Third.FIRST_OPTIONAL_ARG_KEY) {
            defaultValue = MyTaskDestination.Third.FIRST_OPTIONAL_ARG_DEFAULT
            type = NavType.IntType
        }),
    ) {
        it.arguments?.getInt(MyTaskDestination.Third.FIRST_OPTIONAL_ARG_KEY)?.let { firstArg ->
            ThirdScreen(firstArg)
        }
    }
}