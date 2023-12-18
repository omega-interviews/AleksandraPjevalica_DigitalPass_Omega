package com.example.digitalpassport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.digitalpassport.databinding.FragmentUpdateCredentialsScreenBinding


class UpdateCredentialsScreenFragment : Fragment() {
    private lateinit var updateCredentialsScreenFragment: FragmentUpdateCredentialsScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateCredentialsScreenFragment = FragmentUpdateCredentialsScreenBinding.inflate(layoutInflater, container, false)
        updateCredentialsScreenFragment.getReadyCredential.setOnClickListener {
            Toast.makeText(requireContext(), "Credential is created ", Toast.LENGTH_LONG).show()

        }
        updateCredentialsScreenFragment.getTimeCredential.setOnClickListener {
            Toast.makeText(activity, "Credential is created", Toast.LENGTH_LONG).show()
        }
        return updateCredentialsScreenFragment.root
    }


}