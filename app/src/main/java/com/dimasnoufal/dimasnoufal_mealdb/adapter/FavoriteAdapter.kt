package com.dimasnoufal.dimasnoufal_mealdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimasnoufal.dimasnoufal_mealdb.R
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ActivityFavoriteBinding
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ItemRowFavoriteBinding
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ItemRowRekomendasiBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<MealEntity>() {
        override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    private lateinit var onFavoriteItemCallBack: IOnFavoriteItemCallBack

    inner class FavoriteViewHolder(private val binding: ItemRowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MealEntity) {
            binding.apply {
                Glide.with(itemView.context)
                        .load(item.meal?.strMealThumb)
                        .error(R.drawable.img_placeholder)
                        .into(ivPoster)
                    tvMeal.text = item.meal?.strMeal
                itemView.setOnClickListener {
                    onFavoriteItemCallBack.onFavoriteItemClickCallback(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowBinding = ItemRowFavoriteBinding.inflate(layoutInflater, parent, false)
        return FavoriteViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        val itemData = differ.currentList[position]
        holder.bind(itemData)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setData(list: List<MealEntity?>?) {
        differ.submitList(list)
    }

    fun setOnItemClickCallback(action: IOnFavoriteItemCallBack) {
        this.onFavoriteItemCallBack = action
    }

    interface IOnFavoriteItemCallBack {
        fun onFavoriteItemClickCallback(data: MealEntity)
    }
}