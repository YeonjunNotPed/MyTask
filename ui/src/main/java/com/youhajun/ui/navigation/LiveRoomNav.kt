package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.screens.LiveRoomScreen

fun NavGraphBuilder.liveRoomNav(navController: NavHostController) {
    composable(MyTaskDestination.LiveRoom.routeWithArg,
        arguments = listOf(navArgument(MyTaskDestination.LiveRoom.IDX_ARG_KEY) {
            type = NavType.LongType
        })
    ) {
        LiveRoomScreen {
            when(it) {
                LiveRoomSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}