package br.edu.infnet.tasksapp.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.tasksapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val bindind by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindind.root)
    }
}