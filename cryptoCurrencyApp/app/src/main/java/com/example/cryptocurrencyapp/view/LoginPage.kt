package com.example.cryptocurrencyapp.view

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : Fragment() {

    private var email : String = ""
    private var password : String = ""
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
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
        binding.signUpTextView.setOnClickListener {
            goToSignUpPage(it)
        }

        binding.loginButton.setOnClickListener {
            logIn(it)
        }
    }

    fun goToSignUpPage(view: View){
        val action = LoginPageDirections.actionLoginPageToSignUpPage()
        Navigation.findNavController(view).navigate(action)
    }

    fun logIn(view: View){
        email = binding.emailLoginText.text.toString()
        password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val action = LoginPageDirections.actionLoginPageToCurrencyList()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            if (email.isEmpty()){
                Toast.makeText(requireContext(),"You have to enter an email.", Toast.LENGTH_LONG).show()
            }else if (password.isEmpty()){
                Toast.makeText(requireContext(),"You have to enter an password.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}