package br.edu.infnet.tasksapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.tasksapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val adapter by lazy { TaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()

    }

    private fun setupListener(){
        binding.fabAdd.setOnClickListener{

        }
    }

    private fun setupAdapter(){
        binding.rvTasks.adapter = adapter
    }
}