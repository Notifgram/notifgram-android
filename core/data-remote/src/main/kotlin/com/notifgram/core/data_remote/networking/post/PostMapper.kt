package com.notifgram.core.data_remote.networking.post

import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.convertIntToMediaType
import com.notifgram.core.data_repository.convertMediaTypeToInt
import com.notifgram.core.domain.entity.Post

fun PostDto.toPost(fileUriBuilder: FileUriBuilder): Post {
    if (id == null)
        throw Exception("Post Id received from remote is null")

    return Post(
        id = id,
        channelId = channelId,
        text = text,
//        mediaUri = mediaUri,
        mediaType = convertIntToMediaType(mediaType),
        mediaFile = fileUriBuilder.buildFile(fileName)
    )
}

fun Post.toPostDto(): PostDto {
    return PostDto(
        id = id,
        channelId = channelId,
        text = text,
//        mediaUri = mediaUri,
        mediaType = convertMediaTypeToInt(mediaType),
        fileName = mediaFile?.name ?: ""

    )
}