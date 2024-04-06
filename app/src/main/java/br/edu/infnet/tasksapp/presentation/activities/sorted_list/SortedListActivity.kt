package br.edu.infnet.tasksapp.presentation.activities.sorted_list

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivitySortedListBinding
import br.edu.infnet.tasksapp.presentation.fragments.task_list_recycler_view.TaskListRecyclerViewFragment

class SortedListActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySortedListBinding.inflate(layoutInflater) }
    private val fragmentTaskList = TaskListRecyclerViewFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sortedListToolbar.title = getString(R.string.sorted_task_list_title)
        binding.sortedListToolbar.setTitleTextColor(Color.WHITE)

        setSupportActionBar(binding.sortedListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState == null){
            val bundle = bundleOf(getString(R.string.sorted_key) to true)
            fragmentTaskList.arguments = bundle

            supportFragmentManager.commit{
                setReorderingAllowed(true)
                add(R.id.frag_rv_task_list_sorted, fragmentTaskList)
            }
        }
        setupListeners()

    }

    private fun setupListeners(){
        binding.sortedListToolbar.setNavigationOnClickListener {
            finish()
        }
    }

}