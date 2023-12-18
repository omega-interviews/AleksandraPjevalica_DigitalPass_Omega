package com.example.digitalpassport

import CheckMarkCircleDrawable
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.digitalpassport.databinding.FragmentFlashPassScreenBinding
import com.example.digitalpassport.databinding.FragmentPassesListScreenBinding
import com.example.digitalpassport.databinding.RecyclerViewItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

lateinit var flashScreenBinding: FragmentFlashPassScreenBinding
lateinit var flashPass: Pass
lateinit var flashUser: User
 var flashTime =  0L
 var flashReady = 0L
lateinit var  checkMarkDrawable : CheckMarkCircleDrawable


class FragmentFlashPass : Fragment() {
    val viewModel: PassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flashScreenBinding = FragmentFlashPassScreenBinding.inflate(layoutInflater, container, false)

        viewModel.getTime().observe(viewLifecycleOwner) { time ->
            flashTime = time

        }

        viewModel.getReady().observe(viewLifecycleOwner) { ready ->
            flashReady = ready

        }



        return flashScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkMarkDrawable = CheckMarkCircleDrawable(requireContext())
        viewModel.getSelectedPass().observe(viewLifecycleOwner) { selectedPass ->
            flashPass = selectedPass
            if (flashPass != null){
                flashScreenBinding.title.text = flashPass.name
                flashScreenBinding.description.text = flashPass.description
                val imageBytes = Base64.decode(flashPass.icon, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                flashScreenBinding.image.setImageBitmap(decodedImage)
            }

        }


        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            flashUser = user
            if (flashUser != null){
                flashScreenBinding.userName.text = flashUser.firstName + " " + flashUser.lastName
                val imageBytes = Base64.decode(flashUser.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                flashScreenBinding.userIcon.setImageBitmap(decodedImage)
            }

        }

        viewModel.getTime().observe(viewLifecycleOwner) { time ->
            flashTime = time
            if (flashTime != null){
                flashScreenBinding.timeTv.text = "Time expiration: "+ formatTimestamp(flashTime)
            }

        }

        viewModel.getReady().observe(viewLifecycleOwner) { ready ->
            flashReady = ready
            if (flashReady != null){
                flashScreenBinding.readyTv.text = "Ready expiration: " + formatTimestamp(flashReady)
            }

        }
        val layerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.square_frame) as LayerDrawable

        val borderIndex = 0

        val borderShapeDrawable = layerDrawable.getDrawable(borderIndex) as GradientDrawable



        if (flashTime < System.currentTimeMillis() || flashReady < System.currentTimeMillis()){
            checkMarkDrawable.circleColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            checkMarkDrawable.tickColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            flashScreenBinding.activityIcon.setImageDrawable(checkMarkDrawable)
            val newBorderColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            borderShapeDrawable.setColor(newBorderColor)

            flashScreenBinding.squareFrameView.background = layerDrawable
        }
        else{
            checkMarkDrawable.circleColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            checkMarkDrawable.tickColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            flashScreenBinding.activityIcon.setImageDrawable(checkMarkDrawable)
            val newBorderColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            borderShapeDrawable.setColor(newBorderColor)

            flashScreenBinding.squareFrameView.background = layerDrawable
        }
        flashScreenBinding.updateCredentialsButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFlashPass_to_updateCredentialsScreenFragment)

        }
    }

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * 1000L) // Multiply by 1000 to convert seconds to milliseconds
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
}
