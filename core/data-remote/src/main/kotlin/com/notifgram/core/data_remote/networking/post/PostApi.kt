package com.notifgram.core.data_remote.networking.post

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
interface PostApi {

    @GET("$SUFFIX_URL/{id}")
    suspend fun getPost(
        @Path("id") id: Int,
        //@Query("apikey") apiKey: String = API_KEY
    ): PostDto

    // For sync
    @GET("$SUFFIX_URL/GetByIds/")
    suspend fun getPosts(
        @Query("ids") ids: List<Int>?,
    ): List<PostDto>

    @GET(SUFFIX_URL)
    suspend fun getPosts(
        //@Query("symbol") symbol: String,
        //@Query("apikey") apiKey: String = API_KEY
    ): List<PostDto>

    @Headers("Content-Type: application/json")
    @POST(SUFFIX_URL)
    fun postPost(@Body dataModal: PostDto): Call<PostDto>?


    /////////////////////// NEED TO BE TESTED /////////////////////
    @PUT("$SUFFIX_URL/{id}")
    suspend fun update(
        @Path("id") id: Int,
        @Body post: PostDto?
    ): Response<PostDto>/* Call<PostDto?>?*/
//    suspend fun update(@Path("id") id: Int, @Body Post: PostDto?): PostDto/* Call<PostDto?>?*/

    @DELETE("$SUFFIX_URL/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Boolean>

    @DELETE(SUFFIX_URL)
    suspend fun deleteAll(): Response<Int>

    @GET(value = "$SUFFIX_URL/changelist/")
    suspend fun getPostChangeList(
        @Query("after") after: String,
    ): List<LastRemoteChange>


    companion object {
        const val SUFFIX_URL = "$BACKEND_API_URL/posts"
    }
}
