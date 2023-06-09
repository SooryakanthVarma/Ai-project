package com.example.cakapmetaclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cakapmetaclass.animation.BlinkingRedDot
import com.example.cakapmetaclass.animation.MetahumanSpeakingAnimation
import com.example.cakapmetaclass.animation.SpeakingMetahuman
import com.example.cakapmetaclass.ui.theme.CakapMetaClassTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CakapMetaClassTheme {
                b()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun b() {
        val rememberText = remember {
            mutableStateOf("")
        }
        val items = remember { mutableStateListOf<String>() }
        viewModel.lastMessage.observe(this) {
            items.add(it)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.Cyan)
                ) {

                    MetahumanSpeakingAnimation()
                    BlinkingRedDot()
                }
                LazyColumn(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    itemsIndexed(items) { _, data ->
                        Card(modifier = Modifier.wrapContentHeight()) {
                            Text(
                                text = data,
                                style = TextStyle(color = Color.Black, fontSize = 15.sp)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = rememberText.value,
                    onValueChange = {
                        rememberText.value = it
                    },
                    placeholder = {},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions {
                        rememberText.value.let {
                            viewModel.sendMessage(it)
                        }
                    },

                    label = {},
                    supportingText = {},
                    trailingIcon = {}
                )
                Button(onClick = {
                    rememberText.value.let {
                        viewModel.sendMessage(it)
                    }
                }) {
                    Text(text = "Tanyakan")
                }
            }
        }
    }
}
