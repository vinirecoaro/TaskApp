package br.edu.infnet.tasksapp.presentation.activities.reset_password

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.databinding.ActivityResetPasswordBinding
import br.edu.infnet.tasksapp.presentation.activities.login.LoginActivity
import br.edu.infnet.tasksapp.presentation.activities.reset_internal_password.ResetInternalPasswordActivity
import br.edu.infnet.tasksapp.presentation.fragments.button.ButtonFragment
import br.edu.infnet.tasksapp.presentation.fragments.button.OnButtonClickListener
import br.edu.infnet.tasksapp.presentation.fragments.email_edit_text.EmailEditTextFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class ResetPasswordActivity : AppCompatActivity(), OnButtonClickListener {

    private val binding by lazy { ActivityResetPasswordBinding.inflate(layoutInflater)}
    private val viewModel by viewModels<ResetPasswordViewModel>()
    private val buttonFragment = ButtonFragment()
    private val emailFragment = EmailEditTextFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resetPasswordToolbar.title = getString(R.string.redefine_password)
        binding.resetPasswordToolbar.setTitleTextColor(Color.WHITE)

        //Insert a back button on Navigation bar
        setSupportActionBar(binding.resetPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            val bundle = bundleOf(getString(R.string.button_text_key) to getString(R.string.to_sent))

            buttonFragment.arguments = bundle
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.frag_button_send, buttonFragment)
                add(R.id.frag_et_email_reset_password, emailFragment)
            }
            buttonFragment.listener = this

        }

        setUpListeners()

    }

    private fun setUpListeners(){
        viewModel.onResetPasswordSuccess = {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(getString(R.string.email_sent), true)
            startActivity(intent)
        }

        viewModel.onResetPasswordFail = {
            Snackbar.make(binding.fragButtonSend,getString(R.string.fail_redefine_password_message), Snackbar.LENGTH_LONG).show()
        }

        binding.resetPasswordToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.tvResetInternalPassword.setOnClickListener {
            startActivity(Intent(this,ResetInternalPasswordActivity::class.java))
        }
    }

    private fun verifyFields(vararg text: EditText) : Boolean{
        for (i in text){
            if (i.text.toString() == "" || i == null){
                Snackbar.make(binding.fragButtonSend,"${getString(R.string.fill_field)} ${i.hint}", Snackbar.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    override fun onButtonClicked() {
        val editTextEmailFragment = emailFragment.requireView().findViewById<EditText>(R.id.et_email_frag)
        val textViewEmailFragmet = emailFragment.requireView().findViewById<TextView>(R.id.tv_email_validity).text.toString()
        if(verifyFields(editTextEmailFragment)){
            if(textViewEmailFragmet == getString(R.string.valid_email)){
                viewModel.resetPassword(editTextEmailFragment.text.toString())
            }else{
                Toast.makeText(this, getString(R.string.type_valid_email),Toast.LENGTH_LONG).show()
            }
        }
    }

}