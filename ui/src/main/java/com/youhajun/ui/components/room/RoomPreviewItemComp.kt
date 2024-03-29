package com.youhajun.ui.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.domain.models.vo.room.RoomPreviewVo
import com.youhajun.ui.R

@Composable
fun RoomPreviewItemComp(
    roomPreviewVo: RoomPreviewVo,
    onClickItem: (RoomPreviewVo) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClickItem.invoke(roomPreviewVo) }
            .border(1.dp, Color.White, RoundedCornerShape(12.dp))
            .padding(0.5.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .wrapContentSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = roomPreviewVo.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.W800,
                maxLines = 2,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(top = 4.dp)
            )

            Text(
                text = stringResource(id = R.string.room_preview_create_at, roomPreviewVo.createAt),
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = Color.Black,
                modifier = Modifier.wrapContentSize()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = roomPreviewVo.content,
            fontSize = 14.sp,
            fontWeight = FontWeight.W800,
            minLines = 2,
            maxLines = 2,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                id = R.string.room_preview_matching_standard_number,
                roomPreviewVo.matchingStandardNumber,
            ),
            fontSize = 11.sp,
            fontWeight = FontWeight.W400,
            color = Color.Black,
            modifier = Modifier.wrapContentSize()
        )

        Text(
            text = stringResource(
                id = R.string.room_preview_current_people,
                roomPreviewVo.currentPeopleNumber,
            ),
            fontSize = 11.sp,
            fontWeight = FontWeight.W400,
            color = Color.Black,
            modifier = Modifier.wrapContentSize()
        )
    }
}