package com.notifgram.core.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(
    var id: Int = 0,
    var type: String = "",
    var fileUri: String = "",
    var description: String = "",

// var comment:String
) : Parcelable {
    companion object {
        fun newInstance() = Content(
            description = "",
        )
    }

}

fun Content.doesMatchSearchQuery(query: String): Boolean {
    val matchingCombinations = listOf(
        "$description",
    )

    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}