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
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemList
import com.dimasnoufal.dimasnoufal_mealdb.ui.DetailActivity

class MainAdapter :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<MealsItemList>() {
        override fun areItemsTheSame(oldItem: MealsItemList, newItem: MealsItemList): Boolean {
            return oldItem.idMeal!!.toInt() == newItem.idMeal!!.toInt()
        }

        override fun areContentsTheSame(oldItem: MealsItemList, newItem: MealsItemList): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    private lateinit var onItemCallBack: IOnItemCallBack

    inner class ViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MealsItemList) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.strMealThumb)
                    .error(R.drawable.img_placeholder)
                    .into(ivPoster)
                tvMeal.text = data.strMeal
                clRoot.setOnClickListener {
                    onItemCallBack.onItemClickCallback(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowBinding = ItemRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
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