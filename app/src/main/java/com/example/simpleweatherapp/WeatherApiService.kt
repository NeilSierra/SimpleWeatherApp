package com.example.simpleweatherapp

// Step 7 — Define the API Endpoint
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    // TODO: add a @GET annotation with the correct endpoint path
    // (everything after the base URL, before the "?").
    @GET("data/2.5/weather")
    suspend fun getWeather(
        // TODO: add @Query annotations for city, appid, and units.
        // Match the parameter names exactly to what the API expects.
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
