package com.notifgram.presentation_post


sealed class PostListingsEvent {
    object Refresh : PostListingsEvent()
    data class OnSearchQueryChange(val query: String) :
        PostListingsEvent() //query is not needed for now

    data class ToggleFollowedPost(val id: Int) : PostListingsEvent()
    object ToggleOrderSection : PostListingsEvent()
//    data class Order(val postOrder: PostOrder) : PostListingsEvent()
}
