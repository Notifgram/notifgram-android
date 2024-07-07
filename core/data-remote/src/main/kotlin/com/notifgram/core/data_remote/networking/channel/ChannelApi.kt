package com.notifgram.core.data_remote.networking.channel

import com.notifgram.core.common.BACKEND_API_URL
import com.notifgram.core.data_repository.sync.LastRemoteChange
import com.squareup.moshi.JsonClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
interface ChannelApi {

    @GET("$SUFFIX_URL/{id}")
    suspend fun getChannel(
        @Path("id") id: Int,
        //@Query("apikey") apiKey: String = API_KEY
    ): ChannelDto

    // For sync
    @GET(SUFFIX_URL)
    suspend fun getChannels(
        @Query("id") ids: List<Int>?,
    ): List<ChannelDto>

    @GET(SUFFIX_URL)
    suspend fun getChannels(
        //@Query("symbol") symbol: String,
        //@Query("apikey") apiKey: String = API_KEY
    ): List<ChannelDto>

    @Headers("Content-Type: application/json")
    @POST(SUFFIX_URL)
    fun postChannel(@Body dataModal: ChannelDto): Call<ChannelDto>?


    /////////////////////// NEED TO BE TESTED /////////////////////
    @PUT("$SUFFIX_URL/{id}")
    suspend fun update(
        @Path("id") id: Int,
        @Body channel: ChannelDto?
    ): Response<ChannelDto>/* Call<ChannelDto?>?*/
//    suspend fun update(@Path("id") id: Int, @Body Channel: ChannelDto?): ChannelDto/* Call<ChannelDto?>?*/

    @DELETE("$SUFFIX_URL/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Boolean>

    @DELETE(SUFFIX_URL)
    suspend fun deleteAll(): Response<Int>

    @GET(value = "$SUFFIX_URL/changelist/")
    suspend fun getChannelChangeList(
        @Query("after") after: String,
    ): List<LastRemoteChange>


    companion object {
        const val SUFFIX_URL = "$BACKEND_API_URL/channels"
    }
}
