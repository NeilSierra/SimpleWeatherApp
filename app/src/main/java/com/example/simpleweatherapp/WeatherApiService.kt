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

    // Part 6 — Add a “Use Current Location” button using FusedLocationProviderClient
    // New function for the use current location button
    @GET("data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
