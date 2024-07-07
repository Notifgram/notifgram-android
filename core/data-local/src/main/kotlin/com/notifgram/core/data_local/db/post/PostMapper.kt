package com.notifgram.core.data_local.db.post

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.convertIntToMediaType
import com.notifgram.core.data_repository.convertMediaTypeToInt
import com.notifgram.core.domain.entity.Post

fun Post.toPostEntity(): PostEntity {
    i("Post.toPostEntity() mediaFile?.name=${mediaFile?.name}")
    val post = PostEntity(
        id = id,
        channelId = channelId,
        text = text,
        fileName = mediaFile?.name,
        mediaType = convertMediaTypeToInt(mediaType)
    )
    i("Post.toPostEntity() post=$post")
    return post
}

fun PostEntity.toPost(fileUriBuilder: FileUriBuilder): Post {
    i("PostEntity.toPost fileName=$fileName")
    return Post(
        id = id,
        channelId = channelId,
        text = text,
        mediaFile = fileUriBuilder.buildFile(fileName),
        mediaType = convertIntToMediaType(mediaType),
    )
}
