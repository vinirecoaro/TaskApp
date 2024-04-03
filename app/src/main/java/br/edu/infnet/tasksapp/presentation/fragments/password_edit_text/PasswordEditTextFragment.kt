package br.edu.infnet.tasksapp.presentation.fragments.password_edit_text

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.FragmentPasswordEditTextBinding
import java.util.regex.Pattern

class PasswordEditTextFragment : Fragment() {

    private var _binding : FragmentPasswordEditTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordEditTextBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setupListeners()

        return rootView
    }

    private fun setupListeners(){
        val defaultBackground = binding.etPasswordFrag.background
        binding.etPasswordFrag.addTextChangedListener(
            object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()){
                        binding.tvPasswordStrength.visibility = View.GONE
                        binding.etPasswordFrag.background = defaultBackground
                    }else{
                        val passwordScore = calculatePasswordDifficulty(p0.toString())
                        if(passwordScore == getString(R.string.weak_password)){
                            binding.etPasswordFrag.background = resources.getDrawable(R.drawable.radius_border_edit_text_red)
                            binding.tvPasswordStrength.setTextColor(resources.getColor(R.color.holo_red_dark))
                        }else if(passwordScore == getString(R.string.mid_password)){
                            binding.etPasswordFrag.background = resources.getDrawable(R.drawable.radius_border_edit_text_yellow)
                            binding.tvPasswordStrength.setTextColor(resources.getColor(R.color.yellow))
                        }else{
                            binding.etPasswordFrag.background = resources.getDrawable(R.drawable.radius_border_edit_text_green)
                            binding.tvPasswordStrength.setTextColor(resources.getColor(R.color.holo_green_dark))
                        }

                        binding.tvPasswordStrength.visibility = View.VISIBLE
                        binding.tvPasswordStrength.text = passwordScore
                    }
                }
            }
        )
    }

    private fun calculatePasswordDifficulty(password: String): String {
        val length = password.length
        val hasLowerCase = Pattern.compile("[a-z]").matcher(password).find()
        val hasUpperCase = Pattern.compile("[A-Z]").matcher(password).find()
        val hasDigit = Pattern.compile("\\d").matcher(password).find()
        val hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()

        var score = when {
            length < 8 -> 0
            length < 12 -> 1
            length < 16 -> 2
            else -> 3
        }

        if(hasLowerCase){
            score += 1
        }

        if(hasUpperCase){
            score += 1
        }

        if(hasDigit){
            score += 1
        }

        if(hasSpecialChar){
            score += 1
        }

        return when (score) {
            in 0..2 -> getString(R.string.weak_password)
            in 3..4 -> getString(R.string.mid_password)
            else -> getString(R.string.strong_password)
        }
    }

}