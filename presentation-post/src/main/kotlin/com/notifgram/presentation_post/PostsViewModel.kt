package com.notifgram.presentation_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Result
import com.notifgram.core.domain.usecase.SyncUseCase
import com.notifgram.core.domain.usecase.post.LoadPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    loadPostsUseCase: LoadPostsUseCase,
    private val syncUseCase: SyncUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostListingsState())
    val uiState: StateFlow<PostListingsState> = _uiState

    private val _isOrderSectionVisible = MutableStateFlow(false)
    val isOrderSectionVisible = _isOrderSectionVisible.asStateFlow()

//    private val _orderState = MutableStateFlow(OrderState())
//    val orderState = _orderState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var allPosts: StateFlow<PostListingsState> =
        loadPostsUseCase.execute(LoadPostsUseCase.Request)
            .map {
                PostListingsState(items = (it as Result.Success).data.posts)
            }
            .catch { ex ->
                _uiState.value = PostListingsState(error = ex.message)
            }.onEach {
                i("$TAG allPosts.onEach{ it.searchText=${it.searchText}")
                i("$TAG allPosts.onEach{ it.items=${it.items}")
                i("$TAG allPosts.onEach{ it.isLoading=${it.isLoading}")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PostListingsState(isLoading = true),
            )

//    var post: StateFlow<PostListingsState> =
//        loadPostWithFileContentUseCase.execute(LoadPostWithFileContentUseCase.Request)
//            .map {
//                PostListingsState(item = it.data?.post)
//            }
//            .catch { ex ->
//                _uiState.value = PostListingsState(error = ex.message)
//
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = PostListingsState(isLoading = true),
//            )

//    @OptIn(FlowPreview::class)
//    val searchedPosts: StateFlow<PostListingsState> =
//        _orderState.flatMapLatest { orderState1: OrderState ->
//
//            searchText.debounce(500L)
//                .onEach { _isSearching.update { true } }
//                .combine(allPosts) { text, state ->
//                    if (text.isBlank()) {
//                        PostListingsState(
//                            items = orderPosts(state.items, orderState1),
//                            postOrder = orderState1.postOrder
//                        )
//                    } else {
//                        PostListingsState(
//                            items = orderPosts(
//                                state.items.filter {
//                                    it.doesMatchSearchQuery(text)
//                                }, orderState1
//                            ),
//                            postOrder = orderState1.postOrder
//                        )
//
//                    }
//                }
//                .onEach { _isSearching.update { false } }
//
//        }.stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            initialValue = PostListingsState(isLoading = true)
//        )

    fun onEvent(event: PostListingsEvent) {
        when (event) {
//            is PostListingsEvent.Refresh ->

            is PostListingsEvent.OnSearchQueryChange -> {
                i("$TAG PostListingsEvent.OnSearchQueryChange event.query=${event.query}")
                onSearchTextChange(event.query)
            }

//            is PostListingsEvent.Order -> {
//                if (_orderState.value.postOrder::class == event.postOrder::class &&
//                    _orderState.value.postOrder.orderType == event.postOrder.orderType
//                ) {
//                    i("$TAG PostListingsEvent.Order -> {  order request is not changed.")
//                    return
//                }
//                _orderState.value = _orderState.value.copy(postOrder = event.postOrder)
//            }

            is PostListingsEvent.ToggleOrderSection ->
                _isOrderSectionVisible.value = !_isOrderSectionVisible.value

//            is PostListingsEvent.ToggleFollowedPost -> {
//                i("$TAG PostListingsEvent.ToggleFollowedPost  id=${event.id}")
//                viewModelScope.launch {
//                    toggleFollowingPostUseCase.execute(
//                        ToggleFollowingPostUseCase.Request(
//                            event.id
//                        )
//                    ).collect {}
//                }
//            }

            else -> {}
        }
    }

    fun sync() {
        i("$TAG sync")
        viewModelScope.launch {
            syncUseCase.execute(
                SyncUseCase.Request
            ).collect {}
        }
    }

    // gets triggered when onLongClick happens on post card
    fun toggleSelectedPost(postId: Int) {
        val currentSelection = uiState.value.selectedPosts
        _uiState.value = _uiState.value.copy(
            selectedPosts = if (currentSelection.contains(postId))
                currentSelection.minus(postId) else currentSelection.plus(postId)
        )
    }

    private fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    companion object {
        private const val TAG = "PostsViewModel"

    }
}

