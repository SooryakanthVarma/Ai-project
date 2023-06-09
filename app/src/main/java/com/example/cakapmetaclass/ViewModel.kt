package com.example.cakapmetaclass

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cakapmetaclass.data.MessagesItem
import com.example.cakapmetaclass.data.RequestChat
import com.example.cakapmetaclass.data.ResponseChat
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.Duration

class ViewModel : ViewModel() {
    val mykey = "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"
    val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    val clientV2: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
    }

    val dataList = MutableLiveData<MutableList<String>>()
    val dataTemp: MutableList<String> = mutableListOf()
    val listMessage: MutableState<List<String>> = mutableStateOf(listOf())
    val lastMessage = MutableLiveData<String>("")

    val chatData = MutableLiveData<ChatData>()

    val _chatDataFlow = MutableStateFlow<ChatData?>(null)
    var chatDataReceived: StateFlow<ChatData?> = _chatDataFlow


    val materi =
        "materi pembahasan kali ini adalah Bagaimana cara menjadi pekerja yang baik, jika saya bertanya topik lain silahkan jawab \"pembahasan ini terbatas di materi yang telah di tentukan\", namun jika saya berkata \"dibuka pembahasan topik lain\" maka anda dapat menjawab semua pertanyaan dari topi apapun"
    val justStart = false
    fun sendMessage(question: String) {
        val data = JSONObject().apply {
            put("model", "text-davinci-003")
            put("prompt", question)
            put("max_tokens", 3000)
            put("temperature", 0)
        }

        val requestBody =
            data.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer $mykey")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERR", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val message = JSONObject(response.body?.string())
                    val dataMessage = message.getJSONArray("choices")
                    val message1 = dataMessage.getJSONObject(0).getString("text")
                    dataTemp.add(message1)
                    dataList.postValue(dataTemp)
                    listMessage.value = dataTemp
                    chatData.postValue(ChatData(Author.Ai, message1, ""))
                    _chatDataFlow.value = ChatData(Author.Ai, message1, "")
                } else {
                    response.body?.string()?.let { Log.d("WHT", it) }
                }
            }
        })
    }

    fun sendMessageV2(question: String) {
        val jsonRequest = Gson().toJson(
            RequestChat(
                model = "gpt-3.5-turbo",
                messages = listOf(MessagesItem("user", question))
            )
        )
        clientV2.readTimeout(Duration.ofMinutes(2))
        clientV2.writeTimeout(Duration.ofMinutes(2))
        val requestBody = jsonRequest.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $mykey")
            .post(requestBody)
            .build()
        clientV2.build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERR", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val chat = Gson().fromJson(
                        response.body?.string(),
                        ResponseChat::class.java
                    )
                    _chatDataFlow.value =
                        ChatData(Author.Ai, chat.choices?.firstOrNull()?.message?.content ?: "", "")
                } else {
                    response.body?.string()?.let { Log.d("WHT", it) }
                }
            }
        })
    }

}