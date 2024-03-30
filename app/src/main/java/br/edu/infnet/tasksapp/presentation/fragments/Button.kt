package br.edu.infnet.tasksapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.FragmentButtonBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class Button : Fragment() {
    private var param1: String? = null
    private var _binding : FragmentButtonBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentButtonBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.btButtonFragment.text = param1

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            Button().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}