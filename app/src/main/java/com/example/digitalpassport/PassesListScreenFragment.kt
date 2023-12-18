package com.example.digitalpassport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digitalpassport.databinding.FragmentPassesListScreenBinding

class PassesListScreenFragment : Fragment(), PassListAdapter.Callbacks {
    private lateinit var passListBinding: FragmentPassesListScreenBinding
    private lateinit var passListAdapter: PassListAdapter

    val viewModel: PassViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passListBinding = FragmentPassesListScreenBinding.inflate(layoutInflater, container, false)
        viewModel.getPassesList().observe(viewLifecycleOwner) { list ->
            listOfPasses = list

        }
        if (listOfPasses !=  null){
            listOfPasses.forEach {
                passListAdapter = PassListAdapter(requireContext(), listOfPasses, this)
                passListAdapter.updatePassList(listOfPasses)
            }
        }


        passListBinding.passRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        passListBinding.passRecyclerView.adapter = passListAdapter

        return passListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passListAdapter.notifyDataSetChanged()
    }
    override fun handleUserData(pass: Pass) {
        viewModel.setSelectedPass(pass)
        view?.findNavController()?.navigate(R.id.action_passesListScreenFragment_to_fragmentFlashPass)


    }

}

