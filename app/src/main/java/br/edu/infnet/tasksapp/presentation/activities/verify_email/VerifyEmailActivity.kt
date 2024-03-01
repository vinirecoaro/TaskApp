package br.edu.infnet.tasksapp.presentation.activities.verify_email

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityVerifyEmailBinding
import br.edu.infnet.tasksapp.presentation.activities.login.LoginActivity
import br.edu.infnet.tasksapp.presentation.activities.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class VerifyEmailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVerifyEmailBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<VerifyEmailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpListeners()

    }

    private fun setUpListeners(){
        binding.btResentEmail.setOnClickListener {
            binding.btResentEmail.isEnabled = false
            lifecycleScope.launch {
                viewModel.sendEmailVerificarion()
                binding.btResentEmail.isEnabled = true
            }
        }

        binding.btLogin.setOnClickListener {
            binding.btLogin.isEnabled = false
            lifecycleScope.launch {
                viewModel.logoff()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            binding.btLogin.isEnabled = true
        }

        viewModel.isVerified.observe(this, Observer {isVerified ->
            if(isVerified){
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        viewModel.onSendEmailSuccess = {
            //Toast.makeText(this, "Email de verificação enviado com sucesso", Toast.LENGTH_LONG).show()
            Snackbar.make(binding.btResentEmail, "Email de verificação enviado com sucesso.", Snackbar.LENGTH_LONG).show()
        }

        viewModel.onSendEmailFailure = {
            //Toast.makeText(this, "Erro ao enviar email de verificação", Toast.LENGTH_LONG).show()
            Snackbar.make(binding.btResentEmail, "Erro ao enviar email de verificação.", Snackbar.LENGTH_LONG).show()
        }

    }
}