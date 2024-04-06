package br.edu.infnet.tasksapp.presentation.activities.sorted_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        if(savedInstanceState == null){
            supportFragmentManager.commit{
                setReorderingAllowed(true)
                add(R.id.frag_rv_task_list_sorted, fragmentTaskList)
            }
        }

    }
}