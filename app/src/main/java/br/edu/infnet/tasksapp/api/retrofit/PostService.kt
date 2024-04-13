package br.edu.infnet.tasksapp.api.retrofit
import br.edu.infnet.tasksapp.domain.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = "fd6ba221b30d89ed98d202ec641bd5c4"
    ): Response<WeatherResponse>

}