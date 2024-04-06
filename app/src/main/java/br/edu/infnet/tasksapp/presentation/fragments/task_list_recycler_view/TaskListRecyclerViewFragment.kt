package br.edu.infnet.tasksapp.presentation.fragments.task_list_recycler_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.FragmentTaskListRecyclerViewBinding
import br.edu.infnet.tasksapp.presentation.activities.edit_task.EditTaskActivity
import br.edu.infnet.tasksapp.presentation.activities.main.TaskAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TaskListRecyclerViewFragment : Fragment() {

    private var _binding : FragmentTaskListRecyclerViewBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {TaskAdapter(emptyList())}
    private val viewModel : TaskListRecyclerViewViewModel by inject()
    lateinit var userId : String

    private val editTaskContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getAllTasks(userId)
            val data: Intent? = result.data
            val toastMessage = data?.getStringExtra(getString(R.string.edit_intent))
            if (!toastMessage.isNullOrEmpty()) {
                if(toastMessage == getString(R.string.edit_intent)){
                    Toast.makeText(requireContext(), getString(R.string.edit_task_success_message), Toast.LENGTH_SHORT).show()
                }else if(toastMessage == getString(R.string.delete_intent)){
                    Toast.makeText(requireContext(), getString(R.string.delete_task_success_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListRecyclerViewBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.rvFragTaskList.layoutManager = LinearLayoutManager(requireContext())

        getUserId()
        setupAdapter()
        setupListener()
        observeStates()

        return rootView
    }

    private fun observeStates(){
        viewModel.state.observe(this){state ->
            when(state){
                TaskListFragmentState.Loading -> {
                    binding.pbFragTasks.isVisible = true
                }
                TaskListFragmentState.Empty -> {
                    binding.pbFragTasks.isVisible = false
                    Toast.makeText(requireContext(),getString(R.string.label_empty_tasks), Toast.LENGTH_LONG).show()
                }
                is TaskListFragmentState.Error -> {
                    binding.pbFragTasks.isVisible = false
                    Toast.makeText(requireContext(),state.message, Toast.LENGTH_LONG).show()
                }

                is TaskListFragmentState.Success -> {
                    binding.pbFragTasks.isVisible = false
                    val tasks = state.tasks
                    adapter.updateList(tasks)
                }
            }

        }
    }

    private fun setupListener(){
        adapter.onItemClick = {
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra(getString(R.string.task_intent), it)
            editTaskContract.launch(intent)
        }
    }

    private fun getUserId(){
        viewModel.getUserId().observe(this){id ->
            userId = id
            viewModel.getAllTasks(userId)
        }
    }

    private fun setupAdapter(){
        binding.rvFragTaskList.adapter = adapter
    }

    fun<T> Flow<T>.observe(owner : LifecycleOwner, observe : (T) -> Unit){
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(Lifecycle.State.STARTED){
                this@observe.collect(observe)
            }
        }
    }

}