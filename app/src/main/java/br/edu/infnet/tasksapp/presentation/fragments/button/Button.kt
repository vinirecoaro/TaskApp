package br.edu.infnet.tasksapp.presentation.fragments.button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.FragmentButtonBinding

class Button : Fragment() {
    private var _binding : FragmentButtonBinding? = null
    private val binding get() = _binding!!
    var listener: OnButtonClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentButtonBinding.inflate(inflater, container, false)
        val rootView = binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonText = requireArguments().getString("button_text")
        binding.btButtonFragment.text = buttonText

        binding.btButtonFragment.setOnClickListener {
            listener?.onButtonClicked()
        }

    }

}