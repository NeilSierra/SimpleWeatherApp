package com.example.simpleweatherapp

// Step 9 — MainActivity
import android.Manifest // NEW: for permission constants
import android.content.pm.PackageManager // NEW: for permission check result
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts // NEW: for the permission request launcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // NEW: for checking permission status
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient // NEW: location client
import com.google.android.gms.location.LocationServices // NEW: to build the location client
import com.google.android.gms.location.Priority // NEW: accuracy/power priority for location requests
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await // NEW: lets us await a Task<Location> inside a coroutine
import android.annotation.SuppressLint // NEW: needed for the suppress annotation

class MainActivity : AppCompatActivity() {

    private val apiKey = "35211ba0aff001e1a5351ccb1862d078"

    // NEW: location client + TextViews promoted to class properties so both
    // the city-search flow and the location flow can update them
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvCityResult: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvDescription: TextView

    // NEW: handles the result of the runtime permission dialog
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                fetchCurrentLocationWeather()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NEW: initialize the fused location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val etCityName = findViewById<EditText>(R.id.etCityName)
        val btnGetWeather = findViewById<Button>(R.id.btnGetWeather)
        val btnUseLocation = findViewById<Button>(R.id.btnUseLocation) // NEW: the "Use Current Location" button

        // NEW: these were local `val`s before; now assigned to class properties (declared above)
        tvCityResult = findViewById(R.id.tvCityResult)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvDescription = findViewById(R.id.tvDescription)

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

        // NEW: "Use Current Location" button click listener
        btnUseLocation.setOnClickListener {
            checkPermissionAndFetchLocation()
        }
    }

    // NEW: checks if location permission is already granted; requests it if not
    private fun checkPermissionAndFetchLocation() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            fetchCurrentLocationWeather()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // NEW: fetches the device's current location, then calls the weather API
    // using lat/lon instead of a city name
    @SuppressLint("MissingPermission") // NEW: safe here — permission is verified before this is ever called
    private fun fetchCurrentLocationWeather() {
        lifecycleScope.launch {
            try {
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null
                ).await()

                if (location == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Couldn't determine location. Make sure GPS is on.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val response = RetrofitClient.api.getWeatherByLocation(
                    lat = location.latitude,
                    lon = location.longitude,
                    apiKey = apiKey,
                    units = "metric"
                )

                tvCityResult.text = response.name
                tvTemperature.text = "${response.main.temp}°C"
                tvDescription.text = response.weather[0].description

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Couldn't get weather for your location.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}