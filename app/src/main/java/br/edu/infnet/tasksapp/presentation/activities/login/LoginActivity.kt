package br.edu.infnet.tasksapp.presentation.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityLoginBinding
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivity
import br.edu.infnet.tasksapp.presentation.activities.register.RegisterActivity
import br.edu.infnet.tasksapp.presentation.activities.reset_password.ResetPasswordActivity
import br.edu.infnet.tasksapp.presentation.activities.verify_email.VerifyEmailActivity
import br.edu.infnet.tasksapp.presentation.fragments.Button
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragment = Button.newInstance("Login")
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_button, fragment)
            .commit()

        resetPasswordSucess()
        setupListeners()
    }

    private fun setupListeners(){
        binding.btLogin.setOnClickListener {
            binding.btLogin.isEnabled = false
            lifecycleScope.launch (Dispatchers.Main){
                viewModel.login(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString())
            }
            binding.btLogin.isEnabled = true
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvRedifinePassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
        viewModel.onUserLogged = {userId ->
            viewModel.setUserId(userId)
            startActivity(Intent(this, MainActivity::class.java))
        }
        viewModel.onUserNotVerified = {
            startActivity(Intent(this, VerifyEmailActivity::class.java))
        }
        viewModel.onError = { message ->
            Snackbar.make(binding.btLogin, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun resetPasswordSucess(){
        val result = intent.getBooleanExtra(getString(R.string.email_sent),false)
        if(result){
            Toast.makeText(this,getString(R.string.redefine_password_success_message), Toast.LENGTH_LONG).show()
        }
    }

}