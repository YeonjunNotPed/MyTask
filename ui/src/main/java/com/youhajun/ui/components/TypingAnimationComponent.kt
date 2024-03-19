package com.youhajun.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun TypingAnimationComponent(
    fullText: String,
    trigger: Boolean,
    typingSpeedMillis: Long = 50L,
    textComposable: @Composable (String) -> Unit
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(trigger) {
        displayedText = ""
        fullText.forEach { char ->
            displayedText += char
            delay(typingSpeedMillis)
        }
    }

    textComposable(displayedText)
}