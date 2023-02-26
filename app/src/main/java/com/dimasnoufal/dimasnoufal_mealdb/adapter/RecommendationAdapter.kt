package com.dimasnoufal.dimasnoufal_mealdb.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimasnoufal.dimasnoufal_mealdb.R
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ItemRowBinding
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ItemRowRekomendasiBinding
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemList
import com.dimasnoufal.dimasnoufal_mealdb.ui.DetailActivity

class RecommendationAdapter :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<MealsItemList>() {
        override fun areItemsTheSame(oldItem: MealsItemList, newItem: MealsItemList): Boolean {
            return oldItem.idMeal!!.toInt() == newItem.idMeal!!.toInt()
        }

        override fun areContentsTheSame(oldItem: MealsItemList, newItem: MealsItemList): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    private lateinit var onItemCallBack: RecommendationAdapter.IOnItemCallBack

    inner class RecommendationViewHolder(private val binding: ItemRowRekomendasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MealsItemList) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.strMealThumb)
                    .error(R.drawable.img_placeholder)
                    .into(ivRecommendationMeal)
                cvRecommendationMeal.setOnClickListener {
                    onItemCallBack.onItemClickCallback(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationAdapter.RecommendationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowBinding = ItemRowRekomendasiBinding.inflate(layoutInflater, parent, false)
        return RecommendationViewHolder(rowBinding)
    }

    override fun onBindViewHolder(
        holder: RecommendationAdapter.RecommendationViewHolder,
        position: Int
    ) {
        val itemData = differ.currentList[position]
        holder.bind(itemData)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setData(list: List<MealsItemList?>?) {
        differ.submitList(list)
    }

    fun setOnItemClickCallback(action: IOnItemCallBack) {
        this.onItemCallBack = action
    }

    interface IOnItemCallBack {
        fun onItemClickCallback(data: MealsItemList)
    }
}