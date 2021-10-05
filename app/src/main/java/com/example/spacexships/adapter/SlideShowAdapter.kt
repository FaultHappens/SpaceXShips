package com.example.spacexships.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.spacexships.R
import com.example.spacexships.data.ShipsData

class SlideShowAdapter(
    private var shipsList: List<ShipsData>,
    private var context: Context?
): PagerAdapter() {
    private lateinit var shipsData: ShipsData
    private lateinit var itemView: View
    private lateinit var imageView: ImageView
    private var inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        shipsData = shipsList[position]
        itemView = inflater.inflate(R.layout.slide_show_item_layout , container, false)
        imageView = itemView.findViewById(R.id.image)
        if(shipsData.image == null){
            Glide.with(itemView.context)
                .load(R.drawable.elon_smoking)
                .into(imageView)
        }else {
            Glide.with(itemView.context)
                .load(shipsData.image)
                .into(imageView)
        }
        container.addView(itemView)
        return itemView
    }

    override fun getCount(): Int {
        return shipsList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}