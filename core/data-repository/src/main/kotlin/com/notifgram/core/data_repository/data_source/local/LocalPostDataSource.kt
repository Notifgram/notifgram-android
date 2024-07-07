package com.notifgram.core.data_repository.data_source.local

import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface LocalPostDataSource {
    suspend fun searchByName(searchText: String): List<Post>
    fun searchByNameFlow(searchText: String): Flow<List<Post>>
    fun getAllFlow(): Flow<List<Post>>
    suspend fun getAll(): List<Post>
    fun getPosts(idsSet: Set<Int>): Flow<List<Post>>
    suspend fun getPost(id: Int): Post?
    fun getPostFlow(id: Int): Flow<Post?>

    suspend fun upsertPosts(items: List<Post>)
    suspend fun insertPosts(items: List<Post>)
    suspend fun upsertPost(item: Post)
    suspend fun insertPost(item: Post)

    suspend fun clearPostListings()
    suspend fun delete(item: Post): Int
    suspend fun delete(id: Int): Int
    /**
     * Deletes rows in the db matching the specified [ids]
     */
    suspend fun deletePosts(ids: List<Int>): Int
}