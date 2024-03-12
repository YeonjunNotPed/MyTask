package com.youhajun.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.StoreSideEffect
import com.youhajun.ui.screens.StoreScreen

fun NavGraphBuilder.storeNav(navController: NavHostController) {
    composable(MyTaskDestination.Store.route) {
        StoreScreen {
            when(it) {
                StoreSideEffect.Navigation.NavigateToStoreHistory -> {

                }

                StoreSideEffect.Navigation.NavigateUp -> navController.navigateUp()
            }
        }
    }
}