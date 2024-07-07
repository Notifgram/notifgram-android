package com.notifgram.core.data_remote.source

import com.notifgram.core.data_remote.networking.channel.ChannelApi
import com.notifgram.core.data_remote.networking.channel.ChannelDto
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.entity.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RemoteChannelDataSourceImplTest {

    private val channelApi = mock<ChannelApi>()
    private val channelDataSource = RemoteChannelDataSourceImpl(channelApi)

    @ExperimentalCoroutinesApi
    @Test
    fun testGetChannels() = runTest {
        val remoteChannels = listOf(ChannelDto(1, "name", "description"))
        val expectedChannels = listOf(Channel(1, "name", "description", false))
        whenever(channelApi.getChannels()).thenReturn(remoteChannels)
        val result = channelDataSource.getChannels().first()
        Assert.assertEquals(expectedChannels, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetChannel() = runTest {
        val id = 1
        val remoteChannel = ChannelDto(1, "name", "description")
        val expectedChannel = Channel(1, "name", "description", false)
        whenever(channelApi.getChannel(id)).thenReturn(remoteChannel)
        val result = channelDataSource.getChannel(id).first()
        Assert.assertEquals(expectedChannel, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetChannelsThrowsError() = runTest {
        whenever(channelApi.getChannels()).thenThrow(RuntimeException())
        channelDataSource.getChannels().catch {
            Assert.assertTrue(it is UseCaseException.ChannelException)
        }.collect()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetChannelThrowsError() = runTest {
        val id = 1
        whenever(channelApi.getChannel(id)).thenThrow(RuntimeException())
        channelDataSource.getChannel(id).catch {
            Assert.assertTrue(it is UseCaseException.ChannelException)
        }.collect()
    }
}