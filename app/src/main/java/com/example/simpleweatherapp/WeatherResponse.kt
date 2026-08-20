package com.example.simpleweatherapp

// Step 6 — Build the Data Model
data class WeatherResponse(
    val name: String,
    val main: Main,
    // TODO: the JSON also contains a "weather" field, which is a LIST of
    // objects, each with a "description". Add that property here.
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    // TODO: the JSON's "main" object also includes humidity.
    // Add the matching property, with the correct type.
    val humidity: Int
)

// TODO: define the data class for each item inside the "weather" list.
// It needs at least one property: description (String).
data class Weather(
    val description: String
)