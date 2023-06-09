package com.example.cakapmetaclass

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cakapmetaclass.components.ChatItems
import com.example.cakapmetaclass.ui.theme.CakapMetaClassTheme
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import kotlinx.coroutines.launch
import java.util.Locale

class ChatActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    val viewModel: ViewModel by viewModels()
    var tts: TextToSpeech? = null
    var speakQueque = ""
    val languageIdentifier: LanguageIdentifier by lazy { LanguageIdentification.getClient() }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendMessageV2(viewModel.materi)
        setContent {
            val listState = rememberLazyListState()
//            CakapMetaClassTheme {
            Box(Modifier.fillMaxSize()) {
                val textState = remember { mutableStateOf("") }
                val submitedText = remember { mutableStateOf("") }
                val listtext = remember { mutableStateListOf<ChatData>() }
                val cr = rememberCoroutineScope()
                viewModel.chatDataReceived.collectAsState().value?.let {
//                    if (it.needSpeech) {
                    speakQueque = it.message
                    tts = TextToSpeech(this@ChatActivity, this@ChatActivity)
//                    } else {
                    listtext.add(it)
//                    }
                    viewModel._chatDataFlow.value = null
                    cr.launch {
                        listState.scrollToItem(listtext.size - 1)
                    }
                }
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Gray)
                    ) {

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(state = listState, modifier = Modifier.padding(horizontal = 8.dp)) {
                        itemsIndexed(listtext) { index, item ->
                            ChatItems(chatData = item)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (index == listtext.size - 1) Spacer(modifier = Modifier.height(50.dp))
                        }
                    }

                }
                TextField(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text("Chat") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        submitedText.value = textState.value
                        listtext.add(ChatData(Author.User, submitedText.value, ""))
                        textState.value = ""
                        viewModel.sendMessageV2(submitedText.value)
                    })
                )

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
    }


    private fun checkLocaleFromText(text: String, langId: (String) -> Unit) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {

                } else {
                    langId(languageCode)
                }
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
            }
    }


    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            checkLocaleFromText(speakQueque) {
                //meta human animation will run needed to implement
                tts?.language = Locale(it)
                tts?.setSpeechRate(1.0F)
                tts?.speak(speakQueque, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }

}