package com.notifgram.core.data_remote.source

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_remote.networking.channel.ChannelApi
import com.notifgram.core.data_remote.networking.channel.ChannelDto
import com.notifgram.core.data_remote.networking.channel.toChannel
import com.notifgram.core.data_remote.networking.channel.toChannelDto
import com.notifgram.core.data_repository.data_source.remote.RemoteChannelDataSource
import com.notifgram.core.data_repository.sync.LastRemoteChange
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.entity.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RemoteChannelDataSourceImpl @Inject constructor(
    private val channelApi: ChannelApi
) : RemoteChannelDataSource {

    override suspend fun updateChannel(item: Channel): Flow<Channel> {
        i("$TAG updateChannel item=$item")
        val channel = channelApi.update(item.id, item.toChannelDto()).body()
        i("$TAG updateChannel returned result: channel=$channel")
        return flow<Channel> {

            if (channel != null)
                emit(channel.toChannel())
            else
                throw UseCaseException.ChannelException(Throwable("Response does not contain returned channel"))
        }.catch {
            throw UseCaseException.ChannelException(it)
        }
    }

    override suspend fun getChannelChangeList(after: String): List<LastRemoteChange> {
        i("$TAG getChannelChangeList() after=$after")
        val changedChannelsIds = channelApi.getChannelChangeList(after = after)
        i("$TAG getChannelChangeList() changedChannelsIds=$changedChannelsIds")
        return changedChannelsIds
    }

    override suspend fun deleteAllChannels() {
        i("$TAG deleteAllChannels")
        channelApi.deleteAll()
    }

    override suspend fun deleteChannel(id: Int): Flow<Boolean> {
        i("$TAG deleteChannel id=$id")
        val isSuccess: Boolean = channelApi.delete(id).body()
            ?: throw UseCaseException.ChannelException(Throwable("response is null"))
        return flow { emit(isSuccess) }
    }

    override fun getChannels(): Flow<List<Channel>> = flow {
        emit(channelApi.getChannels())
    }.map { channel ->
        channel.map {
            it.toChannel()
        }
    }.catch {
        throw UseCaseException.ChannelException(it)
    }

    override suspend fun getChannels(ids: List<Int>): List<Channel> {
        return channelApi.getChannels(ids).map { channelDto ->
            channelDto.toChannel()
        }
    }


    override fun getChannel(id: Int): Flow<Channel> = flow {
        emit(channelApi.getChannel(id))
    }.map {
        it.toChannel()
    }.catch {
        throw UseCaseException.ChannelException(it)
    }

    override fun postChannel(item: Channel) {
        i("$TAG postChannel item=$item")

        channelApi.postChannel(item.toChannelDto())?.enqueue(
            object : Callback<ChannelDto> {
                override fun onFailure(call: Call<ChannelDto>, t: Throwable) {
                    i("$TAG onFailure t=$t")
                    throw UseCaseException.ChannelException(t)
                }

                override fun onResponse(
                    call: Call<ChannelDto>,
                    response: Response<ChannelDto>
                ) {
                    val channel = response.body()?.toChannel()
                    i("$TAG onResponse response.body()=${response.body()}")
                    i("$TAG onResponse body=$channel")

                }
            }
        )
//        return flow { emit(channel ) }
    }


    companion object {
        private const val TAG = "RemoteChannelDataSourceImpl"
    }

}

