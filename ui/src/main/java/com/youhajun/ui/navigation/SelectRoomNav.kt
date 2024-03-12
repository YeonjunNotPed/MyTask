package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.SelectRoomSideEffect
import com.youhajun.ui.screens.SelectRoomScreen

fun NavGraphBuilder.selectRoomNav(navController: NavHostController) {
    composable(MyTaskDestination.SelectRoom.route) {
        SelectRoomScreen {
            when(it) {
                is SelectRoomSideEffect.Navigation.NavigateToLiveRoom -> {
                    navController.navigate(MyTaskDestination.LiveRoom.navigateLiveRoom(it.idx))
                }

                SelectRoomSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}