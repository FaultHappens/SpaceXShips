package com.example.spacexships.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spacexships.R
import com.example.spacexships.data.LaunchData
import com.example.spacexships.databinding.LaunchCardviewBinding
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.ListAdapter


class LaunchesListAdapter: ListAdapter <LaunchData, LaunchesListViewHolder>(TaskDiffCallBack()) {

    lateinit var initialLaunchesList: MutableList<LaunchData>
    private lateinit var binding: LaunchCardviewBinding


    class TaskDiffCallBack : DiffUtil.ItemCallback<LaunchData>() {
        override fun areItemsTheSame(oldItem: LaunchData, newItem: LaunchData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LaunchData, newItem: LaunchData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesListViewHolder {
        binding = LaunchCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaunchesListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LaunchesListViewHolder, position: Int) {
        return holder.bind(holder.itemView, getItem(position))
    }


    fun getFilter(): Filter {
        return launchFilter
    }

    private val launchFilter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filteredList: ArrayList<LaunchData> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialLaunchesList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.ROOT)
                initialLaunchesList.forEach {
                    if (it.name.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                submitList(results.values as ArrayList<LaunchData>)
            }
        }
    }

}

class LaunchesListViewHolder(private val binding: LaunchCardviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(view: View, launch: LaunchData) {
        binding.linksButton.setOnClickListener{
            val popupView: View = LayoutInflater.from(view.context).inflate(R.layout.links_popupwindow, null)
            popupView.findViewById<TextView>(R.id.article).setOnClickListener {
                openLink(launch.links.article, view)
            }
            popupView.findViewById<TextView>(R.id.flickr).setOnClickListener {
                openLink(launch.links.flickr.original[0].toString(), view)
            }
            popupView.findViewById<TextView>(R.id.patch).setOnClickListener {
                openLink(launch.links.patch.large, view)
            }
            popupView.findViewById<TextView>(R.id.presskit).setOnClickListener {
                openLink(launch.links.presskit, view)
            }
            popupView.findViewById<TextView>(R.id.reddit).setOnClickListener {
                openLink(launch.links.reddit.media.toString(), view)
            }
            popupView.findViewById<TextView>(R.id.webcast).setOnClickListener {
                openLink(launch.links.webcast, view)
            }
            popupView.findViewById<TextView>(R.id.wikipedia).setOnClickListener {
                openLink(launch.links.wikipedia, view)
            }
            popupView.findViewById<TextView>(R.id.youtube).setOnClickListener {
                openLink("https://www.youtube.com/watch?v=${launch.links.youtube_id}", view)
            }


            val popupWindow = PopupWindow(
                popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            popupView.findViewById<Button>(R.id.closeButton).setOnClickListener{
                popupWindow.dismiss()
            }
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        }
        Log.d("TEST", "SETTING")
        binding.launchName.text = launch.name
        binding.launchDescription.text = launch.details ?: "No Description"
        binding.launchYear.text = launch.date_local?.substring(0..3) ?: "No Info"

    }

    private fun openLink(link: String, view: View){
        if(link.isEmpty()) {
            Toast.makeText(view.context, "Error! No links provided", Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            view.context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            intent.setPackage(null)
            view.context.startActivity(intent)
        }
    }



}