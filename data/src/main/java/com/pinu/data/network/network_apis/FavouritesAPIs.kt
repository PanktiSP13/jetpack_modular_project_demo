package com.pinu.data.network.network_apis

import com.pinu.domain.entities.network_service.response.BookResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FavouritesAPIs {

    /* -------------- Favourites Service --------------------- */

    @GET("/favourites")
    suspend fun getFavourites(): BookResponse

    @POST("/favourites/add")
    suspend fun addToFavourites(@Body bookId: Int): BookResponse

    @DELETE("/favourites/remove")
    suspend fun removeFromFavourites(@Query("book_id") bookId: Int): BookResponse
}