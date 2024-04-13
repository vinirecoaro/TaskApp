package br.edu.infnet.tasksapp.presentation.activities.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}