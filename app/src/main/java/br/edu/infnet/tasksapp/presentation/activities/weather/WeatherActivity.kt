package br.edu.infnet.tasksapp.presentation.activities.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.api.retrofit.PostService
import br.edu.infnet.tasksapp.api.retrofit.RetrofitHelper
import br.edu.infnet.tasksapp.databinding.ActivityWeatherBinding
import br.edu.infnet.tasksapp.domain.model.WeatherResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round

class WeatherActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }
    private val retrofit by lazy { RetrofitHelper.retrofit }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.weatherDataToolbar.title = getString(R.string.weather_data)
        binding.weatherDataToolbar.setTitleTextColor(Color.WHITE)

        setSupportActionBar(binding.weatherDataToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListeners()

    }

    private fun setupListeners(){
        binding.btFetch.setOnClickListener {
            if(
                binding.etLat.text.isEmpty() ||
                binding.etLong.text.isEmpty()
            ){
                Toast.makeText(this, getString(R.string.fill_all_fields),Toast.LENGTH_LONG).show()
            }else{
                val lat = binding.etLat.text.toString().toDouble()
                val lon = binding.etLong.text.toString().toDouble()
                fetchWeather(lat,lon)
            }
        }

        binding.weatherDataToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            val postService = retrofit.create(PostService::class.java)
            val weatherData = postService.getWeather(latitude, longitude)
            if(weatherData.isSuccessful){
                val data = weatherData.body()
                val forecast = "${getString(R.string.weather_forecast)} ${data!!.weather[0].main}"
                binding.tvForecast.text = forecast
                val tempMax = "${getString(R.string.maximum_temperature)} ${BigDecimal(data.main.temp_max-273).setScale(2, RoundingMode.HALF_UP)}° C"
                binding.tvTempMax.text = tempMax
                val tempMin = "${getString(R.string.minimum_temperature)} ${BigDecimal(data.main.temp_min-273).setScale(2, RoundingMode.HALF_UP)}° C"
                binding.tvTempMin.text = tempMin
                val feelsLike = "${getString(R.string.thermal_sensation)} ${BigDecimal(data.main.feels_like-273).setScale(2, RoundingMode.HALF_UP)}° C"
                binding.tvFeelsLike.text = feelsLike
            }else{
                Toast.makeText(this@WeatherActivity, R.string.error_on_fetch_weather_data, Toast.LENGTH_LONG).show()
            }
        }

    }

}