package com.youhajun.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.chat.GptChannelDrawerSheetComp
import com.youhajun.ui.components.chat.GptMessageItemComp
import com.youhajun.ui.components.chat.GptSelectRoleComp
import com.youhajun.ui.components.chat.GptTypeItemComp
import com.youhajun.ui.components.input.GptInputTextField
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.viewModels.GptViewModel
import kotlinx.coroutines.launch

@Composable
fun GptScreen(
    viewModel: GptViewModel = hiltViewModel(),
    onNavigate: (GptSideEffect.Navigation) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var typingAnimationTrigger by remember { mutableStateOf(false) }
    var typingAnimationTargetIdx by remember { mutableLongStateOf(-1L) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is GptSideEffect.Navigation -> onNavigate.invoke(it)
                is GptSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT)
                    .show()

                GptSideEffect.HideKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                GptSideEffect.DrawerMenuClose -> scope.launch {
                    state.drawerState.close()
                }

                GptSideEffect.DrawerMenuOpen -> scope.launch {
                    state.drawerState.open()
                }

                is GptSideEffect.RunTypingAnimation -> {
                    typingAnimationTargetIdx = it.targetIdx
                    typingAnimationTrigger = !typingAnimationTrigger
                }
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerContent = {
                GptChannelDrawerSheetComp(
                    state.gptChannelList,
                    state.currentGptChannel?.channelIdx,
                    onClickChannel = viewModel::onClickChannel,
                    onClickCreateChannel = viewModel::onClickCreateChannel,
                    onClickDeleteChannel = viewModel::onClickDeleteChannel
                )
            },
            gesturesEnabled = true,
            drawerState = state.drawerState
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        }
                ) {
                    val (myTaskHeader, lazyColumn, selectRoleComp, gptInputTextField, floatingLazyRow) = createRefs()

                    MyTaskHeader(
                        modifier = Modifier.constrainAs(myTaskHeader) {
                            top.linkTo(parent.top)
                        },
                        title = stringResource(id = R.string.header_title_gpt),
                        rightIcon = Icons.Filled.Menu,
                        onClickRightIcon = viewModel::onClickHeaderMenuIcon,
                        leftIcon = Icons.Filled.ArrowBackIosNew,
                        onClickLeftIcon = viewModel::onClickHeaderBackIcon
                    )

                    LazyColumn(
                        reverseLayout = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.color_e6f4fa))
                            .constrainAs(lazyColumn) {
                                top.linkTo(myTaskHeader.bottom)
                                bottom.linkTo(gptInputTextField.top)
                                height = Dimension.fillToConstraints
                            },
                    ) {
                        items(
                            items = state.currentGptMessages,
                            key = { it.idx },
                        ) { item ->
                            val isTarget = item.idx == typingAnimationTargetIdx
                            val role = state.currentGptChannel?.roleOfAi
                            GptMessageItemComp(item, isTarget, typingAnimationTrigger, role)
                        }
                    }

                    if (state.wasSentMessage) {
                        GptSelectRoleComp(
                            modifier = Modifier
                                .constrainAs(selectRoleComp) {
                                    top.linkTo(lazyColumn.top)
                                    end.linkTo(lazyColumn.end)
                                }
                                .padding(10.dp),
                            selectedRole = state.selectedRole,
                            isExpanded = state.isRoleExpanded,
                            roleList = state.roleList,
                            addRoleInput = viewModel.addRoleInputStateOf.value,
                            onAddRoleValueChange = viewModel::onChangedAddRoleInput,
                            onClickExpandIcon = viewModel::onClickRoleExpandIcon,
                            onClickRole = viewModel::onClickRole,
                            onClickDeleteRole = viewModel::onClickDeleteRole,
                            onClickAddRole = viewModel::onClickAddRole,
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 12.dp)
                                .constrainAs(floatingLazyRow) {
                                    bottom.linkTo(gptInputTextField.top, margin = 16.dp)
                                },
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(
                                items = state.gptTypeList,
                                key = { it.type },
                            ) { gptType ->
                                val isSelected = gptType == state.selectedGptType
                                GptTypeItemComp(gptType, isSelected) {
                                    viewModel.onClickGptType(it)
                                }
                            }
                        }
                    }

                    GptInputTextField(
                        modifier = Modifier.constrainAs(gptInputTextField) {
                            bottom.linkTo(parent.bottom)
                        },
                        input = viewModel.gptInputStateOf.value,
                        onValueChange = viewModel::onChangedGptInput,
                        onClickSend = viewModel::onClickSendAnswer
                    )
                }
            }
        }
    }
}