package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.destinations.MyTaskDestination
import com.youhajun.model_ui.sideEffects.GptSideEffect
import com.youhajun.ui.screens.GptScreen

fun NavGraphBuilder.gptNav(navController: NavHostController) {
    composable(MyTaskDestination.Gpt.route) {
        GptScreen {
            when(it) {
                GptSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}