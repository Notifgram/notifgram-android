package com.notifgram.core.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var isFollowed: Boolean,

    ) : Parcelable {
    companion object {
        fun newInstance() = Channel(
            name = "",
            description = "",
            isFollowed = false,
        )
    }

}

fun Channel.doesMatchSearchQuery(query: String): Boolean {
    val matchingCombinations = listOf(
        name,
    )

    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}