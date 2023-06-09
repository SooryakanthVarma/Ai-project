package com.example.cakapmetaclass.data

import com.google.gson.annotations.SerializedName

data class ResponseChat(

	@field:SerializedName("created")
	val created: Int? = null,

	@field:SerializedName("usage")
	val usage: Usage? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("choices")
	val choices: List<ChoicesItem?>? = null,

)

data class Message(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class ChoicesItem(

	@field:SerializedName("finish_reason")
	val finishReason: String? = null,

	@field:SerializedName("index")
	val index: Int? = null,

	@field:SerializedName("message")
	val message: Message? = null
)

data class Usage(

	@field:SerializedName("completion_tokens")
	val completionTokens: Int? = null,

	@field:SerializedName("prompt_tokens")
	val promptTokens: Int? = null,

	@field:SerializedName("total_tokens")
	val totalTokens: Int? = null
)
