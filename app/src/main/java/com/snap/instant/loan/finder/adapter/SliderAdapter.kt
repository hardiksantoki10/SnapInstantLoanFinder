package com.snap.instant.loan.finder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.adapter.SliderAdapter.SliderViewHolder

class SliderAdapter internal constructor( val sliderItems: ArrayList<Int>, val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container, parent, false
            )
        )
    }

    override fun onBindViewHolder(@NonNull holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }
    override fun getItemCount(): Int {
        return sliderItems.size
    }
    inner class SliderViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageSlide)
        }
        fun setImage(sliderItems: Int) {
//use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems)
        }
    }

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }
}