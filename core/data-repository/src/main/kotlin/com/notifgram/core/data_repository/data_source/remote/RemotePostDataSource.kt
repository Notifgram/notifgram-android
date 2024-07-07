package com.notifgram.core.data_repository.data_source.remote

import com.notifgram.core.data_repository.sync.LastRemoteChange
import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface RemotePostDataSource {

    suspend fun deleteAllPosts()
    suspend fun deletePost(id: Int): Flow<Boolean>
    fun getPosts(): Flow<List<Post>>
    suspend fun getPosts(ids: List<Int>): List<Post>
    fun getPost(id: Int): Flow<Post>
    fun postPost(item: Post)
    suspend fun updatePost(item: Post): Flow<Post>

    // Sync
    suspend fun getPostChangeList(after: String): List<LastRemoteChange>
}
