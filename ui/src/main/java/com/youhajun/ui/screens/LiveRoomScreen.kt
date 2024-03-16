package com.youhajun.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.viewModels.LiveRoomViewModel

@Composable
fun LiveRoomScreen(
    viewModel: LiveRoomViewModel = hiltViewModel(),
    idx:Long,
    onNavigate: (LiveRoomSideEffect.Navigation) -> Unit = {}
) {

    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is LiveRoomSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                is LiveRoomSideEffect.Navigation -> onNavigate(it)
            }
        }

        viewModel.setRoomIdx(idx)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyTaskHeader(title = stringResource(id = R.string.header_title_select_room)) {
            viewModel.onClickHeaderBackIcon()
        }

    }
}