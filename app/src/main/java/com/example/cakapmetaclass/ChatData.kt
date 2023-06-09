package com.example.cakapmetaclass

data class ChatData(
    val author: Author,
    val message: String,
    val avatar: String,
    var needSpeech: Boolean = true
) {
    fun user(): Boolean = author == Author.User
}

enum class Author {
    Ai, User
}