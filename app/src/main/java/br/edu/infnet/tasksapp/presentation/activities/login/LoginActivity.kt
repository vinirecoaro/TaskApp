package br.edu.infnet.tasksapp.presentation.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.tasksapp.databinding.ActivityLoginBinding
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivity
import br.edu.infnet.tasksapp.presentation.activities.register.RegisterActivity
import br.edu.infnet.tasksapp.presentation.activities.verify_email.VerifyEmailActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
        viewModel.onUserLogged = {
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
        val result = intent.getBooleanExtra("Email Enviado",false)
        if(result){
            Toast.makeText(this,"Email de redefinição de senha enviado com sucesso", Toast.LENGTH_LONG).show()
        }
    }

}