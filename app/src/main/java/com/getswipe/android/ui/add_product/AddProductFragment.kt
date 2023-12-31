package com.getswipe.android.ui.add_product

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.getswipe.android.R
import com.getswipe.android.databinding.FragmentAddProductBinding
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.utils.Constant
import com.getswipe.android.utils.Resource
import com.getswipe.android.utils.getFile
import com.getswipe.android.utils.getText
import com.getswipe.android.utils.isEmptyText
import com.getswipe.android.utils.messageDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddProductVM by viewModel()
    private var imageUri: Uri? = null
    private val loadingView by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.item_progress_bar)
            .create()
    }
    private val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { isGranted ->
                if (!isGranted.value) {
                    Snackbar.make(requireView(), "Access denied.", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUri = result.data!!.data
                binding.ivProductImage.load(result.data!!.data) {
                    transformationDispatcher(Dispatchers.Default)
                    transformations(RoundedCornersTransformation(12F))
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddProductBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        handleClick()
    }

    private fun initUi() {
        val productTypeAdapter = ArrayAdapter<String>(requireContext(), org.koin.android.R.layout.support_simple_spinner_dropdown_item, Constant.productTypeList)
        binding.dropdownProductType.setAdapter(productTypeAdapter)
    }

    private fun collectAddProductState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addProductResource.collectLatest { resource ->
                    when (resource) {
                        is Resource.FAILURE -> {
                            loadingView.dismiss()
                            requireContext().messageDialog("Error ${resource.message}") { dialog ->
                                dialog.dismiss()
                            }
                        }

                        is Resource.LOADING -> {
                            loadingView.show()
                        }

                        is Resource.SUCCESS -> {
                            loadingView.dismiss()
                            requireContext().messageDialog("Added ${resource.data}") { dialog ->
                                dialog.dismiss()
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleClick() = with(binding) {
        binding.ivProductImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), requiredPermission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val pickIntent = Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imagePicker.launch(pickIntent)
            } else {
                requestPermission.launch(arrayOf(requiredPermission))
            }
        }
        binding.btnSubmitProduct.setOnClickListener {
            collectAddProductState()
            if (validateInputs()) {
                val imageFile = imageUri?.let { getFile(it) }

                viewModel.addProduct(
                    ProductUploadModel(
                        image = imageFile,
                        productName = inputProductName.getText(),
                        productType = inputProductType.getText(),
                        price = inputProductPrice.getText(),
                        tax = inputProductTax.getText(),
                    ),
                )
            }
        }
    }

    private fun validateInputs(): Boolean = with(binding) {
        if (inputProductName.isEmptyText()) {
            inputProductName.error = "Enter Product Name"
            return@with false
        } else {
            inputProductName.isErrorEnabled = false
        }
        if (inputProductType.isEmptyText()) {
            inputProductType.error = "Enter Product Type"
            return@with false
        } else {
            inputProductName.isErrorEnabled = false
        }
        if (inputProductPrice.isEmptyText()) {
            inputProductPrice.error = "Enter Product Price"
            return@with false
        } else {
            inputProductPrice.isErrorEnabled = false
        }
        if (inputProductTax.isEmptyText()) {
            inputProductTax.error = "Enter Product Tax"
            return@with false
        } else {
            inputProductTax.isErrorEnabled = false
        }
        return@with true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
