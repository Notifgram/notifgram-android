package com.notifgram.core.data_repository

import com.notifgram.core.domain.entity.MediaType

fun convertMediaTypeToInt(mediaType: MediaType): Int {
    return when (mediaType) {
        MediaType.AUDIO -> 1
        MediaType.VIDEO -> 2
        MediaType.NO_MEDIA -> 0
        MediaType.IMAGE -> -1
    }
}

fun convertIntToMediaType(mediaTypeCode:Int ): MediaType {
    return when (mediaTypeCode) {
        1  ->MediaType.AUDIO
        2  -> MediaType.VIDEO
        0   ->  MediaType.NO_MEDIA
        -1 ->  MediaType.IMAGE
        else -> throw Exception("Wrong media type code")
    }
}