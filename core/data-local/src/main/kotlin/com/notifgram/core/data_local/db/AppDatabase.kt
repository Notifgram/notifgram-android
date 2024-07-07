package com.notifgram.core.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.notifgram.core.data_local.db.channel.ChannelDao
import com.notifgram.core.data_local.db.channel.ChannelEntity
import com.notifgram.core.data_local.db.post.PostDao
import com.notifgram.core.data_local.db.post.PostEntity

@Database(
    entities = [
        ChannelEntity::class,
        PostEntity::class
    ],
    version = 22
//    autoMigrations = [  //Source for room migration:https://developer.android.com/training/data-storage/room/migrating-db-versions
//        AutoMigration(from = 1, to = 2, spec = AppDatabase.MyAutoMigration::class)
//    ],

)
internal abstract class AppDatabase : RoomDatabase() {

    abstract val channelDao: ChannelDao
    abstract val postDao: PostDao

}