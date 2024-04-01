package br.edu.infnet.tasksapp.presentation.activities.reset_password

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityResetPasswordBinding
import br.edu.infnet.tasksapp.presentation.activities.login.LoginActivity
import br.edu.infnet.tasksapp.presentation.fragments.button.ButtonFragment
import br.edu.infnet.tasksapp.presentation.fragments.button.OnButtonClickListener
import com.google.android.material.snackbar.Snackbar

class ResetPasswordActivity : AppCompatActivity(), OnButtonClickListener {

    private val binding by lazy { ActivityResetPasswordBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resetPasswordToolbar.title = getString(R.string.redefine_password)
        binding.resetPasswordToolbar.setTitleTextColor(Color.WHITE)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.resetPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = bundleOf(getString(R.string.button_text_key) to "Enviar")
        val fragment = ButtonFragment()
        fragment.arguments = bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.frag_button_send, fragment)
        }
        fragment.listener = this

        setUpListeners()

    }

    private fun setUpListeners(){
        viewModel.onResetPasswordSuccess = {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(getString(R.string.email_sent), true)
            startActivity(intent)
        }

        viewModel.onResetPasswordFail = {
            Snackbar.make(binding.btSend,getString(R.string.fail_redefine_password_message), Snackbar.LENGTH_LONG).show()
        }

        binding.resetPasswordToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun verifyFields(vararg text: EditText) : Boolean{
        for (i in text){
            if (i.text.toString() == "" || i == null){
                Snackbar.make(binding.btSend,"${getString(R.string.fill_field)} ${i.hint}", Snackbar.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    override fun onButtonClicked() {
        if(verifyFields(binding.etEmail)){
            viewModel.resetPassword(binding.etEmail.text.toString())
        }
    }

}