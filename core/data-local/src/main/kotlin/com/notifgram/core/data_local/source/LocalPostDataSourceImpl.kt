package com.notifgram.core.data_local.source

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_local.db.post.PostDao
import com.notifgram.core.data_local.db.post.toPost
import com.notifgram.core.data_local.db.post.toPostEntity
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.data_source.local.LocalPostDataSource
import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalPostDataSourceImpl @Inject constructor(
    private val dao: PostDao,
//    @ApplicationContext val context: Context
    private val fileUriBuilder: FileUriBuilder
) : LocalPostDataSource {

    override suspend fun searchByName(searchText: String): List<Post> =
        dao.searchByText(searchText).map { entity ->
            entity.toPost(fileUriBuilder)
        }

    // NOT USED
    override fun searchByNameFlow(searchText: String): Flow<List<Post>> {
        return emptyFlow() // Not implemented yet
    }

    override fun getAllFlow(): Flow<List<Post>> {
        i("$TAG getAllFlow")
        return dao.getAllFlow().map { list ->
            list.map { entity ->
                i("$TAG ${entity.fileName}")
                entity.toPost(fileUriBuilder)
            }
        }
    }

    override suspend fun getAll(): List<Post> =
        dao.getAll().map { entity ->
            entity.toPost(fileUriBuilder)
        }

    override fun getPosts(idsSet: Set<Int>): Flow<List<Post>> {
        return dao.getPosts(idsSet).map { entity ->
            entity.map { post ->
                post.toPost(fileUriBuilder)
            }
        }
    }

    override suspend fun getPost(id: Int): Post? =
        dao.getPostById(id)?.toPost(fileUriBuilder)

    override fun getPostFlow(id: Int): Flow<Post?> =
        dao.getPostByIdFlow(id).map { it?.toPost(fileUriBuilder) }

    /**
     * Inserts or updates [Post] in the db under the specified primary keys
     */
    override suspend fun upsertPosts(items: List<Post>) =
        dao.upsertPosts(items.map { it.toPostEntity() })


    override suspend fun insertPost(item: Post) {
        i("$TAG insertPost() item=$item")
        dao.insertPost(item.toPostEntity())
    }

    override suspend fun upsertPost(item: Post) {
        i("$TAG upsertPost() item=$item")
        dao.upsertPost(item.toPostEntity())
    }

    override suspend fun insertPosts(items: List<Post>) {
        i("$TAG insertPosts() items=$items")
        dao.insertPosts(
            items.map { it.toPostEntity() }
        )
    }

    override suspend fun delete(item: Post): Int =
        dao.deletePost(item.toPostEntity())

    override suspend fun delete(id: Int): Int =
        dao.deletePost(id)

    override suspend fun clearPostListings() = dao.clearPostListings()

    /**
     * for sync
     * Deletes rows in the db matching the specified [ids]
     */
    override suspend fun deletePosts(ids: List<Int>): Int {
        i("$TAG deletePosts() ids=$ids")
        val countOfDeletedRows = dao.deletePosts(ids)
        i("$TAG deletePosts() countOfDeletedRows=$countOfDeletedRows")
        return countOfDeletedRows
    }

    companion object {
        private const val TAG = "LocalPostDataSourceImpl"
    }
}