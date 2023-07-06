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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.getswipe.android.databinding.FragmentAddProductBinding
import com.getswipe.android.domain.model.ProductUploadModel
import com.getswipe.android.utils.Resource
import com.getswipe.android.utils.getFile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddProductVM by viewModels()
    private var imageUri: Uri? = null
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
                binding.ivProductImage.load(result.data!!.data)
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
        handleClick()
    }

    private fun collectAddProductState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addProductResource.collectLatest {
                    when (it) {
                        is Resource.FAILURE -> {
                            Toast.makeText(
                                requireContext(),
                                "Error ${it.message}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        is Resource.LOADING -> {
                            Toast.makeText(requireContext(), "Adding...", Toast.LENGTH_SHORT).show()
                        }

                        is Resource.SUCCESS -> {
                            Toast.makeText(requireContext(), "Added ${it.data}", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun handleClick() {
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
            val imageFile = imageUri?.let { getFile(it) }

            viewModel.addProduct(
                ProductUploadModel(
                    image = imageFile,
                    productName = binding.inputProductName.editText?.text.toString(),
                    productType = binding.inputProductType.editText?.text.toString(),
                    price = binding.inputProductPrice.editText?.text.toString(),
                    tax = binding.inputProductTax.editText?.text.toString(),
                ),
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
