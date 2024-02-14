package com.youhajun.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.youhajun.ui.models.destinations.BottomNavDestination

@Composable
fun MyTaskBottomNavigation(
    bottomScreens: List<BottomNavDestination>,
    onTabSelected: (BottomNavDestination) -> Unit,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color.White,
    ) {
        bottomScreens.forEach {
            val isSelected = currentRoute == it.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(it) },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = null)
                },
                label = {
                    Text(stringResource(id = it.labelStringRes))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}