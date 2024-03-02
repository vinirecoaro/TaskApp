package br.edu.infnet.tasksapp.presentation.activities.edit_task

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityEditTaskBinding
import br.edu.infnet.tasksapp.domain.model.TaskDomain

class EditTaskActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditTaskBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editTaskToolbar.setTitle(getString(R.string.edit_task))
        binding.editTaskToolbar.setTitleTextColor(Color.WHITE)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.editTaskToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getTaskInfo()
        setupListeners()

    }

    private fun setupListeners(){
        binding.editTaskToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getTaskInfo(){
        val task = intent.getParcelableExtra<TaskDomain>("task")
        if(task != null){
            binding.etTitleEditTask.setText(task.title)
            binding.etDescriptionEditTask.setText(task.description)
        }
    }
}