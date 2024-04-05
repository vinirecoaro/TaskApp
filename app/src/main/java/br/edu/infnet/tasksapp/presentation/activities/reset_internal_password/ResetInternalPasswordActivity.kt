package br.edu.infnet.tasksapp.presentation.activities.reset_internal_password

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityResetInternalPasswordBinding
import br.edu.infnet.tasksapp.presentation.fragments.email_edit_text.EmailEditTextFragment
import br.edu.infnet.tasksapp.presentation.fragments.password_edit_text.PasswordEditTextFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class ResetInternalPasswordActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResetInternalPasswordBinding.inflate(layoutInflater) }
    private val viewModel : ResetInternalPasswordViewModel by inject()
    private val passwordFragment = PasswordEditTextFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resetInternalPasswordToolbar.title = getString(R.string.redefine_internal_password_title)
        binding.resetInternalPasswordToolbar.setTitleTextColor(Color.WHITE)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.resetInternalPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState == null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.frag_et_password_reset_internal_password, passwordFragment)
            }
        }

        setupListeners()
    }

    private fun setupListeners(){
        binding.btResetInternalPassword.setOnClickListener {
            val editTextPasswordFrag = passwordFragment.requireView().findViewById<EditText>(R.id.et_password_frag)
            if(verifyFields(editTextPasswordFrag)){
                viewModel.setUserInternalPassword(editTextPasswordFrag.text.toString())
                Toast.makeText(this, getString(R.string.redefine_internal_password_success_message), Toast.LENGTH_LONG).show()
                editTextPasswordFrag.setText("")
            }
        }

        binding.resetInternalPasswordToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun verifyFields(vararg text: EditText): Boolean {
        for (i in text) {
            if (i.text.toString() == "" || i == null) {
                Snackbar.make(
                    binding.btResetInternalPassword, "${getString(R.string.fill_field)} ${i.hint}", Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}