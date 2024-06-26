package br.edu.infnet.tasksapp.presentation.activities.register

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityRegisterBinding
import br.edu.infnet.tasksapp.presentation.activities.verify_email.VerifyEmailActivity
import br.edu.infnet.tasksapp.presentation.fragments.email_edit_text.EmailEditTextFragment
import br.edu.infnet.tasksapp.presentation.fragments.password_edit_text.PasswordEditTextFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel>()
    private val passwordFragment = PasswordEditTextFragment()
    private val emailFragment = EmailEditTextFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.registerToolbar.setTitle(getString(R.string.register))
        binding.registerToolbar.setTitleTextColor(Color.WHITE)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.registerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.frag_et_password_register, passwordFragment)
                add(R.id.frag_et_email_register, emailFragment)
            }

        }

        setupListeners()
        setColorBasedOnTheme()
    }

    private fun setupListeners(){
        binding.registerToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btRegister.setOnClickListener{
            binding.btRegister.isEnabled = false
            val passwordEtFrag = passwordFragment.requireView().findViewById<EditText>(R.id.et_password_frag)
            val passwordTvFrag = passwordFragment.requireView().findViewById<TextView>(R.id.tv_password_strength).text.toString()
            val emailEtFrag = emailFragment.requireView().findViewById<EditText>(R.id.et_email_frag)
            val emailTvFrag = emailFragment.requireView().findViewById<TextView>(R.id.tv_email_validity).text.toString()
            if(
                verifyFields(binding.etName, emailEtFrag, passwordEtFrag) &&
                (passwordTvFrag == getString(R.string.mid_password) || passwordTvFrag == getString(R.string.strong_password)) &&
                emailTvFrag == getString(R.string.valid_email)
                ){
                lifecycleScope.launch(Dispatchers.Main) {
                    viewModel.createUser(
                        binding.etName.text.toString(),
                        emailEtFrag.text.toString(),
                        passwordEtFrag.text.toString())
                }
            }
            binding.btRegister.isEnabled = true
        }
        viewModel.onUserCreated = {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.sendEmailVerificarion()
            }
            startActivity(Intent(this, VerifyEmailActivity::class.java))
            finish()
        }

        viewModel.onError = { message ->
            Snackbar.make(binding.btRegister, message, Snackbar.LENGTH_LONG).show()
        }

        viewModel.onSendEmailSuccess = {
            Snackbar.make(binding.btRegister, getString(R.string.sent_email_verification_message_success), Snackbar.LENGTH_LONG).show()
        }

        viewModel.onSendEmailFailure = {
            Snackbar.make(binding.btRegister, R.string.sent_email_verification_error, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setColorBasedOnTheme(){
        when (this.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_person_24_light,0, 0, 0)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_person_24_black,0, 0, 0)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }
    }

    private fun verifyFields(vararg text: EditText): Boolean {
        for (i in text) {
            if (i.text.toString() == "" || i == null) {
                Snackbar.make(
                    binding.btRegister, "${getString(R.string.fill_field)} ${i.hint}", Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}