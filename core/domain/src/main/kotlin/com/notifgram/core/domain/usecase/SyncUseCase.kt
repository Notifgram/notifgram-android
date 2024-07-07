package com.notifgram.core.domain.usecase

import com.notifgram.core.common.MyLog.i
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface StartSync {
    fun startSync()
}

class SyncUseCase @Inject constructor(
    configuration: Configuration,
    private val startSync: StartSync,
) : UseCase<SyncUseCase.Request, SyncUseCase.Response>(
    configuration
) {
    override fun process(request: Request): Flow<Response> {
        i("$TAG SyncUseCase")
        startSync.startSync()
        return flowOf(Response)
    }

    object Request : UseCase.Request
    object Response : UseCase.Response

    companion object {
        private const val TAG = "SyncChannelsUseCase"
    }
}