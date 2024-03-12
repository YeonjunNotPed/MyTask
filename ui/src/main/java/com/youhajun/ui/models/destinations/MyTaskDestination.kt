package com.youhajun.ui.models.destinations

sealed class MyTaskDestination {
    abstract val route: String
    companion object {
        fun isShowBottomNavScreens(route: String?) = when (route) {
            First.route, Second.routeWithArg, Third.routeWithArg -> true
            else -> false
        }
    }
    object First : MyTaskDestination() {
        override val route: String = "first"
    }
    object Second : MyTaskDestination() {
        override val route: String = "second"

        const val FIRST_ARG_KEY = "firstArg"
        const val SECOND_ARG_KEY = "secondArg"
        private const val PATH = "/{$FIRST_ARG_KEY}/{$SECOND_ARG_KEY}"
        val routeWithArg = "$route$PATH"
    }

    object Third : MyTaskDestination() {
        override val route: String = "third"

        const val FIRST_OPTIONAL_ARG_KEY = "firstArg"
        const val FIRST_OPTIONAL_ARG_DEFAULT = 0
        private const val PATH = "?{$FIRST_OPTIONAL_ARG_KEY}={$FIRST_OPTIONAL_ARG_KEY}"
        val routeWithArg = "$route$PATH"
    }

    object Store : MyTaskDestination() {
        override val route: String = "store"
    }

    object Login : MyTaskDestination() {
        override val route: String = "login"
    }

    object SelectRoom : MyTaskDestination() {
        override val route: String = "selectRoom"
    }
    object LiveRoom : MyTaskDestination() {
        override val route: String = "liveRoom"

        const val IDX_ARG_KEY = "idxArg"
        private const val PATH = "/{$IDX_ARG_KEY}"
        val routeWithArg = "$route$PATH"

        fun navigateLiveRoom(idx:Int):String = "$route/$idx"
    }
}