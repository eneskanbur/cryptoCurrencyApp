package com.example.cryptocurrencyapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.FragmentLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : Fragment() {

    private var email: String? = null
    private var password: String? = null
    private var rememberMe: Boolean = false
    private var user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        checkRemember()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setImageResource(R.drawable.crypto)
        user = auth.currentUser

        binding.signUpTextView.setOnClickListener {
            goToSignUpPage(it)
        }

        binding.loginButton.setOnClickListener {
            logIn(it)
        }
    }

    fun goToSignUpPage(view: View) {
        val action = LoginPageDirections.actionLoginPageToSignUpPage()
        Navigation.findNavController(view).navigate(action)
    }

    private fun checkRemember() {
        if (sharedPreferences?.getBoolean("user", false) == true) {
            findNavController().navigate(LoginPageDirections.actionLoginPageToCurrencyList())
        }
    }

    fun logIn(view: View) {

        email = binding.emailLoginText.text.toString()
        password = binding.passwordText.text.toString()

        if (binding.rememberMe.isChecked) {
            with(sharedPreferences?.edit()) {
                this!!.putBoolean("user", true)
                apply()
            }
        } else {
            with(sharedPreferences?.edit()) {
                this!!.putBoolean("user", false)
                apply()
            }
        }

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            auth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = LoginPageDirections.actionLoginPageToCurrencyList()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            if (email.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.you_have_to_enter_an_email),
                    Toast.LENGTH_LONG
                ).show()
            } else if (password.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.you_have_to_enter_an_password),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}