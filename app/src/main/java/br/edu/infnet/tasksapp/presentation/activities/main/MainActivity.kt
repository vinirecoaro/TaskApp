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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.data.DataStoreManager
import br.edu.infnet.tasksapp.di.appModule
import br.edu.infnet.tasksapp.presentation.dialog.DialogEditTextActivity
import br.edu.infnet.tasksapp.presentation.activities.edit_task.EditTaskActivity
import br.edu.infnet.tasksapp.presentation.activities.login.LoginActivity
import br.edu.infnet.tasksapp.presentation.activities.sorted_list.SortedListActivity
import br.edu.infnet.tasksapp.presentation.fragments.task_list_recycler_view.TaskListRecyclerViewFragment
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {

    private val viewModel : MainActivityViewModel by inject()
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val dialogEditTextActivity = DialogEditTextActivity(this)
    private val fragmentTaskList = TaskListRecyclerViewFragment()
    lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainToolbar.title = getString(R.string.tasks)
        binding.mainToolbar.setTitleTextColor(Color.WHITE)

       setSupportActionBar(binding.mainToolbar)

        if(savedInstanceState == null){
            val bundle = bundleOf(getString(R.string.sorted_key) to false)
            fragmentTaskList.arguments = bundle

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.frag_rv_task_list_main, fragmentTaskList)
            }
        }
        getUserId()
        setupListener()
    }

    private fun setupListener(){
        binding.fabAdd.setOnClickListener{
            showDialog()
        }
    }

    private fun showDialog(){
        dialogEditTextActivity.showDialog(
            title = getString(R.string.title_new_task),
            topicTitle1 = getString(R.string.subtitle_new_task_title),
            placeHolder1 = getString(R.string.label_new_task_name),
            topicTitle2 = getString(R.string.subtitle_new_task_description),
            placeHolder2 = getString(R.string.label_new_task_description)
        ){results ->
            viewModel.insert(results.first, results.second, userId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main_activity_menu_logoff -> {
                lifecycleScope.launch {
                    viewModel.logoff()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
            R.id.main_activity_menu_sorted_task_list -> {
                startActivity(Intent(this,SortedListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserId(){
        viewModel.getUserId().observe(this){id ->
            userId = id
        }
    }

    fun<T> Flow<T>.observe(owner : LifecycleOwner, observe : (T) -> Unit){
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(Lifecycle.State.STARTED){
                this@observe.collect(observe)
            }
        }
    }

}