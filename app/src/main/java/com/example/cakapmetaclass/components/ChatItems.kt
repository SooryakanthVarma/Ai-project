package com.example.cakapmetaclass.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakapmetaclass.ChatData


@Composable
fun ChatItems(chatData: ChatData) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth().wrapContentHeight()
                .align(if (chatData.user()) Alignment.CenterEnd else Alignment.CenterStart),
            border = BorderStroke(1.dp, if (chatData.user()) Color.Black else Color.Blue),
        ) {
            Text(
                text = chatData.message,
                style = TextStyle(Color.Black, 15.sp),
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}