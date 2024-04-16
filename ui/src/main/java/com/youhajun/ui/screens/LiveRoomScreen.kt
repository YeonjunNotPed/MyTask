package com.youhajun.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.ui.models.holder.CallMediaStateHolder
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.call.CallBottomComp
import com.youhajun.ui.components.room.RoomCallingComp
import com.youhajun.ui.components.room.RoomStageComp
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.viewModels.LiveRoomViewModel

@Composable
fun LiveRoomScreen(
    viewModel: LiveRoomViewModel = hiltViewModel(),
    onNavigate: (LiveRoomSideEffect.Navigation) -> Unit = {}
) {

    val permissionsLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
            val audioPermissionGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

            if (cameraPermissionGranted && audioPermissionGranted) viewModel.onLiveRoomSignalingConnect()
            else viewModel.onLiveRoomPermissionDenied()
        }
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is LiveRoomSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                is LiveRoomSideEffect.LivePermissionLauncher -> {
                    val cameraPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    val audioPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

                    if (cameraPermissionGranted && audioPermissionGranted) it.granted()
                    else permissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
                }
                is LiveRoomSideEffect.Navigation -> onNavigate(it)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.color_292929))
    ) {
        MyTaskHeader(title = stringResource(id = R.string.header_title_select_room), onClickLeftIcon = viewModel::onClickHeaderBackIcon)

        when (state.webRTCSessionType) {
            WebRTCSessionType.Offline,
            WebRTCSessionType.Creating,
            WebRTCSessionType.Ready,
            WebRTCSessionType.Impossible -> RoomStageComp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state.mySessionInfoVo,
                state.eglContext
            )

            WebRTCSessionType.Active -> RoomCallingComp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            viewModel.onTabCallingScreen()
                        })
                    },
                state.mySessionInfoVo,
                state.partnerSessionInfoVo,
                state.eglContext
            )
        }

        AnimatedVisibility(visible = state.isVisibleBottomAction) {
            val myMediaState = state.mySessionInfoVo?.callMediaStateHolder ?: CallMediaStateHolder()
            CallBottomComp(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp),
                mediaStateVo = myMediaState,
                onClickCallAction = viewModel::onClickCallControlAction
            )
        }
    }
}