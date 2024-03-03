package br.edu.infnet.tasksapp.presentation.activities.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.infnet.tasksapp.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.presentation.dialog.DialogEditTextActivity
import br.edu.infnet.tasksapp.presentation.activities.edit_task.EditTaskActivity
import br.edu.infnet.tasksapp.presentation.activities.login.LoginActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel : MainActivityViewModel by viewModels{
        MainActivityViewModel.Factory()
    }
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val adapter by lazy { TaskAdapter(emptyList()) }
    val dialogEditTextActivity = DialogEditTextActivity(this)

    private val editTaskContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getAllTasks()
            val data: Intent? = result.data
            val toastMessage = data?.getStringExtra(getString(R.string.edit_intent))
            if (!toastMessage.isNullOrEmpty()) {
                if(toastMessage == getString(R.string.edit_intent)){
                    Toast.makeText(this@MainActivity, getString(R.string.edit_task_success_message), Toast.LENGTH_SHORT).show()
                }else if(toastMessage == getString(R.string.delete_intent)){
                    Toast.makeText(this@MainActivity, getString(R.string.delete_task_success_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.registerToolbar.setTitle(getString(R.string.tasks))
        binding.registerToolbar.setTitleTextColor(Color.WHITE)

       setSupportActionBar(binding.registerToolbar)

        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        setupAdapter()
        setupListener()
        observeStates()
    }

    private fun setupListener(){
        binding.fabAdd.setOnClickListener{
            showDialog()
        }
        adapter.onItemClick = {
            val intent = Intent(this,EditTaskActivity::class.java)
            intent.putExtra(getString(R.string.task_intent), it)
            editTaskContract.launch(intent)
        }
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
                    val tasks = state.tasks
                    adapter.updateList(tasks)
                }
            }

        }
    }

    private fun setupAdapter(){
        binding.rvTasks.adapter = adapter
    }

    private fun showDialog(){
        dialogEditTextActivity.showDialog(
            title = getString(R.string.title_new_task),
            topicTitle1 = getString(R.string.subtitle_new_task_title),
            placeHolder1 = getString(R.string.label_new_task_name),
            topicTitle2 = getString(R.string.subtitle_new_task_description),
            placeHolder2 = getString(R.string.label_new_task_description)
        ){results ->
            viewModel.insert(results.first, results.second)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.main_activity_menu_logoff -> {
                lifecycleScope.launch {
                    viewModel.logoff()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun<T> Flow<T>.observe(owner : LifecycleOwner, observe : (T) -> Unit){
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(Lifecycle.State.STARTED){
                this@observe.collect(observe)
            }
        }
    }

}