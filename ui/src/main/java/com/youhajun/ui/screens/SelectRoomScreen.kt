package com.youhajun.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskTabHeader
import com.youhajun.ui.components.room.RoomPreviewItemComp
import com.youhajun.ui.models.sideEffects.SelectRoomSideEffect
import com.youhajun.ui.viewModels.SelectRoomViewModel

@Composable
fun SelectRoomScreen(
    viewModel: SelectRoomViewModel = hiltViewModel(),
    onNavigate: (SelectRoomSideEffect.Navigation) -> Unit = {}
) {

    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is SelectRoomSideEffect.Toast -> Toast.makeText(
                    context,
                    it.text,
                    Toast.LENGTH_SHORT
                ).show()

                is SelectRoomSideEffect.Navigation -> onNavigate(it)
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.color_161616))) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            MyTaskTabHeader(stringResource(id = R.string.header_title_select_room)) {
                viewModel.onClickHeaderBackIcon()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.roomList,
                    key = { it.idx },
                ) {
                    RoomPreviewItemComp(roomPreviewVo = it, onClickItem = {
                        viewModel.onClickRoom(it.idx)
                    })
                }
            }
        }

        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(horizontal = 20.dp, vertical = 20.dp),
            onClick = { viewModel.onClickCreateRoom() },
            icon = { Icon(Icons.Filled.Edit, null) },
            text = { Text(text = stringResource(id = R.string.create_room)) },
        )
    }
}