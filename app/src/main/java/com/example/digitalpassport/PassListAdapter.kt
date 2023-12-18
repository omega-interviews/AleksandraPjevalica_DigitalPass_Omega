package com.example.digitalpassport

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalpassport.databinding.RecyclerViewItemBinding
import android.util.Base64
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import java.nio.charset.Charset


class PassListAdapter(
    private val context: Context,
    private var passList: List<Pair<String, Pass>>?,
    val handler: Callbacks
) : RecyclerView.Adapter<PassListAdapter.PassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassViewHolder {
        val passListBinding = RecyclerViewItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return PassViewHolder(passListBinding)
    }

    override fun onBindViewHolder(holder: PassViewHolder, position: Int) {

        val pass = passList?.get(position)?.second

        pass?.let {
            holder.title.text = it.name
            holder.isActive.text = it.description

            val imageBytes = Base64.decode(it.icon, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.thumb.setImageBitmap(decodedImage)

            holder.itemView.setOnClickListener {
                    handler.handleUserData(pass)

            }
        }
    }

    override fun getItemCount(): Int {
        return passList?.size ?: 0
    }

    fun decodeBase64(encodedString: String): String {
        val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
        return decodedBytes.toString(Charset.defaultCharset())
    }
    fun updatePassList(newPassList: List<Pair<String, Pass>>?) {
        passList = newPassList
        notifyDataSetChanged()
    }

    class PassViewHolder(passListBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(passListBinding.root) {

        val title: TextView = passListBinding.title
        val isActive: TextView = passListBinding.activity
        val thumb: ImageView = passListBinding.image
    }
    interface Callbacks {
        fun handleUserData(pass: Pass)
    }
}
