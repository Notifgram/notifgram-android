package com.notifgram.presentation_post

import com.notifgram.core.domain.entity.Post


data class PostListingsState(
    val item: Post? = null,
    val items: List<Post> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
//    val postOrder: PostOrder = PostOrder.Name(OrderType.Ascending),
    val error: String? = null,
    val selectedPosts: Set<Int> = emptySet(),
//    val isOrderSectionVisible: Boolean = false
    //val isRefreshing: Boolean = false,
)
