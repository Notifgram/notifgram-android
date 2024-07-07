package com.notifgram.core.domain.repository

import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

//    fun getPostFlowWithMediaContent(id: Int): Flow<Post?>

    suspend fun getPostsLocally(searchText: String): List<Post>
    suspend fun getPostsLocally(): List<Post>
    fun getPostsLocallyFlow(): Flow<List<Post>>
    fun getPostsFlowAndManageDecryption(): Flow<List<Post>>

    suspend fun getPost(id: Int): Post?
    fun getPostFlow(id: Int): Flow<Post?>

    suspend fun insertPostsLocally(items: List<Post>)
    suspend fun upsertPostLocally(post: Post)

    suspend fun deletePostLocally(id: Int)
    suspend fun deleteAllPostsLocally()


    /////////////////////////////////////////////////////////////
    /////////////////////      REMOTE     ///////////////////////
    /////////////////////////////////////////////////////////////
    suspend fun getPostsFromRemote(): Flow<List<Post>>

    suspend fun insertPostRemotely(item: Post)
}