package com.youhajun.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.youhajun.ui.models.destinations.MyTaskDestination

@Composable
fun FirstScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(onClick = {
            navController.navigate(MyTaskDestination.Login.route)
        }) {
            Text("로그인")
        }

        Button(onClick = {
            navController.navigate(MyTaskDestination.Store.route)
        }) {
            Text("스토어")
        }
    }
}