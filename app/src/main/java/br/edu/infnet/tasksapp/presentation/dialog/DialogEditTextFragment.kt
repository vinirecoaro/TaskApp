package br.edu.infnet.tasksapp.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import br.edu.infnet.tasksapp.databinding.FragmentDialogEditTextBinding
import java.lang.IllegalArgumentException

class DialogEditTextFragment : DialogFragment() {

    private val binding by lazy { FragmentDialogEditTextBinding.inflate(layoutInflater) }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val titleText = arguments?.getString(TITLE_TEXT)
            ?: throw IllegalArgumentException("Ops, passe o título")
        val placeHolderText = arguments?.getString(PLACE_HOLDER)
            ?: throw IllegalArgumentException("Ops, passe o place holder")

        return activity?.let {

            binding.etEditText.hint = placeHolderText
            binding.tvTitle.text = titleText

            AlertDialog.Builder(it)
                .setView(binding.root)
                .setPositiveButton("Confirmar"){_,_ ->
                    setFragmentResult(
                        FRAGMENT_RESULT, bundleOf(
                            EDIT_TEXT_VALUE to binding.etEditText.text.toString()
                        )
                    )
                }
                .setNegativeButton("Cancelar"){_,_ ->
                    dismiss()
                }.create()
        } ?: throw IllegalStateException("A activity não pode ser nula ")
    }

    companion object{
        const val TITLE_TEXT = "TITLE_TEXT"
        const val PLACE_HOLDER = "PLACE_HOLDER"

        const val FRAGMENT_RESULT = "FRAGMENT_RESULT"
        const val EDIT_TEXT_VALUE = "EDIT_TEXT_VALUE"

        fun show(
            title : String,
            placeHolder : String,
            fragmentManager : FragmentManager,
            tag : String = DialogEditTextFragment::class.java.simpleName.toString()
        ){
            DialogEditTextFragment().apply {
                arguments = bundleOf(
                    TITLE_TEXT to title,
                    PLACE_HOLDER to placeHolder
                )
            }.show(
                fragmentManager,
                tag
            )
        }

    }

}