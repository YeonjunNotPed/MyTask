package com.youhajun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.chat.GptMessageItemComp
import com.youhajun.ui.components.input.GptInputTextField
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.viewModels.GptViewModel

@Preview(showBackground = true)
@Composable
fun ChatGptScreen(
    viewModel: GptViewModel = hiltViewModel(),
    onNavigate: (GptSideEffect.Navigation) -> Unit = {}
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MyTaskHeader(
            title = stringResource(id = R.string.header_title_gpt),
            rightIcon = Icons.Filled.ArrowBackIosNew,
            onClickRightIcon = {

            },
            leftIcon = Icons.Filled.ArrowBackIosNew,
            onClickLeftIcon = {

            })

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.color_cce8f4))
                .weight(1f),
        ) {
            items(
                items = state.gptMessageList,
                key = { it.idx },
            ) {
                GptMessageItemComp(it)
            }
        }

        GptInputTextField(
            viewModel.gptInputStateOf.value,
            viewModel::onChangedGptInput
        ) {
            viewModel.onClickSendAnswer()
        }
    }
}