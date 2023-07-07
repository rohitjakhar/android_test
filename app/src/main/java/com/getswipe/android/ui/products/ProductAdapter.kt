package com.getswipe.android.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import coil.transform.RoundedCornersTransformation
import com.getswipe.android.R
import com.getswipe.android.databinding.ItemProductBinding
import com.getswipe.android.domain.model.ProductModel
import kotlinx.coroutines.Dispatchers

class ProductAdapter : ListAdapter<ProductModel, ProductAdapter.ProductVH>(COMPARATOR) {
    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ProductModel>() {
            override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        return ProductVH(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        var lastPosition = 0
        val animation = loadAnimation(
            holder.binding.root.context,
            if (position > lastPosition) R.anim.up_from_bottom_amin else R.anim.down_from_top_anim,
        )
        holder.itemView.startAnimation(animation)
        lastPosition = position
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: ProductVH) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class ProductVH(val binding: ItemProductBinding) : ViewHolder(binding.root) {
        fun bind(item: ProductModel) = with(binding) {
            ivProduct.load(item.image) {
                transformationDispatcher(Dispatchers.IO)
                transformations(RoundedCornersTransformation(12F))
                error(R.drawable.product_image)
            }
            tvProductName.text = item.productName
            tvProductType.text = item.productType
            tvProductTax.text = item.tax.toString()
            tvProductPrice.text = item.price.toString()
        }
    }
}
