package com.youhajun.ui.models.destinations

sealed class MyTaskDestination(val route: String) {
    companion object {
        fun isShowBottomNavScreens(route: String?) = when (route) {
            First.route, Second.routeWithArg, Third.routeWithArg -> true
            else -> false
        }
    }
    object First : MyTaskDestination("first")
    object Second : MyTaskDestination("second") {
        const val FIRST_ARG_KEY = "firstArg"
        const val SECOND_ARG_KEY = "secondArg"
        private const val PATH = "/{$FIRST_ARG_KEY}/{$SECOND_ARG_KEY}"
        val routeWithArg = "$route$PATH"
    }

    object Third : MyTaskDestination("third") {
        const val FIRST_OPTIONAL_ARG_KEY = "firstArg"
        const val FIRST_OPTIONAL_ARG_DEFAULT = 0
        private const val PATH = "?{$FIRST_OPTIONAL_ARG_KEY}={$FIRST_OPTIONAL_ARG_KEY}"
        val routeWithArg = "$route$PATH"
    }

    object Store : MyTaskDestination("store")

    object Login : MyTaskDestination("login")
}