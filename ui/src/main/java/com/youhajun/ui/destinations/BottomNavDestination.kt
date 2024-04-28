package com.youhajun.ui.destinations

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.youhajun.ui.R
import kotlinx.collections.immutable.persistentListOf

interface BottomNavDestination {
    val route: String
    val icon: ImageVector
    @get: StringRes val labelStringRes: Int
}
object FirstView : BottomNavDestination {
    override val route: String = MyTaskDestination.First.route
    override val icon = Icons.Filled.AccountCircle
    override val labelStringRes: Int = R.string.label_first_screen
}
object SecondView : BottomNavDestination {
    override val route: String = MyTaskDestination.Second.routeWithArg
    override val icon = Icons.Filled.AccountCircle
    override val labelStringRes: Int = R.string.label_second_screen
}
object ThirdView : BottomNavDestination {
    override val route: String = MyTaskDestination.Third.routeWithArg
    override val icon = Icons.Filled.AccountCircle
    override val labelStringRes: Int = R.string.label_third_screen
}

val MainBottomTabScreens = persistentListOf(FirstView, SecondView, ThirdView)