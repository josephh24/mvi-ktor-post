package com.example.mviktorpost

import com.example.mviktorpost.network.model.Post

data class PostState(
    val progressBar: Boolean = false,
    val posts: List<Post> = emptyList()
)