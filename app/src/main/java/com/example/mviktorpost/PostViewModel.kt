package com.example.mviktorpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviktorpost.domain.GetPost
import com.example.mviktorpost.network.PostApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class PostViewModel: ViewModel(), ContainerHost<PostState, UIComponent> {

    val getPosts = GetPost(PostApi.providePostApi())

    override val container: Container<PostState, UIComponent> = container(PostState())

    init {
        getPosts()
    }

    fun getPosts() {
        intent {
            val post = getPosts.execute()
            post.onEach { dataState ->
                when(dataState) {
                    is DataState.Loading -> {
                        reduce {
                            state.copy(progressBar = dataState.isLoading)
                        }
                    }
                    is DataState.Success -> {
                        reduce {
                            state.copy(posts = dataState.data)
                        }
                    }
                    is DataState.Error -> {
                        when(dataState.uiComponent) {
                            is UIComponent.Toast -> {
                                postSideEffect(UIComponent.Toast(dataState.uiComponent.text))
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}