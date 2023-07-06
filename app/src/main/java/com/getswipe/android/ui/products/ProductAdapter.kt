package com.getswipe.android.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
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

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        holder.bind(getItem(position))
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
