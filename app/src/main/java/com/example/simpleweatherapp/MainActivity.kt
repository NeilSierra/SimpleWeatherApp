package com.example.simpleweatherapp

// Step 9 — MainActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val apiKey = "35211ba0aff001e1a5351ccb1862d078"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCityName = findViewById<EditText>(R.id.etCityName)
        val btnGetWeather = findViewById<Button>(R.id.btnGetWeather)
        val tvCityResult = findViewById<TextView>(R.id.tvCityResult)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)

        btnGetWeather.setOnClickListener {
            val cityName = etCityName.text.toString().trim()

            if (cityName.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {

                    // TODO 1: call RetrofitClient.api.getWeather(...) with the
                    // right arguments, inside a try block.
                    val response = RetrofitClient.api.getWeather(
                        city = cityName,
                        apiKey = apiKey,
                        units = "metric"
                    )

                    // TODO 2: on success, set tvCityResult, tvTemperature and
                    // tvDescription from the response object.
                    tvCityResult.text = response.name
                    tvTemperature.text = "${response.main.temp}°C"
                    tvDescription.text = response.weather[0].description

                } catch (e: Exception) {

                    // TODO 3: add a catch block. On failure, show a Toast with
                    // a clear, user-friendly error message instead of crashing.
                    Toast.makeText(
                        this@MainActivity,
                        "Couldn't get weather. Check the city name or your connection.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }
}
