package com.notifgram.core.data_repository.sync

import android.util.Log
import com.notifgram.core.common.MyLog.i
import kotlin.coroutines.cancellation.CancellationException

interface Synchronizer {
    suspend fun getChangeListVersions(): CachedVersions

    suspend fun updateChangeListVersions(update: CachedVersions.() -> CachedVersions)

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}

suspend fun Synchronizer.changeListSync(
    versionReader: (CachedVersions) -> String,
    changeListFetcherFromRemote: suspend (String) -> List<LastRemoteChange>,
    versionUpdater: CachedVersions.(String) -> CachedVersions,
    modelDeleter: suspend (List<Int>) -> Int,
    modelUpdater: suspend (List<Int>) -> Unit,
) = suspendRunCatching {
    val tag = "Synchronizer.changeListSync"
    val currentVersion =
        versionReader(getChangeListVersions())
    i("$tag currentVersion=$currentVersion")

    val changeList =
        changeListFetcherFromRemote(currentVersion)
    if (changeList.isEmpty()) return@suspendRunCatching true

    val (deleted, updated) = changeList.partition(LastRemoteChange::isDelete)
    i("$tag deleted=$deleted")
    i("$tag updated=$updated")
    modelDeleter(deleted.map(LastRemoteChange::id))

    modelUpdater(updated.map(LastRemoteChange::id))

    val latestVersion = changeList.last().timestamp

    i("$tag latestVersion=$latestVersion")

    updateChangeListVersions {
        versionUpdater(latestVersion)
    }
}.isSuccess
