package com.youhajun.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.domain.models.enums.GptType
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.chat.GptMessageItemComp
import com.youhajun.ui.components.chat.GptTypeItemComp
import com.youhajun.ui.components.input.GptInputTextField
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.viewModels.GptViewModel

@Composable
fun GptScreen(
    viewModel: GptViewModel = hiltViewModel(),
    onNavigate: (GptSideEffect.Navigation) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (myTaskHeader, lazyColumn, gptInputTextField, floatingLazyRow) = createRefs()

        MyTaskHeader(
            modifier = Modifier.constrainAs(myTaskHeader) {
                top.linkTo(parent.top)
            },
            title = stringResource(id = R.string.header_title_gpt),
            rightIcon = Icons.Filled.ArrowBackIosNew,
            onClickRightIcon = { },
            leftIcon = Icons.Filled.ArrowBackIosNew,
            onClickLeftIcon = { }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.color_e6f4fa))
                .constrainAs(lazyColumn) {
                    top.linkTo(myTaskHeader.bottom)
                    bottom.linkTo(gptInputTextField.top)
                    height = Dimension.fillToConstraints
                }
        ) {
            items(
                items = state.gptMessageList,
                key = { it.idx },
            ) { item ->
                GptMessageItemComp(item)
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
                .constrainAs(floatingLazyRow) {
                    bottom.linkTo(gptInputTextField.top, margin = 16.dp)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = GptType.values(),
                key = { it.type },
            ) { gptType ->
                val isSelected = gptType == state.gptType
                GptTypeItemComp(gptType, isSelected) {
                    viewModel.onClickGptType(it)
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