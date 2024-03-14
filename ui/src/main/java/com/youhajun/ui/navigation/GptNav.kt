package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.models.sideEffects.StoreSideEffect
import com.youhajun.ui.screens.GptScreen
import com.youhajun.ui.screens.StoreScreen

fun NavGraphBuilder.gptNav(navController: NavHostController) {
    composable(MyTaskDestination.Gpt.route) {
        GptScreen {
            when(it) {
                GptSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}