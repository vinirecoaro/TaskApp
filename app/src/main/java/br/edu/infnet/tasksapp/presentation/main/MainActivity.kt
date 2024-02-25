package br.edu.infnet.tasksapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.infnet.tasksapp.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.core.view.isVisible
import br.edu.infnet.tasksapp.R


class MainActivity : AppCompatActivity() {

    private val viewModel : MainActivityViewModel by viewModels{
        MainActivityViewModel.Factory()
    }
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val adapter by lazy { TaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapter()
        setupListener()
        observeStates()
    }

    private fun setupListener(){
        binding.fabAdd.setOnClickListener{

        }
    }

    private fun setupAdapter(){
        binding.rvTasks.adapter = adapter
    }

    private fun observeStates(){
        viewModel.state.observe(this){state ->
            when(state){
                MainActivityState.Loading -> {
                    binding.pbTasks.isVisible = true
                }
                MainActivityState.Empty -> {
                    binding.pbTasks.isVisible = false
                    Toast.makeText(this,getString(R.string.label_empty_tasks), Toast.LENGTH_LONG).show()
                }
                is MainActivityState.Error -> {
                    binding.pbTasks.isVisible = false
                    Toast.makeText(this,state.message, Toast.LENGTH_LONG).show()
                }

                is MainActivityState.Success -> {
                    binding.pbTasks.isVisible = false
                    adapter.submitList(state.tasks)
                }
            }

        }
    }
}