package br.edu.infnet.tasksapp.presentation.fragments.email_edit_text

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.FragmentEmailEditTextBinding

class EmailEditTextFragment : Fragment() {

    private var _binding : FragmentEmailEditTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEmailEditTextBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setupListeners()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun setupListeners(){
        binding.etEmailFrag.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()){
                    binding.tvEmailValidity.visibility = View.GONE
                }else{
                    if(!isValidEmail(p0.toString())){
                        binding.tvEmailValidity.text = getString(R.string.invalid_email)
                        binding.tvEmailValidity.visibility = View.VISIBLE
                    }else{
                        binding.tvEmailValidity.text = getString(R.string.valid_email)
                        binding.tvEmailValidity.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return email.matches(emailRegex)
    }

}