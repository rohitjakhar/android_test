package com.getswipe.android.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.getswipe.android.R
import com.getswipe.android.databinding.FragmentProductsBinding
import com.getswipe.android.utils.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsVM by viewModel()
    private val productAdapter by lazy { ProductAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        handleClicks()
        collectProducts()
    }

    private fun initUi() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun handleClicks() {
        binding.floatingAddProduct.setOnClickListener {
            val navOption = navOptions {
                anim {
                    enter = R.anim.up_from_bottom_amin
                }
            }
            findNavController().navigate(
                R.id.addProductFragment,
                navOptions = navOption,
                args = null,
            )
        }
    }

    private fun collectProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.productListResource.collectLatest {
                    when (it) {
                        is Resource.FAILURE -> {
                            binding.tvErrorMessage.text = it.message
                            binding.rvProducts.isVisible = false
                            binding.progressBar.isVisible = false
                            binding.tvErrorMessage.isVisible = true
                        }

                        is Resource.LOADING -> {
                            binding.rvProducts.isVisible = false
                            binding.progressBar.isVisible = true
                            binding.tvErrorMessage.isVisible = false
                        }

                        is Resource.SUCCESS -> {
                            binding.rvProducts.isVisible = true
                            binding.progressBar.isVisible = false
                            binding.tvErrorMessage.isVisible = false
                            productAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
