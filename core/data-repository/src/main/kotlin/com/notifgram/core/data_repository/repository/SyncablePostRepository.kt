package com.notifgram.core.data_repository.repository

import com.notifgram.core.data_repository.sync.Syncable
import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface SyncablePostRepository : Syncable {

    fun getPosts(): Flow<List<Post>>

}