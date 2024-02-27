package br.edu.infnet.tasksapp.presentation.dialog

import android.app.Activity
import android.app.AlertDialog
import br.edu.infnet.tasksapp.databinding.ActivityDialogEditTextBinding

class DialogEditTextActivity(private val activity: Activity) {

    fun showDialog(title: String, topicTitle1: String, placeHolder1: String, topicTitle2: String, placeHolder2: String, onConfirm: (Pair<String,String>) -> Unit) {
        val binding = ActivityDialogEditTextBinding.inflate(activity.layoutInflater)
        val textView1 = binding.tvTopic1
        val editText1 = binding.etEditText1
        val textView2 = binding.tvTopic2
        val editText2 = binding.etEditText2

        binding.tvTitle.text = title
        textView1.text = topicTitle1
        editText1.hint = placeHolder1
        textView2.text = topicTitle2
        editText2.hint = placeHolder2

        val alertDialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setPositiveButton("Confirmar") { _, _ ->
                val text1 = editText1.text.toString()
                val text2 = editText2.text.toString()
                onConfirm(Pair(text1, text2))
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}