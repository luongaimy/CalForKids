package fr.epita.calforkids

import retrofit2.Response
import retrofit2.http.GET

interface ApiAds {
    @GET("v1/fbdb37c1-f194-4c53-b94d-f9da5f2b39bb")
    suspend fun getAds(): Response<List<Advertisement>>
}