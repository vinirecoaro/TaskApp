package br.edu.infnet.tasksapp.presentation.activities.edit_task

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityEditTaskBinding
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivityViewModel

class EditTaskActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditTaskBinding.inflate(layoutInflater) }
    private val viewModel : EditTaskViewModel by viewModels{
        EditTaskViewModel.Factory()
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_task_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val task = intent.getParcelableExtra<TaskDomain>(getString(R.string.task_intent))
        when(item!!.itemId){
            R.id.save_edit_task_menu -> {
                viewModel.update(
                    task!!.id,
                    binding.etTitleEditTask.text.toString(),
                    binding.etDescriptionEditTask.text.toString()
                )
                val resultIntent = Intent().apply {
                    putExtra("edit", "edit")
                }
                setResult(Activity.RESULT_OK,resultIntent)
                finish()
            }
            R.id.delete_edit_task_menu -> {
                viewModel.delete(
                    task!!.id,
                    binding.etTitleEditTask.text.toString(),
                    binding.etDescriptionEditTask.text.toString()
                )
                val resultIntent = Intent().apply {
                    putExtra("edit", "delete")
                }
                setResult(Activity.RESULT_OK,resultIntent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListeners(){
        binding.editTaskToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getTaskInfo(){
        val task = intent.getParcelableExtra<TaskDomain>(getString(R.string.task_intent))
        if(task != null){
            binding.etTitleEditTask.setText(task.title)
            binding.etDescriptionEditTask.setText(task.description)
        }
    }
}