package com.youhajun.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.screens.LiveRoomScreen
import com.youhajun.ui.viewModels.LiveRoomViewModel

fun NavGraphBuilder.liveRoomNav(navController: NavHostController) {
    composable(MyTaskDestination.LiveRoom.routeWithArg,
        arguments = listOf(navArgument(MyTaskDestination.LiveRoom.IDX_ARG_KEY) {
            type = NavType.IntType
        })
    ) {
        val idx = it.arguments?.getLong(MyTaskDestination.LiveRoom.IDX_ARG_KEY) ?: -1
        val viewModel = hiltViewModel(
            creationCallback = { factory: LiveRoomViewModel.LiveRoomViewModelFactory ->
                factory.create(idx)
            }
        )
        LiveRoomScreen(viewModel) {
            when(it) {
                LiveRoomSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}