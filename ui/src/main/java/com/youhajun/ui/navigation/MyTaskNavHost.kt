package com.youhajun.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.youhajun.ui.models.destinations.MyTaskDestination

@Composable
fun MyTaskNavHost(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = MyTaskDestination.First.route, modifier = modifier
    ) {
        firstNav(navController)
        secondNav(navController)
        thirdNav(navController)
        loginNav(navController)
        storeNav(navController)
        selectRoomNav(navController)
        liveRoomNav(navController)
        gptNav(navController)
    }
}