package br.edu.infnet.tasksapp.presentation.dialog

import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityDialogEditTextBinding

class DialogEditTextActivity(private val activity: Activity) {

    fun showDialog(title: String, topicTitle1: String, placeHolder1: String, topicTitle2: String, placeHolder2: String, topicTitle3 : String, placeHolder3: String, onConfirm: (Triple<String,String,String>) -> Unit) {
        val binding = ActivityDialogEditTextBinding.inflate(activity.layoutInflater)
        val textView1 = binding.tvTopic1
        val editText1 = binding.etEditText1
        val textView2 = binding.tvTopic2
        val editText2 = binding.etEditText2
        val textView3 = binding.tvTopic3
        val editText3 = binding.etEditText3

        binding.tvTitle.text = title
        textView1.text = topicTitle1
        editText1.hint = placeHolder1
        textView2.text = topicTitle2
        editText2.hint = placeHolder2
        textView3.text = topicTitle3
        editText3.hint = placeHolder3

        val alertDialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setPositiveButton(activity.getString(R.string.to_confirm)) { _, _ ->
                val text1 = editText1.text.toString()
                val text2 = editText2.text.toString()
                val text3 = editText3.text.toString()
                if(text1.isEmpty() || text2.isEmpty() || text3.isEmpty()){
                    Toast.makeText(activity.applicationContext, activity.getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
                }else{
                    onConfirm(Triple(text1, text2, text3))
                }

            }
            .setNegativeButton(activity.getString(R.string.to_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}