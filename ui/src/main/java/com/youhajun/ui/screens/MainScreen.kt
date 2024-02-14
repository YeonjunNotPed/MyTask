package com.youhajun.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.youhajun.ui.models.destinations.MainBottomTabScreens
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.components.MyTaskBottomNavigation
import com.youhajun.ui.navigation.MyTaskNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (MyTaskDestination.isShowBottomNavScreens(currentRoute)) {
                MyTaskBottomNavigation(
                    bottomScreens = MainBottomTabScreens,
                    onTabSelected = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentRoute = currentRoute
                )
            }
        }) { paddingValues ->
        MyTaskNavHost(navController, Modifier.padding(paddingValues = paddingValues))
    }
}