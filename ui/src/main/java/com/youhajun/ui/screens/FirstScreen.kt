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
import com.youhajun.ui.destinations.MyTaskDestination

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
            navController.navigate(MyTaskDestination.Login.route) {
                popUpTo(MyTaskDestination.First.route) {
                    inclusive = false
                    saveState = true
                }
            }
        }) {
            Text("로그인")
        }

        Button(onClick = {
            navController.navigate(MyTaskDestination.Store.route) {
                popUpTo(MyTaskDestination.First.route) {
                    inclusive = false
                    saveState = true
                }
            }
        }) {
            Text("스토어")
        }

        Button(onClick = {
            navController.navigate(MyTaskDestination.SelectRoom.route) {
                popUpTo(MyTaskDestination.First.route) {
                    inclusive = false
                    saveState = true
                }
            }
        }) {
            Text("룸 선택")
        }

        Button(onClick = {
            navController.navigate(MyTaskDestination.Gpt.route) {
                popUpTo(MyTaskDestination.First.route) {
                    inclusive = false
                    saveState = true
                }
            }
        }) {
            Text("GPT 채팅")
        }
    }
}