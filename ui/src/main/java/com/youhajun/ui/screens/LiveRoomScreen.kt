package com.youhajun.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.room.RoomCallingComp
import com.youhajun.ui.components.room.RoomStageComp
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.viewModels.LiveRoomViewModel

@Composable
fun LiveRoomScreen(
    viewModel: LiveRoomViewModel,
    onNavigate: (LiveRoomSideEffect.Navigation) -> Unit = {}
) {

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) viewModel.onLiveRoomSignalingConnect()
            else viewModel.onLiveRoomPermissionDenied()
        }
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is LiveRoomSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                is LiveRoomSideEffect.LivePermissionLauncher -> {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> it.granted()
                        else -> permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
                is LiveRoomSideEffect.Navigation -> onNavigate(it)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MyTaskHeader(title = stringResource(id = R.string.header_title_select_room)) {
            viewModel.onClickHeaderBackIcon()
        }

        when(state.webRTCSessionType) {
            WebRTCSessionType.Offline,
            WebRTCSessionType.Active,
            WebRTCSessionType.Impossible -> RoomStageComp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state.myVideoTrack,
                state.eglContext
            )
            WebRTCSessionType.Creating,
            WebRTCSessionType.Ready -> RoomCallingComp()
        }
    }
}