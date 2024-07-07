package com.notifgram.core.data_local.db.post

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    // Used for sync
    /**
     * Inserts or updates [posts] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Upsert
    suspend fun upsertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(
        posts: List<PostEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(
        post: PostEntity
    )

    @Update
    fun updatePost(vararg postEntity: PostEntity)

    @Query(
        """
            SELECT *
            FROM PostEntity
        """
    )
    suspend fun getAll(): List<PostEntity>

    @Query(
        """
            SELECT *
            FROM PostEntity
        """
    )
    fun getAllFlow(): Flow<List<PostEntity>>

    @Query(
        """
            SELECT *
            FROM PostEntity
            WHERE text LIKE '%' || :searchText || '%'   OR
                :searchText == text 
        """
    )
    suspend fun searchByText(searchText: String): List<PostEntity>

    @Query(
        """
           SELECT *
            FROM PostEntity
            WHERE text LIKE '%' || :searchText || '%'   OR
                :searchText == text 
        """
    )
    fun searchByNameFlow(searchText: String): Flow<List<PostEntity>>

    @Query(
        value = """
            SELECT * FROM PostEntity
            WHERE id in (:ids)
        """,
    )
    fun getPosts(ids: Set<Int>): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    suspend fun getPostById(id: Int): PostEntity?

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getPostByIdFlow(id: Int): Flow<PostEntity?>

    @Delete
    suspend fun deletePost(postEntity: PostEntity): Int

    @Query(
        value = """
            DELETE FROM PostEntity
            WHERE id == :id
        """,
    )
    suspend fun deletePost(id: Int): Int

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM PostEntity
            WHERE id in (:ids)
        """,
    )
    suspend fun deletePosts(ids: List<Int>): Int

    //?
    @Query("DELETE FROM PostEntity")
    suspend fun clearPostListings()

}
