package br.edu.infnet.tasksapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.tasksapp.databinding.ItemTaskBinding
import br.edu.infnet.tasksapp.domain.model.TaskDomain

class TaskAdapter : androidx.recyclerview.widget.ListAdapter<TaskDomain, TaskAdapter.ViewHolder>(DiffCalback()){

    var click : (TaskDomain) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding : ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : TaskDomain){
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    class DiffCalback : DiffUtil.ItemCallback<TaskDomain>(){
        override fun areItemsTheSame(oldItem: TaskDomain, newItem: TaskDomain) = oldItem == newItem

        override fun areContentsTheSame(oldItem: TaskDomain, newItem: TaskDomain) = oldItem.id == newItem.id

    }

}