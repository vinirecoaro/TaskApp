package br.edu.infnet.tasksapp.presentation.activities.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.tasksapp.databinding.ItemTaskBinding
import br.edu.infnet.tasksapp.domain.model.TaskDomain

class TaskAdapter(private var taskList : List<TaskDomain>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    var onItemClick : ((TaskDomain) -> Unit)? = null

    class ViewHolder(private val binding : ItemTaskBinding, onItemClick: ((TaskDomain) -> Unit)?, taskList : List<TaskDomain>) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.tvTitle.setOnClickListener {
                onItemClick?.invoke(taskList[adapterPosition])
            }
        }

        fun bind(item : TaskDomain){
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick,
            taskList
        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        /*holder.itemView.setOnClickListener{
            onItemClick?.invoke(task)
        }*/
    }

    fun updateList(newList: List<TaskDomain>){
        taskList = newList
        notifyDataSetChanged()
    }

}