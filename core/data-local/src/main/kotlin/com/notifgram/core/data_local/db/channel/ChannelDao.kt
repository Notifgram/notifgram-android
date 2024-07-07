package com.notifgram.core.data_local.db.channel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Query("UPDATE ChannelEntity SET isFollowed = NOT isFollowed WHERE id=:id")
    suspend fun toggleFollowingChannel(id: Int)

    // Used for sync
    /**
     * Inserts or updates [channels] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertChannels(channels: List<ChannelEntity>)

    // NOT USED
    @Upsert
    suspend fun upsertChannel(channel: ChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(
        channels: List<ChannelEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(
        channel: ChannelEntity
    )

    @Update
    fun updateChannel(vararg channelEntity: ChannelEntity)

    @Query(
        """
            SELECT *
            FROM ChannelEntity
        """
    )
    suspend fun getAll(): List<ChannelEntity>

    @Query(
        """
            SELECT *
            FROM ChannelEntity
        """
    )
    fun getAllFlow(): Flow<List<ChannelEntity>>

    @Query(
        """
            SELECT id
            FROM ChannelEntity
            WHERE isFollowed == true
        """
    )
    fun getAllFollowedChannelsIdsFlow(): Flow<List<Int>>

    @Query(
        """
            SELECT *
            FROM ChannelEntity
            WHERE name LIKE '%' || :searchText || '%'   OR
                :searchText == name 
        """
    )
    suspend fun searchByName(searchText: String): List<ChannelEntity>

    @Query(
        """
           SELECT *
            FROM ChannelEntity
            WHERE name LIKE '%' || :searchText || '%'   OR
                :searchText == name 
        """
    )
    fun searchByNameFlow(searchText: String): Flow<List<ChannelEntity>>

    @Query(
        value = """
            SELECT * FROM ChannelEntity
            WHERE id in (:ids)
        """,
    )
    fun getChannels(ids: Set<Int>): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM ChannelEntity WHERE id = :id")
    suspend fun getChannelById(id: Int): ChannelEntity?

    @Query("SELECT * FROM ChannelEntity WHERE id = :id")
    fun getChannelByIdFlow(id: Int): Flow<ChannelEntity?>

    @Delete
    suspend fun deleteChannel(channelEntity: ChannelEntity): Int

    @Query(
        value = """
            DELETE FROM ChannelEntity
            WHERE id == :id
        """,
    )
    suspend fun deleteChannel(id: Int): Int

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM ChannelEntity
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteChannels(ids: List<Int>): Int

    //?
    @Query("DELETE FROM ChannelEntity")
    suspend fun clearChannelListings()

}
