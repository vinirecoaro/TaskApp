package br.edu.infnet.tasksapp.presentation.activities.edit_task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditTaskBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.editTaskToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListeners()

    }

    private fun setupListeners(){
        binding.editTaskToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}